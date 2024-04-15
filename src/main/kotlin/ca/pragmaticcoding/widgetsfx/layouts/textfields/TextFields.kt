@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.textfields

import javafx.beans.binding.Bindings
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.layout.Region
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
 * Infix function to bind the textProperty() of a TextField to an external StringProperty bidirectionally
 *
 * @param value The external StringProperty to bind to
 */

infix fun TextField.bindTo(value: StringProperty) = apply { textProperty().bindBidirectional(value) }

/**
 * Factiory method to create a [TextField] bound bidirectionally to an external [StringProperty]
 * @param contents [StringProperty] to bind to the [TextField]
 */
fun stringField(contents: StringProperty) = TextField().apply {
    textProperty().bindBidirectional(contents)
    styleClass += "data-text"
}

/**
 * Factiory method to create a [TextField] bound bidirectionally to an external [StringProperty]
 * with a specified maximum width
 * @param contents [StringProperty] to bind to the [TextField]
 * @param maxWidth The maximum allow width of the [TextField] (optional)
 */
fun decimalField(contents: Property<Number>, maxWidth: Double = Region.USE_COMPUTED_SIZE) =
    decimalField(contents).apply {
        this.maxWidth = maxWidth
    }

/**
 * Factiory method to create a [TextField] bound bidirectionally to an external [Property<Number>]
 * This [TextField] uses [NumberStringConverter] to convert between the [TextField] contents
 * and the external bound [Property]
 * @param contents [Property<Number>] to bind to the [TextField]
 */
fun decimalField(contents: Property<Number>) = TextField().apply {
    Bindings.bindBidirectional(textProperty(), contents, NumberStringConverter())
    styleClass += "data-text"
}

/**
 * Factory method for a customized [TextField] configured for fixed-spaced decimal entry.
 * See [FixedDecimalFilter] for further details
 * @param boundProperty Property<Double> to bind bidirectionally to the TextField
 * @param decimalPlaces Number of decimal places for user input
 * @param maxWidth The maximum width for the TextField (optional)
 */
fun decimalField(boundProperty: Property<Double>, decimalPlaces: Int, maxWidth: Double = Region.USE_COMPUTED_SIZE) =
    TextField().apply {
        this.maxWidth = maxWidth
        val textFormatter = TextFormatter(
            FixedDecimalConverter(decimalPlaces),
            boundProperty.value,
            FixedDecimalFilter(decimalPlaces)
        )
        this.textFormatter = textFormatter
        boundProperty.bindBidirectional(textFormatter.valueProperty())
    }

/**
 * Factiory method to create a [TextField] bound bidirectionally to an external [IntegerProperty]
 * @param boundProperty [Property<Int>] to bind to the [TextField]
 * @param maxWidth Option size to limit the width of the [TextField]
 */

fun integerField(boundProperty: Property<Int>, maxWidth: Double = Region.USE_COMPUTED_SIZE) = TextField().apply {
    this.maxWidth = maxWidth
    val textFormatter = TextFormatter(ZeroIntegerStringConverter(), boundProperty.value, IntegerFilter())
    this.textFormatter = textFormatter
    boundProperty.bindBidirectional(textFormatter.valueProperty())
}

/**
 * Custom Integer-String converter that converts "" to 0 instead of Null
 */
class ZeroIntegerStringConverter : IntegerStringConverter() {
    override fun fromString(s: String): Int {
        if (s.isEmpty()) {
            return 0
        }
        return super.fromString(s)
    }
}

/**
 * Double-String converter that formats output strings to a fixed number of decimal places.
 * Additionally, converts empty [String] input to 0.0 instead of Null.
 * @param decimalPlaces Number of decimal places to use in the formatted [String] output
 */
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

/**
 * TextField input filter to support fixed place decimal input.
 * This is highly functional, but perhaps counter-intuitive for users.  The "." is used as a
 * toggle to switch between the whole number part and the decimal part.  Entering more digits than
 * in the decimal portion than allowed by the [decimalPlaces] parameter will result in digits being
 * dropped from the other end of the decimal portion.
 * @param decimalPlaces The number of decimal places to be used in data entry
 */
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

/**
 * Filter for [TextField] to allow only valid integer numbers to be entered
 */
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


