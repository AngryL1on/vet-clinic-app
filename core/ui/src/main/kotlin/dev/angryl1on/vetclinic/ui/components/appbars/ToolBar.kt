package dev.angryl1on.vetclinic.ui.components.appbars

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumWorkSans16
import dev.angryl1on.vetclinic.ui.theme.MediumWorkSans20
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SupportAnnotationUsage")
fun ToolBar(
    @DrawableRes iconRight: Int,
    modifier: Modifier = Modifier,
    lastName: String? = null,
    firstName: String? = null,
    imageAvatar: String? = null,
    @StringRes screenName: String? = null,
    @DrawableRes iconLeft: Int? = null,
    isMainScreen: Boolean = true,
    onLeftIconClick: () -> Unit,
    onRightIconClick: () -> Unit,
) {
    val dimensions = LocalDimensions.current

    if (isMainScreen) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            windowInsets = WindowInsets(
                left = dimensions.horizontalMedium,
                right = dimensions.horizontalMedium,
                top = dimensions.horizontalXSmall,
                bottom = dimensions.horizontalXSmall
            ),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = White,
                navigationIconContentColor = Black,
                actionIconContentColor = Black
            ),
            title = { },
            navigationIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalSmall)
                ) {
                    ToolBarAvatar(
                        modifier = Modifier.fillMaxHeight(),
                        avatarURL = imageAvatar,
                        firstName = firstName,
                        lastName = lastName,
                        onClick = onLeftIconClick
                    )

                    Text(
                        text = stringResource(R.string.with_return),
                        style = MediumWorkSans16
                    )
                }
            },
            actions = {
                ToolBarButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(dimensions.iconButtonDefaultSize)
                        .aspectRatio(1f),
                    icon = iconRight,
                    onClick = onRightIconClick
                )
            }
        )
    } else (
            CenterAlignedTopAppBar(
                modifier = modifier,
                windowInsets = WindowInsets(
                    left = dimensions.horizontalMedium,
                    right = dimensions.horizontalMedium,
                    top = dimensions.horizontalXSmall,
                    bottom = dimensions.horizontalXSmall
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White,
                    navigationIconContentColor = Black,
                    actionIconContentColor = Black
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = screenName.toString(),
                            style = MediumWorkSans20.copy(color = Black)
                        )
                    }
                },
                navigationIcon = {
                    iconLeft?.let {
                        ToolBarButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .size(dimensions.iconButtonDefaultSize)
                                .aspectRatio(1f),
                            icon = it,
                            onClick = onLeftIconClick
                        )
                    }
                },
                actions = {
                    ToolBarButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .size(dimensions.iconButtonDefaultSize)
                            .aspectRatio(1f),
                        icon = iconRight,
                        onClick = onRightIconClick
                    )
                }
            )
    )
}

@Composable
@Preview(showBackground = true)
fun ToolBarPreview() {
    VetClinicTheme {
        Column {
            ToolBar(
                modifier = Modifier.height(56.dp),
                onLeftIconClick = {
                    // Do nothing
                },
                onRightIconClick = {
                    // Do nothing
                },
                imageAvatar = null,
                firstName = "Vadim",
                lastName = "Lushin",
                iconRight = R.drawable.ic_notifications
            )

            Spacer(modifier = Modifier.height(10.dp))

            ToolBar(
                modifier = Modifier.height(56.dp),
                isMainScreen = false,
                onLeftIconClick = {
                    // Do nothing
                },
                onRightIconClick = {
                    // Do nothing
                },
                screenName = stringResource(R.string.my_pets),
                iconLeft = R.drawable.ic_arrow_left,
                iconRight = R.drawable.ic_notifications
            )
        }
    }
}