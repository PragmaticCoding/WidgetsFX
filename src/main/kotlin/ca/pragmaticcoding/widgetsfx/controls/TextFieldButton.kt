@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.controls

import ca.pragmaticcoding.widgetsfx.layouts.promptOf
import ca.pragmaticcoding.widgetsfx.layouts.stringField
import javafx.beans.binding.Bindings
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import java.util.function.Consumer

/**
 * Encapsulated label, TextField and action button.  The action button is automatically
 * disabled once clicked, and re-enabled after the action consumer has completed.
 */
/**
 * Standard constructor
 *
 * @param prompt       String contents of the prompt Text
 * @param buttonText   String prompt for the action button
 * @param boundValue   String property bi-directionally bound to the input TextField
 * @param buttonAction Consumer of Runnable to perform the button action
 */
class TextFieldButton(
    private val prompt: String,
    private val buttonText: String,
    boundValue: StringProperty,
    private val buttonAction: Consumer<Runnable>
) : HBox() {
    private val boundValue: StringProperty = boundValue
    private var preRunAction: Runnable? = null

    init {
        initialize()
    }

    private var postRunAction: Runnable? = null

    private fun initialize() {
        spacing = 6.0
        alignment = Pos.CENTER_LEFT
        val button = Button(buttonText)
        val textField: TextField = stringField(boundValue)
        button.onAction = EventHandler { evt: ActionEvent? ->
            button.isDisable = true
            if (preRunAction != null) {
                preRunAction!!.run()
            }
            buttonAction.accept(Runnable {
                if (postRunAction != null) {
                    postRunAction!!.run()
                }
                button.isDisable = false
            })
        }
        button.defaultButtonProperty()
            .bind(
                Bindings.createBooleanBinding(
                    { (textField.isFocused && textField.text.isNotEmpty()) },
                    textField.focusedProperty(),
                    textField.textProperty()
                )
            )
        children.addAll(promptOf(prompt), textField, button)
    }

    /**
     * Decorator to add an action to be run on the FXAT BEFORE the
     * action Consumer is invoked.
     *
     * @param preRunAction Runnable to be performed before action consumer is invoked
     * @return
     */
    fun withPreRunAction(preRunAction: Runnable?): TextFieldButton {
        this.preRunAction = preRunAction
        return this
    }

    /**
     * Decorator to add an action to be run on the FXAT AFTER the action
     * consumer has been invoked
     *
     * @param postRunAction
     * @return
     */
    fun withPostRunAction(postRunAction: Runnable?): TextFieldButton {
        this.postRunAction = postRunAction
        return this
    }
}
