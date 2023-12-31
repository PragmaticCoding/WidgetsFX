package ca.pragmaticcoding.dirtyfx

import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableValue

open class DirtyPropertyBase<T>(private val wrapper: Property<T>) {

    private var baseValue: ObjectProperty<T> = SimpleObjectProperty(wrapper.value)
    private val isDirty: ObservableBooleanValue =
        Bindings.createBooleanBinding({ wrapper.value != baseValue.value }, wrapper, baseValue)

    fun rebase() {
        baseValue.value = wrapper.value
    }

    fun reset() {
        wrapper.value = baseValue.value
    }

    fun isDirtyProperty(): ObservableValue<Boolean> = isDirty
    fun isDirty(): Boolean = isDirty.value
    fun baseValueProperty(): ObservableValue<T> = baseValue
}