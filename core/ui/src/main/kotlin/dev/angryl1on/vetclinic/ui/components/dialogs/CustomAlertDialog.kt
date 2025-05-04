package dev.angryl1on.vetclinic.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.angryl1on.vetclinic.ui.theme.ActiveButton
import dev.angryl1on.vetclinic.ui.theme.Black
import dev.angryl1on.vetclinic.ui.theme.MediumRoboto14
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto14
import dev.angryl1on.vetclinic.ui.theme.RegularRoboto24
import dev.angryl1on.vetclinic.ui.theme.UnactiveButtonAndProgress
import dev.angryl1on.vetclinic.ui.theme.VetClinicTheme

@Composable
fun CustomAlertDialog(
    titleText: String,
    bodyText: String,
    confirmText: String,
    dismissText: String?,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        containerColor = UnactiveButtonAndProgress,
        titleContentColor = Black,
        textContentColor = Black,
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick
            ) {
                Text(
                    text = confirmText,
                    color = ActiveButton,
                    style = MediumRoboto14
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissButtonClick) {
                dismissText?.let { dismissText ->
                    Text(
                        text = dismissText,
                        color = ActiveButton,
                        style = MediumRoboto14
                    )
                }
            }
        },
        title = {
            Text(
                text = titleText,
                style = RegularRoboto24
            )
        },
        text = {
            Text(
                text = bodyText,
                style = RegularRoboto14
            )
        }
    )
}

@Composable
@Preview
fun CustomAlertDialogPreview() {
    VetClinicTheme {
        Surface {
            CustomAlertDialog(
                titleText = "Вы уверены в своем действии?",
                bodyText = "Подтвердите ваше действие",
                confirmText = "Да, подтверждаю",
                dismissText = "Отменить",
                onDismissRequest = { /* do nothing */ },
                onConfirmButtonClick = { /* do nothing */ },
                onDismissButtonClick = { /* do nothing */ }
            )
        }
    }
}
