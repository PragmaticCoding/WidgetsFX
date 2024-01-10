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

class LabelBox3Application : Application() {

    val text =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum nibh " + "velit, elementum eu ipsum a, porttitor iaculis mauris. Nunc arcu diam, congue at luctus " + "sit amet, gravida nec eros. Mauris fringilla tortor quam, eu aliquam libero egestas " + "quis. Etiam pharetra faucibus metus, eget gravida lectus lobortis nec. Aliquam " + "porttitor, urna ac mattis pulvinar, ex ante feugiat dolor, in venenatis justo tellus " + "sed magna. Nullam et velit quis lorem pharetra vestibulum. Maecenas sollicitudin " + "dictum accumsan. Aliquam vel dui mi. Aliquam scelerisque metus non sollicitudin " + "tristique. Integer consectetur dui at velit finibus, efficitur congue nunc sagittis. " + "Quisque congue gravida diam, eget pharetra urna bibendum non. Donec vestibulum leo " + "est, nec accumsan purus sagittis vitae. "

    override fun start(stage: Stage) {
        val scene = Scene(createContent(), 500.0, 600.0).apply {
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
        val box1 = LabelledPane("Lorem Ipsum Sample").apply {
            getContent().add(
                Label(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum nibh "
                            + "velit, elementum eu ipsum a, porttitor iaculis mauris. Nunc arcu diam, congue at luctus "
                            + "sit amet, gravida nec eros. Mauris fringilla tortor quam, eu aliquam libero egestas "
                            + "quis. Etiam pharetra faucibus metus, eget gravida lectus lobortis nec. Aliquam "
                            + "porttitor, urna ac mattis pulvinar, ex ante feugiat dolor, in venenatis justo tellus "
                            + "sed magna. Nullam et velit quis lorem pharetra vestibulum. Maecenas sollicitudin "
                            + "dictum accumsan. Aliquam vel dui mi. Aliquam scelerisque metus non sollicitudin "
                            + "tristique. Integer consectetur dui at velit finibus, efficitur congue nunc sagittis. "
                            + "Quisque congue gravida diam, eget pharetra urna bibendum non. Donec vestibulum leo "
                            + "est, nec accumsan purus sagittis vitae. "
                ).apply {
                    isWrapText = true
                })
        }
        val box2 = LabelledPane("Letter Triplets List in VBox").apply {
            getContent().add(VBox(10.0, Label("Abc"), Label("Xyz"), Label("DEF")))
            maxWidth = 200.0
        }
        center = VBox(20.0, box1, box2, createOtherBox())
    }

    private fun createOtherBox() = VBox().apply {
        children += Label(text).apply {
            isWrapText = true
        }
        styleClass += "test-red"
        maxWidth = 300.0
    }
}


fun main() {
    Application.launch(LabelBox3Application::class.java)
}
