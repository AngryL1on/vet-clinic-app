package dev.angryl1on.history.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.angryl1on.history.R
import dev.angryl1on.vetclinic.model.medicalrecord.MedicalRecord
import dev.angryl1on.vetclinic.ui.theme.LocalDimensions
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto16
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto14
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto16
import kotlinx.datetime.LocalTime
import java.time.LocalDate

@Composable
fun VisitDetailsScreen(
    record: MedicalRecord
) {
    val dimensions = LocalDimensions.current

    val dateFormatted = runCatching {
        LocalDate.parse(record.date).let { d ->
            listOf(d.dayOfMonth, d.monthValue, d.year)
                .joinToString(".") { it.toString().padStart(2, '0') }
        }
    }.getOrNull() ?: record.date

    val timeFormatted = runCatching {
        LocalTime.parse(record.time).let { t ->
            "${t.hour.toString().padStart(2, '0')}:${t.minute.toString().padStart(2, '0')}"
        }
    }.getOrNull() ?: record.time

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensions.horizontalMedium),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            record.photoDoctor
                ?.takeIf { it.isNotBlank() }
                ?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                }

            Column {
                Text(
                    record.doctorName,
                    style = MediumRoboto16
                )
                Text(
                    stringResource(R.string.branch, record.branchShortName),
                    style = RegularRoboto14
                )
            }
        }

        HorizontalDivider()

        LabeledText(stringResource(R.string.date), dateFormatted)
        LabeledText(stringResource(R.string.time), timeFormatted)
        LabeledText(stringResource(R.string.service), record.type)

        HorizontalDivider()

        LabeledText(stringResource(R.string.diagnosis), record.diagnosis, multiline = true)
        LabeledText(stringResource(R.string.treatment), record.treatment, multiline = true)
        LabeledText(
            label = stringResource(R.string.note),
            value = record.notes.ifBlank { "—" },
            multiline = true
        )
    }
}

@Composable
private fun LabeledText(
    label: String,
    value: String,
    multiline: Boolean = false
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = RegularRoboto16, color = Color(0xFF666666))
        if (multiline) {
            Text(value, style = RegularRoboto14)
        } else {
            Text(
                value,
                style = RegularRoboto14,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
