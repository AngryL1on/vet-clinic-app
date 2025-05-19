package dev.angryl1on.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.angryl1on.history.R
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.Cards
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme

@Composable
fun HistoryCard(
    branchName: String,
    doctorName: String?,
    date: String,
    time: String,
    service: String,
    photoUrl: String? = null,
    onDetails: () -> Unit
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Cards,
                shape = RoundedCornerShape(dimensions.defaultCornerRadius)
            )
            .padding(dimensions.defaultPadding)
    ) {
        Row {
            photoUrl?.let { url ->
                AsyncImage(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape),
                    model = url,
                    contentDescription = stringResource(R.string.photo_doctor)
                )

                Spacer(modifier = Modifier.width(dimensions.horizontalSmall))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall)
            ) {
                Text(
                    text = "Филиал \"$branchName\"",
                    fontSize = 16.sp,
                    fontWeight = W600,
                    color = Black
                )
                Text(
                    text = "Доктор $doctorName",
                    fontSize = 14.sp,
                    fontWeight = W500,
                    color = Black
                )
            }
        }

        Text(text = "Дата: $date", fontSize = 14.sp)
        Text(text = "Время: $time", fontSize = 14.sp)
        Text(text = "Услуга: $service", fontSize = 14.sp)
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall)
        ) {
            PrimaryButton(
                onButtonClick = onDetails,
                text = stringResource(R.string.detailed)
            )
        }
    }
}

@Composable
@Preview
fun HistoryCardPreview() {
    VetClinicTheme {
        Surface {
            HistoryCard(
                branchName = "Доктор Лапкин",
                date = "12.05.2025",
                doctorName = "Лушина Н. Н.",
                service = "Вакцинация",
                time = "12:30",
                onDetails = {}
            )
        }
    }
}
