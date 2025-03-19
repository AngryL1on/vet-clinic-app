package dev.angryl1on.vetclinic.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Пример упрощенного форматирования номера:
 * 1) Если начинается с '8' или '7', убираем эту цифру и выводим +7
 * 2) Далее идёт скобка и следующие 3 цифры
 * 3) Скобка закрывается, следующие 3 цифры, затем -
 * 4) Следующие 2 цифры, затем -
 * 5) Оставшиеся 2 цифры
 *
 * Пример:
 * - Пользователь вводит:  89997775634
 * - Сырые цифры:          89997775634
 * - Форматированная строка: +7(999)777-56-34
 */
object PhoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        // 1. Оставляем только цифры
        val digits = text.text.filter { it.isDigit() }

        // Если ничего не ввели – ничего не показываем
        if (digits.isEmpty()) {
            return TransformedText(
                text = AnnotatedString(""),
                offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = 0
                    override fun transformedToOriginal(offset: Int): Int = 0
                }
            )
        }

        // 2. Убираем ведущую '8' или '7' (если есть), воспринимая её как +7
        val trimmed = when {
            digits.startsWith("8") || digits.startsWith("7") -> digits.drop(1)
            else -> digits
        }

        // 3. Строим результирующую строку, добавляя +7
        val result = StringBuilder("+7")

        // Поэтапно формируем маску: +7(XXX)XXX-XX-XX
        if (trimmed.isNotEmpty()) {
            // Открывающая скобка + первые 3 цифры
            result.append("(")
            val firstGroup = trimmed.take(3)
            result.append(firstGroup)
            if (trimmed.length >= 3) {
                result.append(")")
            }
            // Следующие 3 цифры
            if (trimmed.length > 3) {
                val secondGroup = trimmed.substring(3, trimmed.length.coerceAtMost(6))
                result.append(secondGroup)
            }
            // Первый дефис + 2 цифры
            if (trimmed.length > 6) {
                result.append("-")
                val thirdGroup = trimmed.substring(6, trimmed.length.coerceAtMost(8))
                result.append(thirdGroup)
            }
            // Второй дефис + 2 цифры
            if (trimmed.length > 8) {
                result.append("-")
                val fourthGroup = trimmed.substring(8, trimmed.length.coerceAtMost(10))
                result.append(fourthGroup)
            }
        }

        // Упрощенный OffsetMapping: курсор всегда идёт в конец
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = result.length
            override fun transformedToOriginal(offset: Int): Int = digits.length
        }

        return TransformedText(AnnotatedString(result.toString()), offsetMapping)
    }
}
