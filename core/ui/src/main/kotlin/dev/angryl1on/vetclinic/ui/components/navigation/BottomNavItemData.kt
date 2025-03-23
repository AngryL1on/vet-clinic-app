package dev.angryl1on.vetclinic.ui.components.navigation

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

/**
 * Представляет собой нижний элемент навигации со значком и заголовком.
 *
 * @property [icon] Идентификатор ресурса значка, представляющего иконку для элемента навигации.
 * @property [title] Идентификатор строкового ресурса, представляющий заголовок или описание для элемента навигации.
 */
@Stable
@SuppressLint("SupportAnnotationUsage")
data class BottomNavItemData(
    @DrawableRes val icon : Int,
    @StringRes val title: String,
)
