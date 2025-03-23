package dev.angryl1on.vetclinic.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toSp() =
    LocalDensity.current.run {
        this@toSp.toSp()
    }
