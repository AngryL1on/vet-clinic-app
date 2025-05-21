package dev.angryl1on.vetclinic.ui.components.fields

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import dev.angryl1on.vetclinic.ui.theme.Blue
import dev.angryl1on.vetclinic.ui.theme.Inputs
import dev.angryl1on.vetclinic.ui.theme.InputsText
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto12
import dev.angryl1on.vetclinic.ui.theme.MediumWorkSans16
import dev.angryl1on.vetclinic.ui.theme.Red
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto16
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White

@Composable
fun PrimaryDropdownField(
    modifier: Modifier = Modifier,
    title: String? = null,
    selectedOption: String,
    options: List<String>,
    placeholder: String? = null,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    errorText: String? = null,
    supportText: String? = null,
    @DrawableRes trailingIcon: Int? = null,
    trailingIconModifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    val dimensions = LocalDimensions.current
    var expanded by remember { mutableStateOf(false) }
    var inputFieldSize by remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall)
    ) {
        title?.let {
            Text(
                text = title,
                style = MediumWorkSans16.copy(color = InputsText)
            )
        }

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        inputFieldSize = coordinates.size
                    }
                    .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
                    .background(Inputs)
                    .clickable(enabled = isEnabled) { expanded = !expanded }
                    .padding(
                        vertical = dimensions.verticalMedium,
                        horizontal = dimensions.horizontalMedium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption.ifEmpty { (placeholder ?: "") },
                    style = RegularRoboto16.copy(
                        color = if (selectedOption.isNotEmpty()) InputsText else InputsText.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.weight(1f)
                )

                trailingIcon?.let {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = trailingIcon),
                        contentDescription = null,
                        modifier = trailingIconModifier.size(dimensions.iconDefaultSize),
                        tint = InputsText
                    )
                } ?: Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = InputsText
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { inputFieldSize.width.toDp() }) // устанавливаем ширину
                    .background(Inputs)
            ) {
                options.forEach { option ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val backgroundColor = if (isPressed) Blue else Color.Transparent

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                expanded = false
                                onOptionSelected(option)
                            }
                            .padding(
                                horizontal = dimensions.horizontalMedium,
                                vertical = dimensions.verticalSmall
                            )
                    ) {
                        Text(
                            text = option,
                            style = RegularRoboto16.copy(color = InputsText)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.verticalXXSmall),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isError && errorText != null) {
                Text(
                    text = errorText,
                    style = MediumRoboto12,
                    color = Red,
                    textAlign = TextAlign.Start
                )
            } else {
                supportText?.let {
                    Text(
                        text = it,
                        style = MediumRoboto12,
                        color = InputsText,
                        textAlign = TextAlign.Left
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PrimaryDropdownFieldPreview() {
    var selectedItem by remember { mutableStateOf("") }

    val dimensions = LocalDimensions.current

    VetClinicTheme {
        Surface(
            color = White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensions.horizontalMedium),
                verticalArrangement = Arrangement.Center
            ) {
                PrimaryDropdownField(
                    title = "Питомец",
                    selectedOption = selectedItem,
                    options = listOf("Барсик", "Рекс", "Персик"),
                    placeholder = "Выберите...",
                    onOptionSelected = { selectedItem = it }
                )
            }
        }
    }
}
