package dev.angryl1on.profile.presentation.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import dev.angryl1on.vetclinic.common.presentation.ModelIntent
import dev.angryl1on.vetclinic.common.presentation.Reducer
import dev.angryl1on.vetclinic.common.presentation.UiState
import dev.angryl1on.vetclinic.common.presentation.ViewModel
import dev.angryl1on.vetclinic.domain.usecase.petservice.CreatePetUseCase
import dev.angryl1on.vetclinic.model.pet.PetRequest
import dev.angryl1on.vetclinic.model.pet.PetResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddPetViewModel(
    private val createPetUseCase: CreatePetUseCase
) : ViewModel<AddPetScreenState, AddPetScreenIntent>() {

    /**
     * MVI-infrastructure
     */
    private val reducer = AddPetScreenReducer(AddPetScreenState.Init)
    override val state: Flow<AddPetScreenState>
        get() = reducer.state

    /**
     * form fields
     */
    private val _name = MutableStateFlow("")
    private val _animalType = MutableStateFlow("")
    private val _breed = MutableStateFlow("")
    private val _birthDate = MutableStateFlow("") // dd.MM.yyyy

    // Error state for fields
    private val _nameError = MutableStateFlow<String?>(null)
    private val _animalTypeError = MutableStateFlow<String?>(null)
    private val _breedError = MutableStateFlow<String?>(null)
    private val _birthDateError = MutableStateFlow<String?>(null)


    val name: StateFlow<String> get() = _name
    val animalType: StateFlow<String> get() = _animalType
    val breed: StateFlow<String> get() = _breed
    private val birthDate: StateFlow<String> get() = _birthDate

    val nameError: StateFlow<String?> get() = _nameError
    val animalTypeError: StateFlow<String?> get() = _animalTypeError
    val breedError: StateFlow<String?> get() = _breedError
    val birthDateError: StateFlow<String?> get() = _birthDateError

    /**
     * public callbacks
     */
    fun onNameChanged(value: String) = dispatch(AddPetScreenIntent.NameChanged(value))
    fun onAnimalTypeChanged(value: String) = dispatch(AddPetScreenIntent.AnimalTypeChanged(value))
    fun onBreedChanged(value: String) = dispatch(AddPetScreenIntent.BreedChanged(value))
    fun onBirthDateChanged(value: String) = dispatch(AddPetScreenIntent.BirthDateChanged(value))
    fun submit() = dispatch(AddPetScreenIntent.Submit)

    /**
     * dispatcher
     */
    private fun dispatch(intent: AddPetScreenIntent) {
        when (intent) {
            is AddPetScreenIntent.NameChanged -> {
                _name.value = intent.value
                _nameError.value = validateName(intent.value)
            }
            is AddPetScreenIntent.AnimalTypeChanged -> {
                _animalType.value = intent.value
                _animalTypeError.value = validateAnimalType(intent.value)
            }
            is AddPetScreenIntent.BreedChanged -> {
                _breed.value = intent.value
                _breedError.value = validateBreed(intent.value)
            }
            is AddPetScreenIntent.BirthDateChanged -> {
                _birthDate.value = intent.value
                _birthDateError.value = validateBirthDate(intent.value)
            }

            AddPetScreenIntent.Submit -> processSubmit()

            else -> { /* do nothing */ }
        }

        reducer.sendIntent(intent)
    }

    /**
     * business logic
     */
    private fun processSubmit() {
        viewModelScope.launch {

            if (!isFormValid()) {
                reducer.sendIntent(
                    AddPetScreenIntent.SubmitFailure("Заполните все обязательные поля")
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

            reducer.sendIntent(AddPetScreenIntent.SubmitStart)

            // 1. Формируем ISO-дату
            val birthIso = runCatching {
                // парсим dd.MM.yyyy → Date
                val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val date = parser.parse(birthDate.value)!!
                // форматируем в yyyy-MM-dd
                SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
            }.getOrDefault(birthDate.value) // если парсинг упал, отдадим как есть

            val pet = PetRequest(
                name = _name.value.trim(),
                animalType = _animalType.value.trim(),
                birthDate = birthIso,
                breed = _breed.value.trim()
            )


            createPetUseCase(pet)
                .onSuccess { created ->
                    reducer.sendIntent(AddPetScreenIntent.SubmitSuccess(created))
                    clearForm()
                }
                .onFailure { throwable ->
                    reducer.sendIntent(
                        AddPetScreenIntent.SubmitFailure(throwable.message.toString())
                    )
                }

            Timber.tag("request AddPet").d(pet.toString())
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
                val parsed = runCatching { parser.parse(value) }.getOrNull() ?: return "Неверный формат даты"
                if (parsed.after(Date())) "Дата не может быть в будущем" else null
            }
        }
    }

    /**
     * Reducer
     */
    private class AddPetScreenReducer(initial: AddPetScreenState) :
        Reducer<AddPetScreenState, AddPetScreenIntent>(initial) {

        override fun reduce(oldState: AddPetScreenState, intent: AddPetScreenIntent) {
            when (intent) {
                is AddPetScreenIntent.SubmitStart -> setState(AddPetScreenState.Loading)
                is AddPetScreenIntent.SubmitSuccess -> setState(AddPetScreenState.Success(intent.createdPet))
                is AddPetScreenIntent.SubmitFailure -> setState(AddPetScreenState.Error(intent.error))

                else -> Unit // изменения полей UI-state не трогают
            }
        }
    }
}

/**
 * INTENTS
 */
@Immutable
sealed class AddPetScreenIntent : ModelIntent {
    data class NameChanged(val value: String) : AddPetScreenIntent()
    data class AnimalTypeChanged(val value: String) : AddPetScreenIntent()
    data class BreedChanged(val value: String) : AddPetScreenIntent()
    data class BirthDateChanged(val value: String) : AddPetScreenIntent()

    data object Submit : AddPetScreenIntent()
    data object SubmitStart : AddPetScreenIntent()
    data class SubmitSuccess(val createdPet: PetResponse) : AddPetScreenIntent()
    data class SubmitFailure(val error: String) : AddPetScreenIntent()
}

/**
 * STATE
 */
@Immutable
sealed class AddPetScreenState : UiState {
    data object Init : AddPetScreenState()
    data object Loading : AddPetScreenState()
    data class Success(val createdPet: PetResponse) : AddPetScreenState()
    data class Error(val message: String) : AddPetScreenState()
}
