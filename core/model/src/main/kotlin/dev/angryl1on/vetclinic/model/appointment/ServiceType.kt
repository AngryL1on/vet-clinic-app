package dev.angryl1on.vetclinic.model.appointment

/**
 * Перечисление доступных типов ветеринарных услуг с отображаемыми названиями.
 *
 * @property displayName Читаемое имя услуги, отображаемое в пользовательском интерфейсе.
 */
enum class ServiceType(val displayName: String) {

    /** Консультация ветеринара */
    CONSULTATION("Консультация"),

    /** Рентгеновское обследование */
    XRAY("Рентген"),

    /** Вакцинация животного */
    VACCINATION("Вакцинация"),

    /** Хирургическая операция */
    SURGERY("Операция"),

    /** Общий осмотр животного */
    CHECKUP("Осмотр"),

    /** Процедура чипирования */
    CHIPPING("Чипирование");

    companion object {
        /**
         * Получает соответствующий [ServiceType] по отображаемому имени.
         *
         * @param name Отображаемое имя (например, "Консультация").
         * @return [ServiceType], соответствующий заданному имени, либо `null`, если не найден.
         */
        fun fromDisplayName(name: String): ServiceType? {
            return entries.firstOrNull { it.displayName == name }
        }
    }
}
