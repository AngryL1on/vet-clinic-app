package dev.angryl1on.main.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton

@Composable
fun MainScreen(
    onAppointmentClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(
            text = " Записаться на прием",
            onButtonClick = onAppointmentClick
        )
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen(
        onAppointmentClick = { /* do nothing */ }
    )
}
