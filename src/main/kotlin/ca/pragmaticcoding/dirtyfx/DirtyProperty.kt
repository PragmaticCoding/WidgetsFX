package ca.pragmaticcoding.dirtyfx

import javafx.beans.value.ObservableValue

interface DirtyProperty<T> {

    val base: DirtyPropertyBase<T>

    fun isDirtyProperty(): ObservableValue<Boolean> = base.isDirtyProperty()
    val isDirty: Boolean
        get() = isDirtyProperty().value

    fun rebase() = base.rebase()
    fun reset() = base.reset()
    fun baseValueProperty(): ObservableValue<T> = base.baseValueProperty()
}

