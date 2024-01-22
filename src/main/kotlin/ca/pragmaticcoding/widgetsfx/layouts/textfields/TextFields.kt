@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.textfields

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.converter.DoubleStringConverter
import javafx.util.converter.IntegerStringConverter
import javafx.util.converter.NumberStringConverter
import java.util.function.UnaryOperator

/**
 * Factory method to create a TextField bound to a StringProperty
 *
 * @param boundValue String property to bind to the TextField's text property
 */
fun textFieldOf(boundValue: StringProperty) = TextField().apply { textProperty().bindBidirectional(boundValue) }


/**
 * Infix function to bind the textProperty() of a TextField to an external StringProperty
 *
 * @param value The external StringProperty to bind to
 */

infix fun TextField.bindTo(value: StringProperty) = apply { textProperty().bind(value) }

fun stringField(contents: StringProperty?) = TextField().apply {
    textProperty().bindBidirectional(contents)
    styleClass += "data-text"
}

fun decimalField(contents: Property<Number>, maxWidth: Double) = decimalField(contents).apply {
    this.maxWidth = maxWidth
}

fun decimalField(contents: Property<Number>) = TextField().apply {
    Bindings.bindBidirectional(textProperty(), contents, NumberStringConverter())
    styleClass += "data-text"
}


fun decimalField(boundProperty: Property<Double>, decimalPlaces: Int, maxWidth: Double) = TextField().apply {
    this.maxWidth = maxWidth
    val textFormatter = TextFormatter(
        FixedDecimalConverter(decimalPlaces),
        boundProperty.value,
        FixedDecimalFilter(decimalPlaces)
    )
    this.textFormatter = textFormatter
    boundProperty.bindBidirectional(textFormatter.valueProperty())
}


fun integerField(boundProperty: Property<Int>, maxWidth: Double) = TextField().apply {
    this.maxWidth = maxWidth
    val textFormatter = TextFormatter(ZeroIntegerStringConverter(), boundProperty.value, IntegerFilter())
    this.textFormatter = textFormatter
    boundProperty.bindBidirectional(textFormatter.valueProperty())
}

class ZeroIntegerStringConverter : IntegerStringConverter() {
    override fun fromString(s: String): Int {
        if (s.isEmpty()) {
            return 0
        }
        return super.fromString(s)
    }
}


class FixedDecimalConverter(private val decimalPlaces: Int) : DoubleStringConverter() {
    override fun toString(value: Double): String {
        return String.format("%." + decimalPlaces + "f", value)
    }

    override fun fromString(valueString: String): Double {
        if (valueString.isEmpty()) {
            return 0.0
        }
        return super.fromString(valueString)
    }
}

class FixedDecimalFilter(private val decimalPlaces: Int) : UnaryOperator<TextFormatter.Change?> {
    override fun apply(theChange: TextFormatter.Change?): TextFormatter.Change? = theChange?.let { change ->
        val decimalPos = change.controlText.indexOf(".")
        val caretPos = change.controlCaretPosition
        when (change.text) {
            "." -> {
                change.text = ""
                change.setRange(0, 0)
                if (caretPos <= decimalPos) {
                    change.caretPosition = decimalPos + 1
                    change.anchor = change.controlText.length
                } else {
                    change.caretPosition = decimalPos
                    change.anchor = 0
                }
                return change
            }

            "-" -> {
                if (change.controlText.startsWith("-")) {
                    change.text = ""
                    change.setRange(0, 1)
                    change.caretPosition -= 2
                    change.anchor -= 2
                } else {
                    change.setRange(0, 0)
                }
                return change
            }

            else -> {}
        }
        if ((change.selection.start == 0) && (change.selection.end == change.controlText.length)) {
            change.selectRange(0, decimalPos)
            return change
        }
        if ((change.anchor <= decimalPos) && (change.caretPosition > decimalPos)) {
            change.selectRange(0, decimalPos)
            return change
        }
        if ((change.anchor > decimalPos) && (change.caretPosition <= decimalPos)) {
            change.selectRange(decimalPos + 1, change.controlText.length)
            return change
        }
        if (change.isContentChange) {
            if (caretPos > decimalPos) {
                val newText = change.controlNewText
                val decimalSize = newText.substring(decimalPos + 1).length
                if (decimalSize < decimalPlaces) {
                    change.text += "0"
                }
                if (decimalSize > decimalPlaces) {
                    change.setRange(decimalPos + 1, decimalPos + 1 + decimalPlaces)
                    if (caretPos == newText.length) {
                        change.text = newText.substring(decimalPos + 2, decimalPos + 2 + decimalPlaces)
                    } else {
                        change.text = newText.substring(decimalPos + 1, decimalPos + 1 + decimalPlaces)
                    }
                }
            }
        }
        if (change.controlNewText.matches("-?([0-9]*)?(\\.[0-9]*)?".toRegex())) {
            return change
        }
        return null
    }
}

class IntegerFilter : UnaryOperator<TextFormatter.Change?> {
    override fun apply(theChange: TextFormatter.Change?): TextFormatter.Change? {
        theChange?.let { change ->
            val newText = change.controlNewText
            if (newText.matches("-?([1-9][0-9]*)?".toRegex()) || newText == "0") {
                return change
            } else if ("-" == change.text) {
                if (change.controlText.startsWith("-")) {
                    change.text = ""
                    change.setRange(0, 1)
                    change.caretPosition -= 2
                    change.anchor -= 2
                    return change
                } else {
                    change.setRange(0, 0)
                    return change
                }
            }
            return null
        } ?: return null
    }
}


