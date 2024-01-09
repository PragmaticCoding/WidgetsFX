package ca.pragmaticcoding.widgetsfx

import ca.pragmaticcoding.widgetsfx.LabelStyle.*
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableObjectValue
import javafx.beans.value.ObservableStringValue
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.Labeled

/**
 * Standard Label styles defined in widgetsfx.css.  Use [styleAs] to apply these style selectors to
 * a Label
 */
enum class LabelStyle(val selector: String) {
    /**
     * Corresponds to the selector "label-prompt"
     */
    PROMPT("label-prompt"),

    /**
     * Corresponds to the selector "label-heading-1"
     */
    H1("label-heading-1"),

    /**
     * Corresponds to the selector "label-heading-2"
     */
    H2("label-heading-2"),

    /**
     * Corresponds to the selector "label-heading-3"
     */
    H3("label-heading-3"),

    /**
     * Corresponds to the selector "label-data"
     */
    DATA("label-data")
}

/**
 * Applies a [LabelStyle] to a Label
 *
 * @param labelStyle [LabelStyle] to apply to the Label
 * @receiver [Labeled]
 */
infix fun <T : Labeled> T.styleAs(labelStyle: LabelStyle) = apply { styleClass += labelStyle.selector }

infix fun <T : Labeled> T.bindGraphic(graphicProperty: ObservableObjectValue<Node>) =
    apply { graphicProperty().bind(graphicProperty) }


infix fun <T : Labeled> T.bindTo(value: ObservableStringValue) = apply { textProperty().bind(value) }

fun promptOf(value: ObservableStringValue) = Label() styleAs PROMPT bindTo value
fun promptOf(value: String) = Label(value) styleAs PROMPT
fun labelOf(value: ObservableStringValue) = Label() bindTo value
fun labelOf(value: ObservableStringValue, styleClass: String, graphicNode: Node? = null) = Label().apply {
    graphicNode?.let { graphic = it }
} bindTo value addStyle styleClass

fun labelOf(value: ObservableStringValue, styleClass: String, graphicProperty: ObservableObjectValue<Node>) =
    Label() bindTo value bindGraphic graphicProperty addStyle styleClass

/**
 * Factory method to create a Label styled as an [H1]
 *
 * @param value The text to place in the Label
 * @return Label
 */
fun h1Of(value: String) = Label() styleAs H1

/**
 * Factory method to create a Label styled as an [H1] with its Text property bound to
 * an ObservableStringValue
 *
 * @param value ObservableStringProperty to bind the Label's Text property to
 * @return Label
 */
fun h1Of(value: ObservableStringValue) = Label() styleAs H1 bindTo value

/**
 * Factory method to create a Label styled as an [H2]
 *
 * @param value The text to place in the Label
 * @return Label
 */
fun h2Of(value: String) = Label() styleAs H2

/**
 * Factory method to create a Label styled as an [H2] with its Text property bound to
 * an ObservableStringValue
 *
 * @param value ObservableStringProperty to bind the Label's Text property to
 * @return Label
 */
fun h2Of(value: ObservableStringValue) = Label() styleAs H2 bindTo value

/**
 * Factory method to create a Label styled as an [H3]
 *
 * @param value The text to place in the Label
 * @return Label
 */
fun h3Of(value: String) = Label() styleAs H3

/**
 * Factory method to create a Label styled as an [H3] with its Text property bound to
 * an ObservableStringValue
 *
 * @param value ObservableStringProperty to bind the Label's Text property to
 * @return Label
 */
fun h3Of(value: ObservableStringValue) = Label() styleAs H3 bindTo value


operator fun Labeled.plusAssign(otherProperty: StringProperty) = run { textProperty() += otherProperty }

