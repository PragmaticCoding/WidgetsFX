package ca.pragmaticcoding.widgetsfx.layouts.toggles

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.css.PseudoClass
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.StackPane


interface TogglePart {
    fun getToggleGroup(): ToggleGroup?

    fun setToggleGroup(var1: ToggleGroup?)

    fun toggleGroupProperty(): ObjectProperty<ToggleGroup?>

    fun isSelected(): Boolean

    fun setSelected(var1: Boolean)

    fun selectedProperty(): BooleanProperty
}

class ToggleImpl : TogglePart {
    private val toggleGroup: ObjectProperty<ToggleGroup?> = SimpleObjectProperty()
    private var selected: BooleanProperty = SimpleBooleanProperty()

    override fun getToggleGroup(): ToggleGroup? = toggleGroup.value

    override fun setToggleGroup(var1: ToggleGroup?) {
        toggleGroup.value = var1
    }

    override fun toggleGroupProperty() = toggleGroup

    override fun isSelected(): Boolean = selected.value

    override fun setSelected(var1: Boolean) {
        selected.value = var1
    }

    override fun selectedProperty() = selected

    fun initializeSelected(node: Toggle) {
        selectedProperty().addListener { _, _, newVal ->
            getToggleGroup()?.let {
                if (newVal) {
                    it.selectToggle(node)
                } else if (it.selectedToggle === (node)) {
                    it.selectToggle(null)
                }
            }
        }
    }
}

class ToggleStackPane(toggleImpl: ToggleImpl = ToggleImpl()) : StackPane(), TogglePart by toggleImpl, Toggle {
    companion object {
        val PSEUDO_CLASS_SELECTED: PseudoClass = PseudoClass.getPseudoClass("selected")
    }

    init {
        styleClass += "toggle-stackpane"
        selectedProperty().addListener { _, _, newVal -> pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, newVal) }
        toggleImpl.initializeSelected(this)
    }
}
