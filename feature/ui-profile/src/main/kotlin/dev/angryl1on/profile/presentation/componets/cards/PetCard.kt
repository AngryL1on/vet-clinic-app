package dev.angryl1on.profile.presentation.componets.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import dev.angryl1on.profile.R
import dev.angryl1on.vetclinic.model.pet.PetResponse
import dev.angryl1on.vetclinic.ui.components.buttons.OutlinePrimaryButton
import dev.angryl1on.vetclinic.ui.components.buttons.PrimaryButton
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.Cards
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto16
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto14
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto16
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme

@Composable
fun PetCard(
    petData: PetResponse,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dimensions = LocalDimensions.current
    val formattedBirthDate = androidx.compose.runtime.remember(petData.birthDate) {
        try {
            val parser = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            val formatter = java.text.SimpleDateFormat.getDateInstance(
                java.text.DateFormat.SHORT,
                java.util.Locale.getDefault()
            )
            val date = parser.parse(petData.birthDate)
            formatter.format(date ?: petData.birthDate)
        } catch (e: Exception) {
            petData.birthDate
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Cards,
            contentColor = Black
        ),
        shape = RoundedCornerShape(dimensions.defaultCornerRadius),

        ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = dimensions.verticalMedium,
                    horizontal = dimensions.verticalMedium
                ),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall)
        ) {
            Text(
                text = petData.name,
                style = MediumRoboto16
            )

            Text(
                text = petData.breed,
                style = RegularRoboto14
            )

            petData.photoUrl?.let { url ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(dimensions.defaultCornerRadius)),
                    model = url,
                    contentDescription = stringResource(R.string.profile_photo)
                )
            }

            Text(
                text = "Дата рождения: $formattedBirthDate",
                style = RegularRoboto16
            )

            Spacer(modifier = Modifier.height(dimensions.verticalLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinePrimaryButton(
                    text = stringResource(R.string.edit),
                    onButtonClick = onEditClick
                )

                Spacer(modifier = Modifier.width(dimensions.horizontalXSmall))

                PrimaryButton(
                    text = stringResource(R.string.delete),
                    onButtonClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
@Preview
fun PetCardPreview() {
    VetClinicTheme {
        Surface {
            PetCard(
                petData = PetResponse(
                    id = 1,
                    name = "Арнольд",
                    animalType = "Собака",
                    birthDate = "2020-01-01",
                    breed = "Лабрадор",
                    photoUrl = "https://storage.yandexcloud.net/yac-wh-sb-prod-s3-media-03005/uploads/breed/689/d20ef42c7ee0f73d9bf43fb5c3538e9f.webp"
                ),
                onDeleteClick = { /* do nothing */ },
                onEditClick = { /* do nothing */ }
            )
        }
    }
}
