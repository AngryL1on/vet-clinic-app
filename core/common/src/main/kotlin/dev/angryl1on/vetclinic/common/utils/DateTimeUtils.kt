package dev.angryl1on.vetclinic.common.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun formatDateForCIS(input: String): String {
    return try {
        val parsed = LocalDate.parse(input)
        parsed.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } catch (e: Exception) {
        input // если парсинг не удался — fallback
    }
}

fun formatTimeShort(input: String): String {
    return try {
        LocalTime.parse(input).format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        input
    }
}
