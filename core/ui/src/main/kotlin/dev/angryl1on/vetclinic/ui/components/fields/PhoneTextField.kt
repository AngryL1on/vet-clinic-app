package dev.angryl1on.vetclinic.ui.components.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.utils.PhoneVisualTransformation

@Composable
fun PhoneTextField(
    phone: String,
    isError: Boolean = false,
    errorText: String? = null,
    onPhoneChange: (String) -> Unit
) {
    PrimaryTextField(
        modifier = Modifier.fillMaxWidth(),
        value = phone,
        isError = isError,
        errorText = errorText,
        title = stringResource(R.string.phone_number),
        placeholder = stringResource(R.string.phone_number_enter),
        supportText = stringResource(R.string.phone_enter_sup_text),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
        visualTransformation = PhoneVisualTransformation,   // см. ниже
        maxQuantityOfChar = 11, // чтобы TextField сам не принимал больше 11 цифр
        isMaxQuantityOfCharVisible = false,
        onTextChange = { rawInput ->
            onPhoneChange(normalizePhone(rawInput))
        }
    )
}

/**
 * Приводит пользовательский ввод к 11‑значной строке «79999999999».
 */
/** Приводит ввод к одиннадцати цифрам формата 79999999999. */
private fun normalizePhone(input: String): String {
    val digits = input.filter(Char::isDigit)
    if (digits.isEmpty()) return ""

    // Всегда добавляем/исправляем префикс так, чтобы результат начинался с 7
    val withPrefix = when {
        digits.startsWith("8") -> "7" + digits.drop(1)   // 8xxxxxxxxxx → 7xxxxxxxxxx
        digits.startsWith("7") -> digits                 // 7xxxxxxxxxx  → 7xxxxxxxxxx
        else                  -> "7$digits"             // xxxxxxxxxx   → 7xxxxxxxxxx (или короче)
    }

    return withPrefix.take(11)                          // обрезаем лишнее
}
