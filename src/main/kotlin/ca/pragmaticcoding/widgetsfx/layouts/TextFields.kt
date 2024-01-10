@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts

import javafx.beans.property.StringProperty
import javafx.scene.control.TextField

/**
 * Factory method to create a TextField bound to a StringProperty
 *
 * @param boundValue String property to bind to the TextField's text property
 */
fun textFieldOf(boundValue: StringProperty) = TextField().apply { textProperty().bindBidirectional(boundValue) }

infix fun TextField.bindTo(value: StringProperty) = apply { textProperty().bind(value) }