package ca.pragmaticcoding.kotlinfx

import ca.pragmaticcoding.dirtyfx.DirtyBooleanProperty
import ca.pragmaticcoding.widgetsfx.addStyleSheet
import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.*
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import javafx.stage.Stage

class LabelBox2Application : Application() {
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
        padding = Insets(20.0, 20.0, 20.0, 20.0)
        styleClass += "wrapper-region"
        center = VBox(20.0, createPane(), createPane())
    }

    private fun createPane(): Region {
        val dirtyBoolean = DirtyBooleanProperty(false)
        val dirtyBoolean2 = DirtyBooleanProperty()
        val dirtyBoolean3 = DirtyBooleanProperty(this, "fred", false)
        val dirtyBoolean4 = DirtyBooleanProperty(this, "fred")
        val george = dirtyBoolean3.getBean()
        dirtyBoolean4.rebase()
        val label = Label("This is the Title").apply {
            layoutX = 10.0
            styleClass += "box-label"
        }
        val borderPane = Pane().apply {
            styleClass += "border-pane"
            layoutYProperty().bind(label.heightProperty().divide(2.0))
        }
        val content = StackPane().apply {
            layoutX = 5.0
            layoutYProperty().bind(label.heightProperty().add(2.0))
            children += Button("This is Some Content")
        }
        val pane = Pane().apply {
            minHeight = 200.0
            children += listOf(borderPane, label, content)
            needsLayoutProperty().addListener(InvalidationListener {
                Platform.runLater { setClipping(borderPane, label) }
            })
            borderPane.minWidthProperty().bind(widthProperty())
            borderPane.minHeightProperty().bind(heightProperty().subtract(label.heightProperty().divide(2)))
            content.minWidthProperty().bind(widthProperty().subtract(10.0))
            content.minHeightProperty().bind(heightProperty().subtract(label.heightProperty().add(6.0)))
            borderPane.widthProperty().addListener(InvalidationListener { setClipping(borderPane, label) })
            borderPane.heightProperty().addListener(InvalidationListener { setClipping(borderPane, label) })
            label.widthProperty().addListener(InvalidationListener { setClipping(borderPane, label) })
            label.heightProperty().addListener(InvalidationListener { setClipping(borderPane, label) })
        }

        return pane
    }

    private fun setClipping(nodeToClip: Region, areaToHide: Region) {
        val rectangle = Rectangle(nodeToClip.width, nodeToClip.height + nodeToClip.layoutY).apply {
            layoutX = nodeToClip.layoutX
        }
        println(areaToHide.width)
        val clip = Rectangle(areaToHide.width + 5.0, areaToHide.height).apply {
            layoutX = areaToHide.layoutX
            layoutY = areaToHide.layoutY
        }
        nodeToClip.clip = Shape.subtract(rectangle, clip)
    }
}


fun main() {
    Application.launch(LabelBox2Application::class.java)
}
