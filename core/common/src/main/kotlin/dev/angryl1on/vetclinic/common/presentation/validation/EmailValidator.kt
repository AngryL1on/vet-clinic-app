package dev.angryl1on.vetclinic.common.presentation.validation

import android.util.Patterns
import dev.angryl1on.vetclinic.common.presentation.validation.models.ValidationResult
import dev.angryl1on.vetclinic.common.presentation.Validator

/**
 * Валидатор, который проверяет, является ли строка действительным форматом электронной почты
 *
 * Этот класс реализует [Validator] интерфейс и проверяет строки, чтобы убедиться, что они соответствуют стандартному формату электронной почты
 * @param input электронная почта для проверки
 * @return [ValidationResult] результат проверки
 */
class EmailValidator : Validator<String, ValidationResult> {

    override fun validate(input: String): ValidationResult {
        return if (!Patterns.EMAIL_ADDRESS.matcher(input).matches() || input.isEmpty()) {
            ValidationResult(
                isSuccess = false,
                errorText = "Неверный формат электронной почты"
            )
        } else {
            ValidationResult(isSuccess = true)
        }
    }
}
