package ca.pragmaticcoding.widgetsfx.layouts.properties

import javafx.beans.property.BooleanPropertyBase
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.css.PseudoClass
import javafx.scene.Node

/**
 * Operator function to define += as bind()
 */

operator fun StringProperty.plusAssign(otherProperty: ObservableValue<String>) = this.bind(otherProperty)

class PseudoClassProperty(private val node: Node, private val pseudoClass: PseudoClass) : BooleanPropertyBase() {
    override fun getBean() = node
    override fun getName(): String = pseudoClass.pseudoClassName

    override fun invalidated() {
        node.pseudoClassStateChanged(pseudoClass, value)
    }
}

infix fun Node.addPseudoClass(pseudoClass: PseudoClass) = PseudoClassProperty(this, pseudoClass)