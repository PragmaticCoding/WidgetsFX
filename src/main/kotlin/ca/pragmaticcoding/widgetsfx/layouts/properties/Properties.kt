package ca.pragmaticcoding.widgetsfx.layouts.properties

import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

/**
 * Operator function to define += as bind()
 */

operator fun StringProperty.plusAssign(otherProperty: ObservableValue<String>) = this.bind(otherProperty)