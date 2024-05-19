package ca.pragmaticcoding.widgetsfx.layouts.toggles

import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.css.PseudoClass
import javafx.scene.Scene
import javafx.scene.control.Toggle
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.stage.Stage


interface TogglePart {
    fun getToggleGroup(): ToggleGroup?

    fun setToggleGroup(var1: ToggleGroup?): Unit

    fun toggleGroupProperty(): ObjectProperty<ToggleGroup?>

    fun isSelected(): Boolean

    fun setSelected(var1: Boolean)

    fun selectedProperty(): BooleanProperty
}

class ToggleImpl : TogglePart {
    private val toggleGroup: ObjectProperty<ToggleGroup?> = SimpleObjectProperty()
    private var selected: BooleanProperty = SimpleBooleanProperty()

    override fun getToggleGroup(): ToggleGroup? = toggleGroup.value

    override fun setToggleGroup(p0: ToggleGroup?) {
        toggleGroup.value = p0
    }

    override fun toggleGroupProperty() = toggleGroup

    override fun isSelected(): Boolean = selected.value

    override fun setSelected(p0: Boolean) {
        selected.value = p0
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

class TogglesApplication : Application() {
    override fun start(stage: Stage) {
        stage.scene = Scene(createContent(), 340.0, 300.0).apply {
            TogglesApplication::class.java.getResource("toggles.css")?.toString()?.let { stylesheets += it }
        }
        stage.show()
    }

    private fun createContent(): Region = GridPane().also { gridPane ->
        val toggleGroup = ToggleGroup()
        for (x in 0..8) {
            for (y in 0..8) {
                val pane = ToggleStackPane().apply {
                    styleClass += "toggle-stackpane"
                    setOnMouseClicked { isSelected = true }
                    isPickOnBounds = true
                    toggleGroup.toggles.add(this)
                    gridPane.add(this, x, y)
                }
            }
        }
    }
}

fun main() = Application.launch(TogglesApplication::class.java)