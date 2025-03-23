package dev.angryl1on.vetclinic.ui.components.appbars

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.Dimensions
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme

@Composable
fun ToolBarButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    val dimensions = LocalDimensions.current
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(dimensions.iconDefaultSize),
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = null,
            tint = Black
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ToolBarButtonPreview() {
    VetClinicTheme {
        ToolBarButton(
            onClick = {
                // Do nothing
            },
            icon = R.drawable.ic_notifications
        )
    }
}

