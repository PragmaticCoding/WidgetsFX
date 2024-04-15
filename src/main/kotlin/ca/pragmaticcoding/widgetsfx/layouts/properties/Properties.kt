package ca.pragmaticcoding.widgetsfx.layouts.properties

import javafx.beans.property.BooleanPropertyBase
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue
import javafx.css.PseudoClass
import javafx.scene.Node
import java.util.function.Function

/**
 * Operator function to define += as bind()
 */

operator fun StringProperty.plusAssign(otherProperty: ObservableValue<String>) = this.bind(otherProperty)

fun <T, U> ObservableValue<T>.flatPropertyMap(var1: Function<in T?, out Property<out U?>>): Property<U?> {
    return FlatMappedPropertyBinding(this, var1)
}

class PseudoClassProperty(private val node: Node, private val pseudoClass: PseudoClass) : BooleanPropertyBase() {
    override fun getBean() = node
    override fun getName(): String = pseudoClass.pseudoClassName

    override fun invalidated() {
        node.pseudoClassStateChanged(pseudoClass, value)
    }
}

infix fun Node.addPseudoClass(pseudoClass: PseudoClass) = PseudoClassProperty(this, pseudoClass)