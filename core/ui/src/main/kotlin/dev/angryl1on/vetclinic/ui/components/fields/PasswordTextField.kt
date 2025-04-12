package dev.angryl1on.vetclinic.ui.components.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    title: String? = null,
    placeholderText: String? = null,
    errorText: String? = null,
    maxCharCount: Int? = null,
    maxLines: Int = 1,
    minLines: Int = 1,
    singleLine: Boolean = true,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    onTextChange: (password: String) -> Unit
) {
    var passwordValue by rememberSaveable { mutableStateOf(value) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    PrimaryTextField(
        modifier = modifier,
        value = passwordValue,
        onTextChange = {
            if (it.length <= (maxCharCount ?: (it.length + 1))) {
                passwordValue = it
                onTextChange(it)
            }
        },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        title = title,
        placeholder = placeholderText,
        isEnabled = isEnabled,
        isError = isError,
        trailingIcon = if (isPasswordVisible) {
            R.drawable.icon_visibility_on
        } else {
            R.drawable.icon_visibility_off
        },
        onTrailingIconClicked = {
            isPasswordVisible = !isPasswordVisible
        },
        supportText = errorText,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
@Preview
fun PasswordTextFieldPreview() {
    VetClinicTheme {
        Surface {
            Column(
                modifier = Modifier
                    .background(color = White)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = "Введите пароль",
                    isEnabled = true,
                    isError = false,
                    onTextChange = { /* Do nothing */ }
                )

                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = "Введите пароль",
                    errorText = "Максимум символов",
                    maxCharCount = 20,
                    isEnabled = true,
                    isError = true,
                    onTextChange = { /* Do nothing */ }
                )

                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    placeholderText = "Введите пароль",
                    errorText = "Максимум символов",
                    isEnabled = false,
                    isError = false,
                    onTextChange = { /* Do nothing */ }
                )
            }
        }
    }
}
