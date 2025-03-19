package dev.angryl1on.vetclinic.ui.components.fields

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.theme.Inputs
import dev.angryl1on.vetclinic.ui.theme.InputsText
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto12
import dev.angryl1on.vetclinic.ui.theme.MediumWorkSans16
import dev.angryl1on.vetclinic.ui.theme.Red
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto16
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White
import dev.angryl1on.vetclinic.ui.utils.PhoneVisualTransformation

@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String = "",
    prefix: String? = null,
    placeholder: String? = null,
    supportText: String? = null,
    errorText: String? = null,
    maxQuantityOfChar: Int? = null,
    isMaxQuantityOfCharVisible: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    isOnlyNumbers: Boolean = false,
    onTextChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    @DrawableRes trailingIcon: Int? = null,
    trailingIconModifier: Modifier = Modifier,
    onTrailingIconClicked: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val fillMaxWidthModifier = Modifier.fillMaxWidth()
    val dimensions = LocalDimensions.current
    val finalKeyboardOptions = if (isOnlyNumbers) {
        keyboardOptions.copy(keyboardType = KeyboardType.Number)
    } else {
        keyboardOptions
    }

    var textValue by rememberSaveable { mutableStateOf(value) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall)
    ) {
        title?.let {
            Text(
                text = title,
                style = MediumWorkSans16.copy(
                    color = InputsText
                )
            )
        }
        BasicTextField(
            modifier = fillMaxWidthModifier,
            value = textValue,
            onValueChange = { text ->
                val maxChars = maxQuantityOfChar ?: Int.MAX_VALUE
                if (text.length <= maxChars) {
                    textValue = text
                    onTextChange(text)
                }
            },
            visualTransformation = visualTransformation,
            textStyle = RegularRoboto16.copy(
                color = InputsText
            ),
            readOnly = readOnly,
            enabled = isEnabled,
            maxLines = maxLines,
            minLines = minLines,
            singleLine = singleLine,
            keyboardOptions = finalKeyboardOptions,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(dimensions.defaultCornerRadius))
                        .background(Inputs)
                        .padding(
                            vertical = dimensions.verticalMedium,
                            horizontal = dimensions.horizontalMedium
                        )
                        .requiredHeightIn(min = dimensions.verticalMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    prefix?.let { text ->
                        Text(
                            text = "$text ",
                            style = RegularRoboto16.copy(
                                color = InputsText,
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(5f)
                            .padding(end = dimensions.horizontalXSmall)
                    ) {
                        if (textValue.isEmpty()) {
                            placeholder?.let { placeholderText ->
                                Text(
                                    text = placeholderText,
                                    style = RegularRoboto16.copy(
                                        color = InputsText
                                    )
                                )
                            }
                        }
                        innerTextField()
                    }
                    trailingIcon?.let { trailingIcon ->
                        IconButton(
                            modifier = trailingIconModifier.size(dimensions.iconDefaultSize),
                            onClick = onTrailingIconClicked
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = trailingIcon),
                                contentDescription = null,
                                tint = InputsText,
                            )
                        }
                    }
                }
            }
        )
        Row(
            modifier = fillMaxWidthModifier.padding(top = dimensions.verticalXXSmall)
        ) {
            if (isError) {
                errorText?.let { text ->
                    Text(
                        modifier = fillMaxWidthModifier,
                        text = text,
                        style = MediumRoboto12,
                        color = Red,
                        textAlign = TextAlign.Start
                    )
                }
            } else {
                supportText?.let { text ->
                    Text(
                        text = text,
                        style = MediumRoboto12,
                        color = InputsText,
                        textAlign = TextAlign.Left
                    )
                }
            }
            maxQuantityOfChar?.let {
                if (isMaxQuantityOfCharVisible) {
                    Text(
                        modifier = fillMaxWidthModifier,
                        text = stringResource(
                            id = R.string.limit_of_max_char,
                            textValue.length,
                            maxQuantityOfChar
                        ),
                        style = MediumRoboto12,
                        color = InputsText,
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PrimaryTextFieldPreview() {
    VetClinicTheme {
        Surface {
            Column(
                modifier = Modifier
                    .background(color = White)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PrimaryTextField(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Имя пользователя",
                    placeholder = "Введите имя",
                    maxQuantityOfChar = 20,
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = {
                        // Do nothing
                    }
                )

                PrimaryTextField(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Номер телефона",
                    placeholder = "Введите свой телефон",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PhoneVisualTransformation,
                    maxQuantityOfChar = 11,
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = {
                        // Do nothing
                    }
                )

                PrimaryTextField(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Электронная почта",
                    placeholder = "Введите электронную почту",
                    isEnabled = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = {
                        // Do nothing
                    }
                )

                PrimaryTextField(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Комментарий",
                    placeholder = "Не более 500 символов",
                    supportText = "Только латиница",
                    minLines = 4,
                    maxLines = 4,
                    maxQuantityOfChar = 500,
                    isEnabled = true,
                    onTextChange = {
                        // Do nothing
                    }
                )

                PrimaryTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Выберете страну",
                    trailingIcon = R.drawable.ic_arrow_drop_down,
                    onTextChange = {
                        // Do nothing
                    }
                )

                PrimaryTextField(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Имя пользователя",
                    placeholder = "Введите имя",
                    maxQuantityOfChar = 20,
                    isMaxQuantityOfCharVisible = true,
                    isError = true,
                    errorText = "Данный пользователь не найден",
                    onTextChange = {
                        // Do nothing
                    }
                )
            }
        }
    }
}
