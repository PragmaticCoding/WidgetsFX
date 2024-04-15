package ca.pragmaticcoding.widgetsfx

import javafx.scene.Parent
import javafx.scene.Scene

const val stylesheet: String = "css/widgetsfx.css"

/**
 * Extension function to add the standard widgetsfx.css stylesheet to a Scene
 */
fun Scene.addWidgetStyles(sheetName: String = stylesheet) = apply {
    object {}::class.java.getResource(sheetName)?.toString()?.let {
        stylesheets += it
    }
}


/**
 * Extension function to add the standard widgetsfx.css stylesheet to a Parent.
 * Note that this will not have any effect unless the Parent is used as the Root
 * element of a Scene.
 */
fun <T : Parent> T.addWidgetStyles() = apply {
    object {}::class.java.getResource(stylesheet)?.toString()?.let { stylesheets += it }
}