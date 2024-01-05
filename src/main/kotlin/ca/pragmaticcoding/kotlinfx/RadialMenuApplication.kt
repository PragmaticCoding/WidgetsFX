package ca.pragmaticcoding.kotlinfx

import ca.pragmaticcoding.widgetsfx.RadialMenu
import ca.pragmaticcoding.widgetsfx.RadialMenuItem
import ca.pragmaticcoding.widgetsfx.addStyleSheet
import ca.pragmaticcoding.widgetsfx.addWidgetStyles
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import org.kordamp.ikonli.javafx.FontIcon

class RadialMenuApplication : Application() {
    override fun start(stage: Stage) {
        val scene = Scene(createContent { Platform.exit() }, 640.0, 500.0).apply {
            addStyleSheet("/css/RadialMenu.css")
            addWidgetStyles()
        }
        stage.title = "RadialMenu Demo"
        stage.scene = scene
        stage.show()
    }

    private fun createContent(exitAction: () -> Unit) = BorderPane().apply {
        val menu = RadialMenu(
            RadialMenuItem(
                "This is the first",
                FontIcon("captainicon-049:52:BISQUE"),
                { println("First item selected") }),
            RadialMenuItem(
                "This is the second item in the menu",
                FontIcon("captainicon-176:52:BISQUE"),
                { println("Second item selected") }),
            RadialMenuItem("Cut!", FontIcon("captainicon-238:52:BISQUE"), { exitAction.invoke() })
        )
        menu.addMenuItem(
            RadialMenuItem(
                "This is the third",
                FontIcon("captainicon-066:52:BISQUE"),
                { println("Third item selected") }),
            RadialMenuItem(
                "This is the Fourth",
                FontIcon("captainicon-039:52:BISQUE"),
                { println("Fourth item selected") }),
            RadialMenuItem(
                "This is the fifth",
                FontIcon("captainicon-154:52:BISQUE"),
                { println("Fifth item selected") }),
            RadialMenuItem(
                "This is the Sixth",
                FontIcon("captainicon-271:52:BISQUE"),
                { println("Sixth item selected") }
            ).apply { disable.value = true }
        )
        center = menu
        padding = Insets(50.0)
        styleClass += "wrapper-region"
    }
}

fun main() {
    Application.launch(RadialMenuApplication::class.java)
}