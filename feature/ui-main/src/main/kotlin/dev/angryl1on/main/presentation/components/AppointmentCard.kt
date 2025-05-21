package dev.angryl1on.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.angryl1on.vetclinic.ui.components.buttons.OutlinePrimaryButton
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.theme.Cards
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme

@Composable
fun VetAppointmentCard(
    doctorPhoto: String? = null,
    doctorName: String,
    petName: String,
    branchName: String,
    date: String,
    time: String,
    service: String,
    onCancel: () -> Unit,
    onDetails: () -> Unit
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cards, shape = RoundedCornerShape(dimensions.defaultCornerRadius))
            .padding(dimensions.defaultPadding)
    ) {
        Row {
            if (doctorPhoto != null) {
                AsyncImage(
                    model = doctorPhoto,
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensions.avatarSize)
                        .clip(RoundedCornerShape(dimensions.defaultCornerRadius)),
                    contentScale = ContentScale.Crop
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
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "Доктор $doctorName",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Дата: $date", fontSize = 14.sp)
        Text(text = "Время: $time", fontSize = 14.sp)
        Text(text = "Имя питомца: $petName", fontSize = 14.sp)
        Text(text = "Услуга: $service", fontSize = 14.sp)

        Spacer(modifier = Modifier.height(dimensions.horizontalSmall))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinePrimaryButton(
                onButtonClick = onCancel,
                text = "Отменить"
            )
            Spacer(modifier = Modifier.width(dimensions.verticalXSmall))
//            PrimaryButton(
//                onButtonClick = onDetails,
//                text = "Подробнее"
//            )
        }
    }
}

@Composable
@Preview
fun VetAppointmentCardPreview() {
    VetClinicTheme {
        Surface {
            VetAppointmentCard(
                branchName = "Доктор Лапкин",
                date = "12.05.2025",
                doctorName = "Лушина Н. Н.",
                petName = "Барсик",
                doctorPhoto = null,
                service = "Вакцинация",
                time = "12:30",
                onCancel = {},
                onDetails = {}
            )
        }
    }
}
