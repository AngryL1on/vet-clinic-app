package dev.angryl1on.profile.presentation.componets.buttons.section

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import dev.angryl1on.vetclinic.ui.R
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto16
import dev.angryl1on.vetclinic.ui.theme.UnactiveButtonAndProgress
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme

@Composable
fun ProfileSectionButton(
    modifier: Modifier = Modifier,
    iconSection: Int,
    textSection: String,
    onSectionClick: () -> Unit
) {
    val dimensions = LocalDimensions.current

    Button(
        modifier = modifier,
        contentPadding = PaddingValues(
            vertical = dimensions.verticalMedium,
            horizontal = dimensions.horizontalMedium
        ),
        shape = RoundedCornerShape(dimensions.defaultCornerRadius),
        colors = ButtonDefaults.textButtonColors(
            containerColor = UnactiveButtonAndProgress,
            contentColor = Black
        ),
        content = {
            Icon(
                painter = painterResource(id = iconSection),
                modifier = Modifier.size(dimensions.iconButtonDefaultSize),
                contentDescription = null
            )

            Spacer(
                modifier.width(dimensions.horizontalMedium)
            )

            Text(
                text = textSection,
                style = MediumRoboto16
            )

            Spacer(
                modifier.fillMaxWidth().weight(2.5f)
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                modifier = Modifier.size(dimensions.iconDefaultSize).weight(1f),
                contentDescription = null
            )

        },
        onClick = onSectionClick
    )
}

@Composable
@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun ProfileSectionButtonPreview() {
    VetClinicTheme {
        ProfileSectionButton(
            iconSection = R.drawable.ic_notifications,
            textSection = "Уведомления",
            onSectionClick = { /* Do nothing */ }
        )
    }
}
