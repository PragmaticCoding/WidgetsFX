package ca.pragmaticcoding.widgetsfx.layouts.tables

import ca.pragmaticcoding.widgetsfx.layouts.labels.promptOf
import javafx.geometry.Pos
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.VBox

/**
 * Placeholder displaying an indeterminate [ProgressIndicator] for use while a [TableView] is loading.
 */

class ProgressPlaceholder(message: String) : VBox() {
    init {
        spacing = 10.0
        children.addAll(ProgressIndicator(-1.0), promptOf(message))
        isFillWidth = true
        alignment = Pos.CENTER
    }
}
