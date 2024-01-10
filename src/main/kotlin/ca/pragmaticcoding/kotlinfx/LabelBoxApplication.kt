package ca.pragmaticcoding.kotlinfx

import ca.pragmaticcoding.widgetsfx.layouts.addStyleSheet
import ca.pragmaticcoding.widgetsfx.layouts.addWidgetStyles
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

class LabelBoxApplication : Application() {
    override fun start(stage: Stage) {
        val scene = Scene(createContent(), 320.0, 240.0).apply {
            addStyleSheet("/ca/pragmaticcoding/widgetsfx/css/LabelBox.css")
            addWidgetStyles()
        }
        stage.title = "LabelBox Demo"
        stage.scene = scene
        stage.show()
    }

    fun createContent() = BorderPane().apply {
        center = VBox(Label("This is the Title").apply {
            styleClass += "box-label"
        }).apply {
            styleClass += "label-box"
            padding = Insets(-10.0, 0.0, 0.0, 8.0)
        }
        padding = Insets(100.0, 0.0, 200.0, 0.0)
        styleClass += "wrapper-region"
    }
}

fun main() {
    Application.launch(LabelBoxApplication::class.java)
}