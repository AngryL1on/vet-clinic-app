package dev.angryl1on.vetclinic.ui.components.pagers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.PrimaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import dev.angryl1on.vetclinic.ui.theme.ActiveButton
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto14
import dev.angryl1on.vetclinic.ui.theme.Picker
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun HorizontalPagerView(
    tabs: List<Pair<String, @Composable () -> Unit>>,
    modifier: Modifier = Modifier,
    spaceBetweenTabAndPager: Dp = 0.dp,
    selectedTabIndex: Int = 0,
    maxLinesInTitle: Int = 1,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        initialPageOffsetFraction = 0f
    ) { tabs.size }

    val dimensions = LocalDimensions.current
    val textMeasurer = rememberTextMeasurer()
    val textSizes = remember { mutableStateListOf<Size>() }

    // Когда табов мало — показываем фиксированную строку с растяжением, иначе — скролл
    val useFixed = tabs.size <= 3

    Column(modifier = modifier.fillMaxWidth()) {
        // Индикатор общий для TabRow и ScrollableTabRow
        val indicator: @Composable (List<TabPosition>) -> Unit = { tabPositions ->
            val idx = pagerState.currentPage
            val measuredSize = textSizes.getOrNull(idx) ?: Size(50f, 0f)
            val widthDp = with(LocalDensity.current) { measuredSize.width.toDp() }
            PrimaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[idx])
                    .padding(vertical = dimensions.verticalXSmall),
                width = widthDp,
                color = ActiveButton
            )
        }

        if (useFixed) {
            // фиксированная строка: табы равномерно растягиваются
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                divider = {},
                indicator = indicator
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        modifier = Modifier
                            .weight(1f)
                            .background(White),
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        selectedContentColor = Picker,
                        text = {
                            Text(
                                text = tab.first,
                                maxLines = maxLinesInTitle,
                                style = MediumRoboto14,
                                color = Picker,
                                modifier = Modifier.onGloballyPositioned {
                                    // конвертация IntSize -> Size
                                    val measuredPx = textMeasurer
                                        .measure(tab.first)
                                        .size
                                        .toSize()
                                    if (textSizes.size > index) {
                                        textSizes[index] = measuredPx
                                    } else {
                                        textSizes.add(measuredPx)
                                    }
                                }
                            )
                        }
                    )
                }
            }
        } else {
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
                divider = {},
                indicator = indicator
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        modifier = Modifier.background(White),
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        selectedContentColor = Picker,
                        text = {
                            Text(
                                text = tab.first,
                                maxLines = maxLinesInTitle,
                                style = MediumRoboto14,
                                color = Picker,
                                modifier = Modifier.onGloballyPositioned {
                                    val measuredPx = textMeasurer
                                        .measure(tab.first)
                                        .size
                                        .toSize()
                                    if (textSizes.size > index) {
                                        textSizes[index] = measuredPx
                                    } else {
                                        textSizes.add(measuredPx)
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(spaceBetweenTabAndPager))

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Column(modifier = Modifier.fillMaxSize()) {
                tabs[page].second()
            }
        }
    }
}

@Preview
@Composable
fun HorizontalPagerViewPreview() {
    VetClinicTheme {
        Surface {
            HorizontalPagerView(
                tabs = listOf(
                    "First element" to {
                        Button(onClick = {}) {
                            Text(text = "First Composable")
                        }
                    },
                    "Second element" to {
                        Text("Second Composable")
                    },
                    "Third element" to {
                        Text("Third Composable")
                    }
                ),
                selectedTabIndex = 0,
                spaceBetweenTabAndPager = 8.dp
            )
        }
    }
}
