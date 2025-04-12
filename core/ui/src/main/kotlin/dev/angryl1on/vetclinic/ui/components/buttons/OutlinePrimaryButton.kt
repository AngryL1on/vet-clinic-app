package dev.angryl1on.vetclinic.ui.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.theme.ActiveButton
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto14
import dev.angryl1on.vetclinic.ui.theme.UnactiveButtonAndProgress
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme

@Composable
fun OutlinePrimaryButton(
    text: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes leftIcon: Int? = null,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val dimensions = LocalDimensions.current
    val spacerModifier = Modifier.width(dimensions.verticalXSmall)

    OutlinedButton(
        modifier = modifier,
        onClick = onButtonClick,
        enabled = isEnabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Unspecified,
            contentColor = ActiveButton,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = UnactiveButtonAndProgress
        ),
        border = BorderStroke(
            color = if (isEnabled) ActiveButton else UnactiveButtonAndProgress,
            width = 1.dp
        )
    ) {
        val localContentColorButton = LocalContentColor.current

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .align(Alignment.CenterVertically),
                color = localContentColorButton,
                strokeCap = StrokeCap.Round,
                strokeWidth = dimensions.circularStrokeWith
            )
        } else {
            leftIcon?.let { icon ->
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = null
                )
                Spacer(modifier = spacerModifier)
            }
            Text(
                text = text,
                style = MediumRoboto14
            )
        }
    }
}

@Composable
@Preview
fun OutlinePrimaryButtonPreview() {
    VetClinicTheme {
        val settingsSizeButton = Modifier
            .width(140.dp)
            .height(40.dp)
        val settingsSizeButtonLarge = Modifier
            .width(280.dp)
            .height(80.dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Row for the normal button state
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinePrimaryButton(
                    modifier = settingsSizeButton,
                    leftIcon = R.drawable.ic_upload_data,
                    text = "Button",
                    isEnabled = true,
                    isLoading = false,
                    onButtonClick = {
                        // Do nothing
                    }
                )

                OutlinePrimaryButton(
                    modifier = settingsSizeButtonLarge,
                    leftIcon = R.drawable.ic_upload_data,
                    text = "Button",
                    isEnabled = true,
                    isLoading = false,
                    onButtonClick = {
                        // Do nothing
                    }
                )
            }

            // Row for the loading button state
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinePrimaryButton(
                    modifier = settingsSizeButton,
                    leftIcon = R.drawable.ic_upload_data,
                    text = "Button",
                    isEnabled = true,
                    isLoading = true,
                    onButtonClick = {
                        // Do nothing
                    }
                )

                OutlinePrimaryButton(
                    modifier = settingsSizeButtonLarge,
                    leftIcon = R.drawable.ic_upload_data,
                    text = "Button",
                    isEnabled = true,
                    isLoading = true,
                    onButtonClick = {
                        // Do nothing
                    }
                )
            }

            // Row for the disabled button state
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinePrimaryButton(
                    modifier = settingsSizeButton,
                    leftIcon = R.drawable.ic_upload_data,
                    text = "Button",
                    isEnabled = false,
                    isLoading = false,
                    onButtonClick = {
                        // Do nothing
                    }
                )

                OutlinePrimaryButton(
                    modifier = settingsSizeButtonLarge,
                    leftIcon = R.drawable.ic_upload_data,
                    text = "Button",
                    isEnabled = false,
                    isLoading = false,
                    onButtonClick = {
                        // Do nothing
                    }
                )
            }
        }
    }
}
