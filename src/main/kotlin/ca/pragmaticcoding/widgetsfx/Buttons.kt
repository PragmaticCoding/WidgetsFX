package ca.pragmaticcoding.widgetsfx

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ButtonBase

/**
 * Factory method to create a simple, text-only, Button with an onAction EventHandler
 *
 * @param text The Button text
 * @param handler the EventHandler for the onAction event
 * @return Button
 */
fun buttonOf(text: String, handler: EventHandler<ActionEvent>) = Button(text) addAction handler

/**
 * Infix convenience function to add an onAction EventHandler to a Button
 *
 * @param eventHandler The EventHandler for the onAction event
 */

infix fun <T : ButtonBase> T.addAction(eventHandler: EventHandler<ActionEvent>): T = apply { onAction = eventHandler }