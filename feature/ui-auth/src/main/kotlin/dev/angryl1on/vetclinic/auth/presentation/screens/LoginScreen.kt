package dev.angryl1on.vetclinic.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import dev.angryl1on.vetclinic.auth.R
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.components.fields.PasswordTextField
import dev.angryl1on.vetclinic.ui.components.fields.PrimaryTextField
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.RegularWorkSans24
import dev.angryl1on.vetclinic.ui.theme.White

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit
) {
    val dimensions = LocalDimensions.current

    var email by remember { mutableStateOf("") }
    val password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall)
    ) {
        AsyncImage(
            model = dev.angryl1on.vetclinic.ui.R.drawable.login_image,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.padding(horizontal = dimensions.horizontalMedium),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall)
        ) {
            Text(
                text = stringResource(R.string.welcome),
                style = RegularWorkSans24.copy(color = Black)
            )

            Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                placeholder = stringResource(R.string.enter_email),
                isEnabled = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                isMaxQuantityOfCharVisible = false,
                onTextChange = {
                    email = it
                }
            )

            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                placeholderText = stringResource(R.string.enter_password),
                isEnabled = true,
                isError = false,
                onTextChange = { /* Do nothing */ }
            )

            Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onButtonClick = { onLoginClick(email, password) },
                text = stringResource(R.string.login)
            )
        }
    }
}

@Composable
@Preview
fun LoginScreeenPreview() {
    LoginScreen(
        onLoginClick = { _, _ ->
        }
    )
}
