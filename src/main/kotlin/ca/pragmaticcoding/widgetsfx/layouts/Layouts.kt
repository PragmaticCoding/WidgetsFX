@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts

import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.css.PseudoClass
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox


/**
 * Standard testing styles defined in widgetsfx.css.  These place coloured borders
 * around the Regions and set a background colour.
 * These styleclasses are intended to make it easier to understand the extents of regions
 * in layouts when designing layouts.  They can easily be added to a Region via Node.testStyleAs()
 *
 * @see testStyleAs
 *
 */
enum class TestStyle(val selector: String) {
    BLUE("test-blue"), RED("test-red"), GREEN("test-green")
}

/**
 *  Extension function to add one of the standard testing styles defined in widgetsfx.css
 *  to a Node.  These styles are intended to make it easier to understand how layout elements
 *  are related to one another and their extents when designing GUI screens.
 *
 *  @param nodeStyle The testing style to be applied to the Node
 */
infix fun <T : Node> T.testStyleAs(nodeStyle: TestStyle) = apply { styleClass += nodeStyle.selector }

/**
 * Extension function to add a stylesheet to a Scene
 *
 * @receiver Scene
 * @param sheetName The filename of the stylesheet to add
 */
fun Scene.addStyleSheet(sheetName: String) = apply {
    object {}::class.java.getResource(sheetName)?.toString()?.let { stylesheets += it }
}

fun Scene.addStyleSheet(theObject: Any, sheetName: String) = apply {
    theObject::class.java.getResource(sheetName)?.toString()?.let { stylesheets += it }
}


/**
 * Infix extension function to quickly add padding to a Region with all sides padded to the same amount.
 *
 * @param padSize The amount of padding to apply equally to all sides of the Region
 */
infix fun <T : Region> T.padWith(padSize: Double): T = apply { padding = Insets(padSize) }

infix fun <T : Region> T.minWidthOf(size: Double): T = apply { minWidth = size }
infix fun <T : Region> T.minHeightOf(size: Double): T = apply { minHeight = size }
infix fun <T : Region> T.maxWidthOf(size: Double): T = apply { maxWidth = size }
infix fun <T : Region> T.maxHeightOf(size: Double): T = apply { maxHeight = size }
infix fun <T : Region> T.prefWidthOf(size: Double): T = apply { prefWidth = size }
infix fun <T : Region> T.prefHeightOf(size: Double): T = apply { prefHeight = size }
infix fun <T : Node> T.visibilityOf(visibility: Boolean): T = apply { isVisible = visibility }
infix fun <T : Node> T.isHidden(isHidden: Boolean): T = apply {
    isVisible = !isHidden
    isManaged = !isHidden
}

infix fun <T : Node> T.bindHidden(hiddenProperty: ObservableBooleanValue): T = apply {
    visibleProperty().bind(hiddenProperty)
    managedProperty().bind(hiddenProperty)
}

/**
 * Infix extension function to add a styleclass selector to a Node.
 *
 * @param newStyleClass The selector to apply to the Node
 */
infix fun <T : Node> T.addStyle(newStyleClass: String): T = apply { styleClass += newStyleClass }

/**
 *  Infix extension function to specify the alignment of on HBox
 *
 *  @receiver HBox
 *
 *  @param pos The Pos alignment to apply to the HBox
 */

infix fun HBox.alignTo(pos: Pos): HBox = apply { alignment = pos }
infix fun VBox.alignTo(pos: Pos): VBox = apply { alignment = pos }

/**
 * Operator to add a Node to a Pane or Pane subclass
 *
 * @param newChild The Node to add to the Pane
 *
 */
operator fun Pane.plusAssign(newChild: Node) {
    children += newChild
}

/**
 * Decorator function to "bind" an external property to a PseudoClass such that
 * changes in the external property will trigger Node.pseudoClassStateChanged()
 *
 * @param pseudoClass The PseudoClass to connect to this property
 * @param property The external binary property to monitor for changes
 */

fun <T : Node> T.bindPseudoClass(pseudoClass: PseudoClass, property: ObservableBooleanValue): T = apply {
    pseudoClassStateChanged(pseudoClass, property.value)
    property.addListener(InvalidationListener {
        pseudoClassStateChanged(pseudoClass, property.value)
        val fred: SimpleBooleanProperty
    })
}

infix fun <T : Node> T.addToPane(pane: Pane): T = also { pane.children += it }





