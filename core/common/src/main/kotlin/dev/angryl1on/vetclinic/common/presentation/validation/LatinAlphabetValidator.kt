package dev.angryl1on.vetclinic.common.presentation.validation

import dev.angryl1on.vetclinic.common.presentation.validation.models.ValidationResult
import dev.angryl1on.vetclinic.common.presentation.Validator

/**
 * Валидатор, который проверяет, содержит ли строка только латинские символы, цифры и конкретные специальные символы.
 *
 * Валидация гарантирует, что входная строка состоит из латинских букв (A-Z, A-Z), цифр (0-9) и специальных символов `@`, `#`, `$`, `%`, `^`, `&`, `+` и `=`
 * @param input Строка, которая должна быть проверена
 * @return [ValidationResult] результат проверки
 */
class LatinAlphabetValidator : Validator<String, ValidationResult> {

    override fun validate(input: String): ValidationResult {
        val regex = "^[a-zA-Z0-9@#\$%^&+=!]*$".toRegex()
        return if (!input.matches(regex)) {
            ValidationResult(
                isSuccess = false,
                errorText = "Пароль должен содержать латинские символы, цифры или специальные символы @#\$%^&+=!"
            )
        } else {
            ValidationResult(
                isSuccess = true
            )
        }
    }
}
