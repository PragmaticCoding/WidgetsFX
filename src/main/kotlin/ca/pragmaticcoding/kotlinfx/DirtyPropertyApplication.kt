package ca.pragmaticcoding.kotlinfx

import ca.pragmaticcoding.dirtyfx.DirtyDoubleProperty
import ca.pragmaticcoding.widgetsfx.addStyleSheet
import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.application.Application
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class DirtyPropertyApplication : Application() {
    override fun start(stage: Stage) {
        val scene = Scene(createContent(), 320.0, 240.0).apply {
            addStyleSheet("/ca/pragmaticcoding/widgetsfx/css/LabelBox.css")
            addWidgetStyles()
        }
        stage.title = "DirtyProperty Demo"
        stage.scene = scene
        stage.show()
    }

    private fun createContent() = BorderPane().apply {
        val testProperty = DirtyDoubleProperty(3.2)
        center = VBox(Label("").apply {
            textProperty().bind(
                Bindings.createStringBinding(
                    { testProperty.isDirty.toString() },
                    testProperty.isDirtyProperty()
                )
            )
            styleClass += "box-label"
        }, Label("").apply {
            textProperty().bind(
                Bindings.createStringBinding(
                    { testProperty.baseValueProperty().value.toString() },
                    testProperty.baseValueProperty()
                )
            )
            styleClass += "box-label"
        }, Label().apply {
            textProperty().bind(testProperty.asString())
        }).apply {
            styleClass += "label-box"
            padding = Insets(-10.0, 0.0, 0.0, 8.0)
        }
        bottom = HBox(10.0, Button("Rebase").apply {
            onAction = EventHandler { testProperty.rebase() }
        }, Button("Reset").apply {
            onAction = EventHandler { testProperty.reset() }
        }, Button("Add").apply {
            onAction = EventHandler { testProperty.value = testProperty.value + 2.2 }
        }
        )
        padding = Insets(100.0, 0.0, 200.0, 0.0)
        styleClass += "wrapper-region"
    }
}

fun main() {
    Application.launch(LabelBoxApplication::class.java)
}