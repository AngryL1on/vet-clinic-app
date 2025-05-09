package dev.angryl1on.vetclinic.common.navigation

import dev.angryl1on.vetclinic.domain.navigation.Route

/**
 * Утилиты
 */

/** «Красивое» имя экрана из route‑строки (способ без хардкода длинных FQCN). */
fun String.prettyName(): String =
    substringAfterLast('.')
        .replace("([A-Z])".toRegex(), " $1")
        .trim()

/** Route#routeName понадобится, чтобы не повторять FQCN. */
val Route.routeName: String
    get() = this::class.qualifiedName!!
