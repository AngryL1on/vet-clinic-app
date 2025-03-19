package dev.angryl1on.vetclinic.ui.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto12
import dev.angryl1on.vetclinic.ui.theme.NavActive
import dev.angryl1on.vetclinic.ui.theme.NavFill
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomNavBar(
    navController: NavController,
    navItems: List<BottomNavItemData>,
    scope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current
    val iconSize = Modifier.size(dimensions.iconDefaultSize)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = NavFill,
        contentColor = NavActive
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensions.verticalXSmall),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = currentRoute == item.screen

                NavigationBarItem(
                    selected = isSelected,
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(item.icon),
                            contentDescription = item.title,
                            modifier = iconSize
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MediumRoboto12
                        )
                    },
                    onClick = {
                        scope.launch {
                            if (!isSelected) {
                                navController.navigate(item.screen) {
                                    // Позволяет избежать дублирования экранов,
                                    // когда мы снова нажимаем ту же вкладку
                                    launchSingleTop = true
                                    restoreState = true

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
