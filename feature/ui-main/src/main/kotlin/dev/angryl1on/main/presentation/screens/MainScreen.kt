package dev.angryl1on.main.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.angryl1on.main.presentation.components.VetAppointmentCard
import dev.angryl1on.main.presentation.viewmodel.MainScreenState
import dev.angryl1on.main.presentation.viewmodel.MainViewModel
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto16
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    onAppointmentClick: () -> Unit
) {
    val screenState by viewModel.state.collectAsState(MainScreenState.Init)
    val appointments by viewModel.appointments.collectAsState()

    val dimensions = LocalDimensions.current

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when (screenState) {
        is MainScreenState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is MainScreenState.Error -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.defaultPadding),
                text = "Ошибка загрузки данных",
                color = Color.Red
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = dimensions.defaultPadding),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall)
            ) {
                if (appointments.isEmpty()) {
                    item {
                        Text(
                            text = "У вас нет запланированных приемов",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = dimensions.verticalMedium),
                            style = MediumRoboto16,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(appointments) { appointment ->
                        VetAppointmentCard(
                            doctorPhoto = appointment.doctorPhoto,
                            doctorName = appointment.doctorName,
                            petName = appointment.petName,
                            branchName = appointment.branchName,
                            date = appointment.appointmentDate,
                            time = appointment.appointmentStartTime.dropLast(3),
                            service = appointment.appointmentType,
                            onCancel = { viewModel.cancelAppointment(appointment.id) },
                            onDetails = { /* TODO */ }
                        )
                    }
                }

                item {
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
            }
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen(
        onAppointmentClick = { /* do nothing */ }
    )
}
