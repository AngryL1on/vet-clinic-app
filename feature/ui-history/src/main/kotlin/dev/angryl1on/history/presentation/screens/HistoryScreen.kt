package dev.angryl1on.history.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import dev.angryl1on.history.R
import dev.angryl1on.history.presentation.components.HistoryCard
import dev.angryl1on.history.presentation.viewmodels.HistoryScreenState
import dev.angryl1on.history.presentation.viewmodels.HistoryScreenViewModel
import dev.angryl1on.history.presentation.viewmodels.MedicalHistoryState
import dev.angryl1on.history.presentation.viewmodels.MedicalHistoryViewModel
import dev.angryl1on.vetclinic.domain.navigation.Route
import dev.angryl1on.vetclinic.ui.components.pagers.HorizontalPagerView
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryScreenViewModel = koinViewModel()
) {
    val pets by viewModel.petsDataState.collectAsState()
    val screenState by viewModel.state.collectAsState()

    val dimensions = LocalDimensions.current

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when (screenState) {
        is HistoryScreenState.Loading -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }

        is HistoryScreenState.Error -> {
            // показать ошибку и кнопку «Повторить»
        }

        else -> {
            if (pets.isNotEmpty()) {
                HorizontalPagerView(
                    tabs = pets.map { pet ->
                        pet.name to {
                            key(pet.id) {
                                MedicalHistoryTab(
                                    petId = pet.id,
                                    navController = navController
                                )
                            }
                        }
                    },
                    selectedTabIndex = 0,
                    spaceBetweenTabAndPager = dimensions.verticalXSmall
                )
            }
        }
    }
}

@Composable
fun MedicalHistoryTab(
    petId: Long,
    navController: NavController,
    viewModel: MedicalHistoryViewModel = koinViewModel(
        key = "medical_history_$petId",
        parameters = { parametersOf(petId) }
    )
) {
    val uiState by viewModel.state.collectAsState()
    val records by viewModel.records.collectAsState()

    val dimensions = LocalDimensions.current
    val spacing = dimensions.verticalXSmall

    when (uiState) {
        MedicalHistoryState.Loading -> Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }

        is MedicalHistoryState.Error -> { /* …Error + retry… */
        }

        else -> {
            if (records.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.you_have_no_visits))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensions.horizontalMedium),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    contentPadding = PaddingValues(vertical = spacing)
                ) {
                    items(records, key = { it.id }) { record ->
                        HistoryCard(
                            branchName = record.branchShortName,
                            doctorName = record.doctorName,
                            date = record.date,
                            time = record.time,
                            service = record.type,
                            photoUrl = record.photoDoctor,
                            onDetails = {
                                navController.navigate(Route.VisitDetailsScreen.routeName)
                                navController
                                    .getBackStackEntry(Route.VisitDetailsScreen.routeName)
                                    .savedStateHandle[Route.VisitDetailsScreen.ARG_RECORD] = record
                            }
                        )
                    }
                }
            }
        }
    }
}
