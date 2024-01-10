package ca.pragmaticcoding.kotlinfx

import ca.pragmaticcoding.widgetsfx.layouts.h1Of
import ca.pragmaticcoding.widgetsfx.layouts.plusAssign
import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import kotlin.math.max
import kotlin.math.min

class LabelledPane(labelText: String) : Region() {

    private val rectangle = Rectangle()
    private val clipZone = Rectangle()
    private val id: Int = counter++

    companion object {
        var counter = 0

    }

    private val label = Label(labelText).apply {
        layoutX = 10.0
        styleClass += "pane-label"
        widthProperty().addListener(InvalidationListener { setClipping() })
        heightProperty().addListener(InvalidationListener {
            setClipping()
        })
        maxWidthProperty().bind(this@LabelledPane.widthProperty().subtract(24.0))
        isWrapText = true
    }
    private val borderPane = Pane().apply {
        styleClass += "pane-border"
        minWidthProperty().bind(this@LabelledPane.widthProperty())
        minHeightProperty().bind(this@LabelledPane.heightProperty().subtract(label.heightProperty().divide(2)))
        layoutYProperty().bind(label.heightProperty().divide(2.0))
        widthProperty().addListener(InvalidationListener { setClipping() })
        heightProperty().addListener(InvalidationListener { setClipping() })
    }

    private val content = VBox().apply {
        layoutX = 5.0
        layoutYProperty().bind(label.heightProperty().add(2.0))
        maxWidthProperty().bind(this@LabelledPane.widthProperty().subtract(10.0))
    }

    init {
        content += HBox(h1Of("Fred"))
        children += listOf(borderPane, label, content)
        styleClass += "labelled-pane"
        minHeightProperty().bind(label.heightProperty().add(content.minHeightProperty()).add(20.0))
        widthProperty().addListener(InvalidationListener { prefHeight = getContentHeight() })
        needsLayoutProperty().addListener(InvalidationListener {
            Platform.runLater {
                setClipping()
                content.maxHeightProperty().unbind()
                prefHeight = getContentHeight()
                content.maxHeightProperty().bind(heightProperty().subtract(label.heightProperty()).subtract(10.0))
            }
        })
    }

    private fun getContentHeight(): Double {
        val value = computeChildPrefAreaHeight(content, width - 6.0)
        println("$id New PrefHeight = $value")
        return value + label.height + 10.0
    }


    private fun computeChildPrefAreaHeight(child: Node, width: Double): Double {
        var snapWidth = -1.0
        if (child.isResizable && child.contentBias == Orientation.HORIZONTAL) {
            snapWidth = snapSizeX(
                boundedSize(
                    child.minWidth(-1.0),
                    if (width != -1.0) width else child.prefWidth(-1.0),
                    child.maxWidth(-1.0)
                )
            )
        }
        return snapSizeY(
            boundedSize(
                child.minHeight(snapWidth),
                child.prefHeight(snapWidth),
                child.maxHeight(snapWidth)
            )
        )
    }

    private fun boundedSize(min: Double, pref: Double, max: Double): Double {
        return min(max(pref, min), max(min, max))
    }

    private fun setClipping() {
        with(rectangle) {
            width = borderPane.width
            height = borderPane.height + borderPane.layoutY
            layoutX = borderPane.layoutX
        }
        with(clipZone) {
            width = label.width + 5.0
            height = label.height
            layoutX = label.layoutX
            layoutY = label.layoutY
        }
        borderPane.clip = Shape.subtract(rectangle, clipZone)
    }

    fun getContent(): ObservableList<Node> = content.children
    fun labelTextProperty() = label.textProperty()
}