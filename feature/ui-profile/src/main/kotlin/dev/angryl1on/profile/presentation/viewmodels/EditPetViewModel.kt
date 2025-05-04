package dev.angryl1on.profile.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.Immutable
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.petservice.EditPetUseCase
import dev.angryl1on.vetclinic.domain.usecase.petservice.UploadPhotoUseCase
import dev.angryl1on.vetclinic.model.ErrorResponse
import dev.angryl1on.vetclinic.model.pet.PetRequest
import dev.angryl1on.vetclinic.model.pet.PetResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditPetViewModel(
    private val initialPet: PetResponse,
    private val editPetUseCase: EditPetUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel<EditPetScreenState, EditPetScreenIntent>() {

    /**
     * MVI-infrastructure
     */
    private val reducer = EditPetScreenReducer(EditPetScreenState.Init)
    override val state: Flow<EditPetScreenState>
        get() = reducer.state

    /**
     * form fields
     */
    private val _name = MutableStateFlow(initialPet.name)
    private val _animalType = MutableStateFlow(initialPet.animalType)
    private val _breed = MutableStateFlow(initialPet.breed)
    private val _birthDate = MutableStateFlow(
        // API хранит ISO‑формат, приводим к dd.MM.yyyy
        runCatching {
            SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(initialPet.birthDate)
        }.getOrNull()?.let {
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(it)
        } ?: initialPet.birthDate
    )
    private val _photoUri = MutableStateFlow(
        initialPet.photoUrl?.toUri()
    )

    // Error state for fields
    private val _nameError = MutableStateFlow<String?>(null)
    private val _animalTypeError = MutableStateFlow<String?>(null)
    private val _breedError = MutableStateFlow<String?>(null)
    private val _birthDateError = MutableStateFlow<String?>(null)

    private val petId: Long = initialPet.id
    val name: StateFlow<String> get() = _name
    val animalType: StateFlow<String> get() = _animalType
    val breed: StateFlow<String> get() = _breed
    val birthDate: StateFlow<String> get() = _birthDate
    val photoUri: StateFlow<Uri?> get() = _photoUri

    val nameError: StateFlow<String?> get() = _nameError
    val animalTypeError: StateFlow<String?> get() = _animalTypeError
    val breedError: StateFlow<String?> get() = _breedError
    val birthDateError: StateFlow<String?> get() = _birthDateError

    /**
     * public callbacks
     */
    fun onNameChanged(value: String) = dispatch(EditPetScreenIntent.NameChanged(value))
    fun onAnimalTypeChanged(value: String) = dispatch(EditPetScreenIntent.AnimalTypeChanged(value))
    fun onBreedChanged(value: String) = dispatch(EditPetScreenIntent.BreedChanged(value))
    fun onBirthDateChanged(value: String) = dispatch(EditPetScreenIntent.BirthDateChanged(value))
    fun onPhotoSelected(uri: Uri?) = dispatch(EditPetScreenIntent.PhotoChanged(uri))
    fun submit() = dispatch(EditPetScreenIntent.Submit)

    /**
     * dispatcher
     */
    private fun dispatch(intent: EditPetScreenIntent) {
        when (intent) {
            is EditPetScreenIntent.NameChanged -> {
                _name.value = intent.value
                _nameError.value = validateName(intent.value)
            }

            is EditPetScreenIntent.AnimalTypeChanged -> {
                _animalType.value = intent.value
                _animalTypeError.value = validateAnimalType(intent.value)
            }

            is EditPetScreenIntent.BreedChanged -> {
                _breed.value = intent.value
                _breedError.value = validateBreed(intent.value)
            }

            is EditPetScreenIntent.BirthDateChanged -> {
                _birthDate.value = intent.value
                _birthDateError.value = validateBirthDate(intent.value)
            }

            is EditPetScreenIntent.PhotoChanged -> _photoUri.value = intent.uri

            EditPetScreenIntent.Submit -> processSubmit()

            else -> {}
        }

        reducer.sendIntent(intent)
    }

    /**
     * business logic
     */
    private fun processSubmit() = viewModelScope.launch {
        if (!isFormValid()) {
            reducer.sendIntent(
                EditPetScreenIntent.SubmitFailure(
                    ErrorResponse(-1, "Заполните все обязательные поля")
                )
            )

            // Validate all fields before submitting
            val nameErr = validateName(_name.value)
            _nameError.value = nameErr
            val typeErr = validateAnimalType(_animalType.value)
            _animalTypeError.value = typeErr
            val breedErr = validateBreed(_breed.value)
            _breedError.value = breedErr
            val dateErr = validateBirthDate(_birthDate.value)
            _birthDateError.value = dateErr

            return@launch
        }

        reducer.sendIntent(EditPetScreenIntent.SubmitStart)

        val birthIso = runCatching {
            // парсим dd.MM.yyyy → Date
            val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = parser.parse(_birthDate.value)!!
            // форматируем в yyyy-MM-dd
            SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
        }.getOrDefault(_birthDate.value) // если парсинг упал, отдадим как есть


        val body = PetRequest(
            name = _name.value.trim(),
            animalType = _animalType.value.trim(),
            birthDate = birthIso,
            breed = _breed.value.trim()
        )
        Timber.tag("model").d(body.toString())
        editPetUseCase(id = petId, model = body)
            .onSuccess { updated ->
                reducer.sendIntent(EditPetScreenIntent.SubmitSuccess(updated))
            }
            .onFailure { t ->
                reducer.sendIntent(
                    EditPetScreenIntent.SubmitFailure(
                        ErrorResponse(-1, t.message)
                    )
                )
            }
    }

    /**
     * Загружает текущее фото (_photoUri) на сервер, логирует детали
     * и в случае не-200 отвечает ошибкой с телом ответа.
     */
    fun uploadPhoto(context: Context) = viewModelScope.launch {
        val uri = _photoUri.value ?: run {
            reducer.sendIntent(
                EditPetScreenIntent.SubmitFailure(
                    ErrorResponse(-1, "Файл не выбран")
                )
            )
            return@launch
        }

        // Определяем mimeType и расширение
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val ext = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(mimeType) ?: "jpeg"

        val file = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { input ->
                val tmp = File(context.cacheDir, "pet_${initialPet.id}.$ext")
                tmp.outputStream().use { output ->
                    input.copyTo(output)
                }
                tmp
            } ?: throw IllegalStateException("Не удалось прочитать URI $uri")
        }

        Timber.d("Uploading file ${file.name}, mimeType=$mimeType, size=${file.length()}")

        reducer.sendIntent(EditPetScreenIntent.SubmitStart)

        try {
            Timber.d("uploadPhotoUseCase called with ${initialPet.id} and ${file.name}")
            val updatedPet = uploadPhotoUseCase(initialPet.id, file)
                .getOrThrow()
            _photoUri.value = updatedPet.photoUrl?.toUri()
            reducer.sendIntent(EditPetScreenIntent.SubmitSuccess(updatedPet))

        } catch (e: Exception) {
            Timber.e(e, "Photo upload failed: ${e.message}")
            reducer.sendIntent(
                EditPetScreenIntent.SubmitFailure(
                    ErrorResponse(-1, e.message)
                )
            )
        }
    }

    private fun isFormValid(): Boolean =
        _name.value.isNotBlank() &&
                _animalType.value.isNotBlank() &&
                _breed.value.isNotBlank() &&
                _birthDate.value.isNotBlank()

    private fun clearForm() {
        _name.value = ""
        _animalType.value = ""
        _breed.value = ""
        _birthDate.value = ""
        _photoUri.value = null
    }

    private fun validateName(value: String): String? = when {
        value.isBlank() -> "Введите имя питомца"
        value.any { it.isDigit() } -> "Имя не должно содержать цифр"
        else -> null
    }

    private fun validateAnimalType(value: String): String? = when {
        value.isBlank() -> "Введите тип животного"
        value.any { it.isDigit() } -> "Тип животного не должен содержать цифр"
        else -> null
    }

    private fun validateBreed(value: String): String? = when {
        value.isBlank() -> "Введите породу"
        value.any { it.isDigit() } -> "Порода не должна содержать цифр"
        else -> null
    }

    private fun validateBirthDate(value: String): String? {
        return when {
            value.isBlank() -> "Введите дату рождения"
            else -> {
                val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val parsed =
                    runCatching { parser.parse(value) }.getOrNull() ?: return "Неверный формат даты"
                if (parsed.after(Date())) "Дата не может быть в будущем" else null
            }
        }
    }

    /**
     * Reducer
     */
    private class EditPetScreenReducer(initial: EditPetScreenState) :
        Reducer<EditPetScreenState, EditPetScreenIntent>(initial) {

        override fun reduce(oldState: EditPetScreenState, intent: EditPetScreenIntent) {
            when (intent) {
                is EditPetScreenIntent.SubmitStart -> setState(EditPetScreenState.Loading)
                is EditPetScreenIntent.SubmitSuccess -> setState(EditPetScreenState.Success(intent.createdPet))
                is EditPetScreenIntent.SubmitFailure -> setState(EditPetScreenState.Error(intent.error.message))
                else -> Unit // изменения полей UI-state не трогают
            }
        }
    }
}

/**
 * INTENTS
 */
@Immutable
sealed class EditPetScreenIntent : ModelIntent {
    data class NameChanged(val value: String) : EditPetScreenIntent()
    data class AnimalTypeChanged(val value: String) : EditPetScreenIntent()
    data class BreedChanged(val value: String) : EditPetScreenIntent()
    data class BirthDateChanged(val value: String) : EditPetScreenIntent()
    data class PhotoChanged(val uri: Uri?) : EditPetScreenIntent()

    data object Submit : EditPetScreenIntent()
    data object SubmitStart : EditPetScreenIntent()
    data class SubmitSuccess(val createdPet: PetResponse) : EditPetScreenIntent()
    data class SubmitFailure(val error: ErrorResponse) : EditPetScreenIntent()
}

/**
 * STATE
 */
@Immutable
sealed class EditPetScreenState : UiState {
    data object Init : EditPetScreenState()
    data object Loading : EditPetScreenState()
    data class Success(val createdPet: PetResponse) : EditPetScreenState()
    data class Error(val message: String?) : EditPetScreenState()
}
