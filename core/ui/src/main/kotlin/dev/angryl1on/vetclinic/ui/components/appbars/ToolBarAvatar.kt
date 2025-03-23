package dev.angryl1on.vetclinic.ui.components.appbars

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.min
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import dev.angryl1on.vetclinic.ui.extension.toSp
import dev.angryl1on.vetclinic.ui.theme.ActiveButton
import dev.angryl1on.vetclinic.ui.theme.NavActive
import dev.angryl1on.vetclinic.ui.theme.RegularWorkSans24
import dev.angryl1on.vetclinic.ui.theme.UnactiveButtonAndProgress
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme
import dev.angryl1on.vetclinic.ui.theme.White

@Composable
fun ToolBarAvatar(
    avatarURL: String?,
    firstName: String?,
    lastName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    textStyle: TextStyle = RegularWorkSans24.copy(color = White)
) {
    val initialsName =
        (firstName?.firstOrNull()?.uppercase() ?: " ") +
                (lastName?.firstOrNull()?.uppercase() ?: " ")
    val textMeasurer = rememberTextMeasurer()

    FilledIconButton(
        modifier = modifier.aspectRatio(1f),
        shape = shape,
        onClick = onClick
    ) {
        BoxWithConstraints {
            val minSize = min(maxWidth, maxHeight)
            val textLayoutResult = textMeasurer.measure(
                initialsName,
                style = textStyle.copy(
                    fontSize = (minSize / initialsName.length).toSp()
                ),
                constraints = constraints
            )

            SubcomposeAsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = avatarURL,
                contentDescription = null
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }

                    else -> {
                        Canvas(
                            modifier = Modifier.fillMaxSize(),
                            onDraw = {
                                drawRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(ActiveButton, NavActive, UnactiveButtonAndProgress),
                                        start = Offset(0f, 0f),
                                        end = Offset(1000f, 1000f)
                                    )
                                )
                                drawText(
                                    textLayoutResult = textLayoutResult,
                                    topLeft = Offset(
                                        (size.width - textLayoutResult.size.width) / 2f,
                                        (size.height - textLayoutResult.size.height) / 2f
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ToolBarAvatarPreview() {
    VetClinicTheme {
        ToolBarAvatar(
            modifier = Modifier,
            onClick = {
                // Do nothing
            },
            avatarURL = null,
            firstName = "Vadim",
            lastName = "Lushin"
        )
    }
}
