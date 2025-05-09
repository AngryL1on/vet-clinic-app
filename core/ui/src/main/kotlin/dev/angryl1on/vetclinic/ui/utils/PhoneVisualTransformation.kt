package dev.angryl1on.vetclinic.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

object PhoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter(Char::isDigit)
        if (digits.isEmpty()) {
            val empty = object : OffsetMapping {
                override fun originalToTransformed(offset: Int) = 0
                override fun transformedToOriginal(offset: Int) = 0
            }
            return TransformedText(AnnotatedString(""), empty)
        }

        // Первую «7» убираем из вывода, всё остальное отображаем
        val body = digits.drop(1)

        val out = buildString {
            append("+7")
            for (i in body.indices) {
                when (i) {
                    0      -> append("(")
                    3      -> append(")")
                    6, 8   -> append("-")
                }
                append(body[i])
            }
        }

        val offset = object : OffsetMapping {
            override fun originalToTransformed(o: Int) = out.length
            override fun transformedToOriginal(o: Int) = digits.length
        }

        return TransformedText(AnnotatedString(out), offset)
    }
}
