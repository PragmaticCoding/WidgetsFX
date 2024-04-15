@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.labels

import ca.pragmaticcoding.widgetsfx.layouts.addStyle
import ca.pragmaticcoding.widgetsfx.layouts.labels.LabelStyle.*
import ca.pragmaticcoding.widgetsfx.layouts.properties.plusAssign
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableObjectValue
import javafx.beans.value.ObservableStringValue
import javafx.scene.Node
import javafx.scene.control.ContentDisplay
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
 * Decorator function to apply a [LabelStyle] to a Label
 *
 * @param labelStyle [LabelStyle] to apply to the Label
 * @receiver [Labeled]
 * @return Labeled
 */
infix fun <T : Labeled> T.styleAs(labelStyle: LabelStyle): T = apply { styleClass += labelStyle.selector }

/**
 * Decorator function to bind the graphicProperty() of a Labeled to a Property
 * @param graphicProperty ObservableObjectValue<Node> to bind to the graphicProperty
 */
infix fun <T : Labeled> T.bindGraphic(graphicProperty: ObservableObjectValue<Node>): T =
    apply { graphicProperty().bind(graphicProperty) }

/**
 * Decorator function to bind the textProperty() of a Labeled to an external Property
 * @param value External ObservableStringValue to bind to textProperty()
 */
infix fun <T : Labeled> T.bindTo(value: ObservableStringValue): T = apply { textProperty().bind(value) }
fun <T : Labeled> T.wrapText(): T = apply { isWrapText = true }
infix fun <T : Labeled> T.underlined(setUnderlineOn: Boolean): T = apply { isUnderline = setUnderlineOn }
infix fun <T : Labeled> T.oriented(orientation: ContentDisplay): T = apply { contentDisplay = orientation }


/**
 * Factory method to create a Label with its textProperty() bound to an external property
 * and styled as LabelStyle.PROMPT
 * @param value External ObservableStringValue to bind to the Label's Text property
 */
fun promptOf(value: ObservableStringValue) = Label() styleAs PROMPT bindTo value

/**
 * Factory method to create a Label with a static Text value
 * and styled as LabelStyle.PROMPT
 * @param value String to set as the Text value
 */
fun promptOf(value: String) = Label(value) styleAs PROMPT

/**
 * Factory method to create a Label with its textProperty() bound to an external property
 * @param value External ObservableStringValue to bind to the Label's Text property
 */
fun labelOf(value: ObservableStringValue) = Label() bindTo value

/**
 * Factory method to create a Label with its textProperty() bound to an external property,
 * a styleclass selector added, and optionally with a Graphic value set
 * @param value External ObservableStringValue to bind to the Label's Text property
 * @param styleClass String to use as the styleclass selector
 * @param graphicNode Optional: Node to use as the Graphic for the Label
 */
fun labelOf(value: ObservableStringValue, styleClass: String, graphicNode: Node? = null) = Label().apply {
    graphicNode?.let { graphic = it }
} bindTo value addStyle styleClass

/**
 * Factory method to create a Label with its textProperty() bound to an external property,
 * a styleclass selector added, and its Graphic property bound to an external property
 * @param value External ObservableStringValue to bind to the Label's Text property
 * @param styleClass String to use as the styleclass selector
 * @param graphicProperty ObservableObjectValue<Node> to bind to the Graphic property of the Label
 */
fun labelOf(value: ObservableStringValue, styleClass: String, graphicProperty: ObservableObjectValue<Node>) =
    Label() bindTo value bindGraphic graphicProperty addStyle styleClass

/**
 * Factory method to create a Label styled as an [H1]
 *
 * @param value The text to place in the Label
 * @return Label
 */
fun h1Of(value: String, graphic: Node? = null) = Label(value, graphic) styleAs H1

/**
 * Factory method to create a Label styled as an [H1] with its Text property bound to
 * an ObservableStringValue
 *
 * @param value ObservableStringProperty to bind the Label's Text property to
 * @return Label
 */
fun h1Of(value: ObservableStringValue, graphic: Node? = null) = Label("", graphic) styleAs H1 bindTo value

/**
 * Factory method to create a Label styled as an [H2]
 *
 * @param value The text to place in the Label
 * @return Label
 */
fun h2Of(value: String, graphic: Node? = null) = Label(value, graphic) styleAs H2

/**
 * Factory method to create a Label styled as an [H2] with its Text property bound to
 * an ObservableStringValue
 *
 * @param value ObservableStringProperty to bind the Label's Text property to
 * @return Label
 */
fun h2Of(value: ObservableStringValue, graphic: Node? = null) = Label("", graphic) styleAs H2 bindTo value

/**
 * Factory method to create a Label styled as an [H3]
 *
 * @param value The text to place in the Label
 * @return Label
 */
fun h3Of(value: String, graphic: Node? = null) = Label(value, graphic) styleAs H3

/**
 * Factory method to create a Label styled as an [H3] with its Text property bound to
 * an ObservableStringValue
 *
 * @param value ObservableStringProperty to bind the Label's Text property to
 * @return Label
 */
fun h3Of(value: ObservableStringValue, graphic: Node? = null) = Label("", graphic) styleAs H3 bindTo value

/**
 * Factory method to create a Label styled as an [DATA]
 *
 * @param value The text to place in the Label
 * @return Label
 */
fun dataOf(value: String, graphic: Node? = null) = Label(value, graphic) styleAs DATA

/**
 * Factory method to create a Label styled as an [DATA] with its Text property bound to
 * an ObservableStringValue
 *
 * @param value ObservableStringProperty to bind the Label's Text property to
 * @return Label
 */
fun dataOf(value: ObservableStringValue, graphic: Node? = null) = Label("", graphic) styleAs DATA bindTo value

/**
 * Operator definition for += for Labels that binds the Label's Text property to another StringProperty
 *
 * @param otherProperty StringProperty to bind the Label's Text property to.
 */
operator fun Labeled.plusAssign(otherProperty: StringProperty) = run { textProperty() += otherProperty }



