package dev.angryl1on.vetclinic.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import dev.angryl1on.vetclinic.auth.R
import dev.angryl1on.vetclinic.ui.components.buttons.OutlinePrimaryButton
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.theme.Inputs
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions

@Composable
fun StartScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Inputs),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = dev.angryl1on.vetclinic.ui.R.drawable.ic_logo,
            contentDescription = null,
            modifier = Modifier.size(dimensions.logoSize)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensions.horizontalMedium),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryButton(
                onButtonClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.login)
            )

            Spacer(modifier = Modifier.height(dimensions.verticalXSmall))

            OutlinePrimaryButton(
                onButtonClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.sign_up)
            )
        }

    }
}

@Composable
@Preview
fun StartScreenPreview() {
    StartScreen(
        onLoginClick = {},
        onRegisterClick = {}
    )
}
