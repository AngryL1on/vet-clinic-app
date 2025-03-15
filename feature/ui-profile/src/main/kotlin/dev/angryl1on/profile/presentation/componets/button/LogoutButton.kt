package dev.angryl1on.profile.presentation.componets.button

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto14
import dev.angryl1on.vetclinic.ui.theme.Red
import dev.angryl1on.vetclinic.ui.theme.UnactiveButtonAndProgress
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White

@Composable
fun LogoutButton(
    text: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val dimensions = LocalDimensions.current

    Button(
        modifier = modifier,
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Red,
            contentColor = White,
            disabledContainerColor = UnactiveButtonAndProgress,
            disabledContentColor = White
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
            Text(
                text = text,
                style = MediumRoboto14,
                color = localContentColorButton
            )
        }
    }
}

@Composable
@Preview
fun LogoutButtonPreview() {
    VetClinicTheme {
        Surface {
            LogoutButton(
                modifier = Modifier
                    .width(140.dp)
                    .height(40.dp),
                text = "Logout",
                isLoading = false,
                onButtonClick = {},
            )
        }
    }
}
