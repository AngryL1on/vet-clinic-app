package dev.angryl1on.vetclinic.common.presentation.validation

import dev.angryl1on.vetclinic.common.presentation.validation.models.ValidationResult
import dev.angryl1on.vetclinic.common.presentation.Validator

/**
 * * Валидатор, который проверяет, содержит ли строка как минимум одну заглавную букву
 *
 * Этот класс реализует [Validator] интерфейс и проверяет пароль
 * @param [input] Пароль для проверки
 * @return [ValidationResult] результат, содержит ли пароль как минимум одну заглавную букву
 */
class CapitalCharValidator : Validator<String, ValidationResult> {

    override fun validate(input: String): ValidationResult {
        return if (input.any { it.isUpperCase() }) {
            ValidationResult(isSuccess = true)
        } else {
            ValidationResult(
                isSuccess = false,
                errorText = "Пароль должен содержать заглавную букву"
            )
        }
    }
}
