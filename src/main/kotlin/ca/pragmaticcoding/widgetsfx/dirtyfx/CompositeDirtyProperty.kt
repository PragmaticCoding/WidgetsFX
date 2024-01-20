@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.dirtyfx

import javafx.beans.InvalidationListener
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList

/**
 * CompositeDirtyProperty
 * @param base The new base class to use
 */
class CompositeDirtyProperty(override val base: DirtyPropertyBase<Any>) : DirtyProperty<Any>,
    ObservableValue<Boolean> {

    val items: ObservableList<DirtyProperty<Any>> = FXCollections.observableArrayList<DirtyProperty<Any>>()
    private val isDirtyProperty: BooleanProperty = SimpleBooleanProperty(false)
    override val isDirty: Boolean
        get() = isDirtyProperty.value

    init {
        items.addListener(ListChangeListener {
            isDirtyProperty.unbind()
            isDirtyProperty.bind(listBinding())
        })
    }

    private fun listBinding(): BooleanBinding = object : BooleanBinding() {
        init {
            items.forEach { super.bind(it.isDirtyProperty()) }
        }

        override fun computeValue(): Boolean = items.any { dirtyProperty -> dirtyProperty.isDirty }
    }

    override fun rebase() {
        items.forEach { it.rebase() }
    }

    override fun reset() {
        items.forEach { it.reset() }
    }

    override fun isDirtyProperty(): ObservableValue<Boolean> = isDirtyProperty

    fun addAll(vararg dirtyProperties: DirtyProperty<Any>) {
        dirtyProperties.forEach { add(it) }
    }

    fun add(dirtyProperty: DirtyProperty<Any>) {
        items += dirtyProperty
    }

    fun remove(dirtyProperty: DirtyProperty<Any>) {
        items -= dirtyProperty
    }

    fun clear() = items.clear()

    operator fun plusAssign(dirtyProperty: DirtyProperty<Any>) {
        items += dirtyProperty
    }

    operator fun minusAssign(dirtyProperty: DirtyProperty<Any>) {
        items += dirtyProperty
    }

    override fun removeListener(listener: ChangeListener<in Boolean>?) = isDirtyProperty.removeListener(listener)

    override fun removeListener(listener: InvalidationListener?) = isDirtyProperty.removeListener(listener)

    override fun addListener(listener: ChangeListener<in Boolean>?) = isDirtyProperty.addListener(listener)

    override fun addListener(listener: InvalidationListener?) = isDirtyProperty.addListener(listener)

    override fun getValue() = isDirtyProperty.value
}