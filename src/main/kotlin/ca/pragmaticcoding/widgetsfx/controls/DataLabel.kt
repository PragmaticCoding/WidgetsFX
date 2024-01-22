@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.controls

import javafx.animation.Transition
import javafx.beans.InvalidationListener
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableStringValue
import javafx.css.PseudoClass
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.util.Duration

/**
 * A specialized form of [Label], designed to display data values
 * and as an alternative to using disabled TextFields.
 * This element uses the CSS selector "label-data", and has a
 * pseudo-class ":error" to handle display when the data is flagged
 * as being in an error state.
 * This control has animated copy to clipboard
 * functionality added, where <CTRL><RightClick> will copy the
 * entire contents of the Text to the clipboard.  Additionally, a small
 * animation will run to cue the user that the copy has happened.
 * </RightClick></CTRL> */
class DataLabel : Label {
    private val errorFlag: BooleanProperty = SimpleBooleanProperty(false)

    companion object {
        private val ERROR_CLASS: PseudoClass = PseudoClass.getPseudoClass("error")
    }

    constructor(boundValue: ObservableStringValue) : super() {
        initialize()
        textProperty().bind(boundValue)
    }

    constructor(text: String) : super(text) {
        initialize()
    }

    private fun initialize() {
        errorFlag.addListener(InvalidationListener { pseudoClassStateChanged(ERROR_CLASS, errorFlag.get()) })
        styleClass.add("label-data")
        onMouseClicked = EventHandler { evt: MouseEvent ->
            if (evt.button == MouseButton.SECONDARY && evt.isControlDown) {
                Clipboard.getSystemClipboard().setContent(ClipboardContent().apply {
                    putString(text)
                })
                animateClipboardCapture()
            }
        }
    }

    private fun animateClipboardCapture() {
        object : Transition() {
            init {
                cycleDuration = Duration.millis(150.0)
                isAutoReverse = true
                cycleCount = 2
            }

            override fun interpolate(frac: Double) {
                scaleX = 1 - frac / 4
                scaleY = 1 - frac / 5
            }
        }.play()
    }

    var isError: Boolean
        get() = errorFlag.get()
        set(errorFlag) {
            this.errorFlag.set(errorFlag)
        }

    fun errorProperty(): BooleanProperty {
        return errorFlag
    }

    fun withBoundStatus(boundStatus: ObservableBooleanValue) = apply {
        errorFlag.bind(boundStatus)
    }
}
