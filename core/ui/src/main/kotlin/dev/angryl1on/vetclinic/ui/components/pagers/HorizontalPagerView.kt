package dev.angryl1on.vetclinic.ui.components.pagers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
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
    ) {
        tabs.size
    }
    val dimensions = LocalDimensions.current
    val textMeasurer = rememberTextMeasurer()
    val textSizes = remember { mutableStateListOf<Size>() }


    Column(modifier = modifier) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                val textSize = textSizes.getOrNull(pagerState.currentPage) ?: Size(50f, 0f)
                val width = with(LocalDensity.current) { textSize.width.toDp() }
                PrimaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .padding(vertical = dimensions.verticalXSmall),
                    width = width,
                    color = ActiveButton
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.background(color = Color.Unspecified),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    selectedContentColor = Picker,
                    text = {
                        Text(
                            text = tab.first,
                            maxLines = maxLinesInTitle,
                            style = MediumRoboto14,
                            modifier = Modifier.onGloballyPositioned {
                                val measuredSize = textMeasurer.measure(tab.first).size
                                if (textSizes.size > index) {
                                    textSizes[index] = measuredSize.toSize()
                                } else {
                                    textSizes.add(measuredSize.toSize())
                                }
                            },
                            color = Picker
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(spaceBetweenTabAndPager))

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize()
            ) { tabs[page].second() }
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
