package dev.angryl1on.vetclinic.common.presentation.validation

import dev.angryl1on.vetclinic.common.presentation.validation.models.ValidationResult
import dev.angryl1on.vetclinic.common.presentation.Validator

/**
 * Валидатор, который проверяет, соответствует ли пароль требования к минимальной длине
 *
 * Этот валидатор гарантирует, что входная строка (пароль) имеет длину в 8 символов.
 * @param input Строка пароля для проверки
 * @return [ValidationResult] результат проверки
 */
class PasswordLengthValidator : Validator<String, ValidationResult> {

    override fun validate(input: String): ValidationResult {
        if (input.length < 8) {
            return ValidationResult(
                isSuccess = false,
                errorText = "Длина пароля должна быть от 8 символов"
            )
        }
        return ValidationResult(isSuccess = true)
    }
}
