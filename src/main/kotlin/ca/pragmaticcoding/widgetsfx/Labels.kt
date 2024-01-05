package ca.pragmaticcoding.widgetsfx

import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableObjectValue
import javafx.beans.value.ObservableStringValue
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.Labeled

enum class LabelStyle(val selector: String) {
    PROMPT("label-prompt"), HEADING("label-heading")
}

/**
 *
 */
infix fun <T : Labeled> T.styleAs(labelStyle: LabelStyle) = apply { styleClass += labelStyle.selector }
infix fun <T : Labeled> T.styleAs(labelStyle: String) = apply { styleClass += labelStyle }
infix fun <T : Labeled> T.bindGraphic(graphicProperty: ObservableObjectValue<Node>) =
    apply { graphicProperty().bind(graphicProperty) }


infix fun <T : Labeled> T.bindTo(value: ObservableStringValue) = apply { textProperty().bind(value) }

fun promptOf(value: ObservableStringValue) = Label() styleAs LabelStyle.PROMPT bindTo value
fun promptOf(value: String) = Label(value) styleAs LabelStyle.PROMPT
fun labelOf(value: ObservableStringValue) = Label() bindTo value
fun labelOf(value: ObservableStringValue, styleClass: String, graphicNode: Node? = null) = Label().apply {
    graphicNode?.let { graphic = it }
} bindTo value addStyle styleClass

fun labelOf(value: ObservableStringValue, styleClass: String, graphicProperty: ObservableObjectValue<Node>) =
    Label() bindTo value bindGraphic graphicProperty addStyle styleClass


fun headingOf(value: String) = Label() styleAs LabelStyle.HEADING

operator fun Labeled.plusAssign(otherProperty: StringProperty) = run { textProperty() += otherProperty }

