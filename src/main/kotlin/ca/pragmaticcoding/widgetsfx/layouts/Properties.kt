@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts

import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableValue

operator fun StringProperty.plusAssign(otherProperty: ObservableValue<String>) = this.bind(otherProperty)