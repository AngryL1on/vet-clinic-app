package dev.angryl1on.vetclinic.common.presentation.validation.models

import androidx.compose.runtime.Immutable

/**
 * Data class that represents the result of a validation process
 *
 * @property isSuccess A boolean value that indicates whether the validation was successful or not
 * @property errorText An optional string containing the error message if the validation failed
 */
@Immutable
data class ValidationResult(
    val isSuccess: Boolean,
    val errorText: String? = null
)
