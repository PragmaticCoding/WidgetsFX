package ca.pragmaticcoding.widgetsfx

import javafx.beans.binding.Bindings
import javafx.beans.binding.DoubleBinding
import javafx.beans.value.ObservableDoubleValue
import javafx.collections.FXCollections
import javafx.css.CssMetaData
import javafx.css.SimpleStyleableDoubleProperty
import javafx.css.StyleConverter
import javafx.css.StyleableDoubleProperty
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label
import javafx.scene.effect.Bloom
import javafx.scene.effect.Effect
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.shape.*
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.transform.Rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class RadialMenu(
    option1: RadialMenuItem,
    option2: RadialMenuItem,
    centre: RadialMenuItem,
    vararg radialMenuItems: RadialMenuItem
) : Pane() {

    private val options = FXCollections.observableArrayList(option1, option2)
    private val numOptions = Bindings.createIntegerBinding({ options.size }, options)
    private val innerRadiusProperty: StyleableDoubleProperty =
        SimpleStyleableDoubleProperty(INNER_RADIUS_META_DATA, 60.0)

    private val angle: DoubleBinding = Bindings.createDoubleBinding({ PI / numOptions.value }, numOptions)

    private val outerRadiusProperty: StyleableDoubleProperty =
        SimpleStyleableDoubleProperty(OUTER_RADIUS_META_DATA, 200.0)
    private val originX = Bindings.createDoubleBinding({ outerRadiusProperty.value * 1.04 }, outerRadiusProperty)
    private val originY = Bindings.createDoubleBinding({ outerRadiusProperty.value * 1.04 }, outerRadiusProperty)

    companion object CssStuff {
        val OUTER_RADIUS_META_DATA: CssMetaData<RadialMenu, Number> =
            object : CssMetaData<RadialMenu, Number>("-rm-outer-radius", StyleConverter.getSizeConverter()) {
                override fun isSettable(styleable: RadialMenu) = !styleable.outerRadiusProperty.isBound
                override fun getStyleableProperty(styleable: RadialMenu) = styleable.outerRadiusProperty
            }
        val INNER_RADIUS_META_DATA: CssMetaData<RadialMenu, Number> =
            object : CssMetaData<RadialMenu, Number>("-rm-inner-radius", StyleConverter.getSizeConverter()) {
                override fun isSettable(styleable: RadialMenu) = !styleable.innerRadiusProperty.isBound
                override fun getStyleableProperty(styleable: RadialMenu) = styleable.innerRadiusProperty
            }
        private val cssMetaDataList =
            (Region.getClassCssMetaData() + OUTER_RADIUS_META_DATA + INNER_RADIUS_META_DATA) as MutableList

        fun getClassCssMetaData() = cssMetaDataList
    }

    override fun getCssMetaData() = getClassCssMetaData()

    fun outerRadiusProperty() = outerRadiusProperty
    fun innerRadiusProperty() = innerRadiusProperty
    var outerRadius: Double
        get() = outerRadiusProperty.get()
        set(value) = outerRadiusProperty.set(value)
    var innerRadius: Double
        get() = innerRadiusProperty.get()
        set(value) = innerRadiusProperty.set(value)

    init {
        styleClass += "radial-menu"
        children += createMenuItem(0, option1)
        children += createMenuItem(1, option2)
        children += createCentre(centre)
        children += createCentreNode(centre)
        addMenuItem(*radialMenuItems)
        minHeight = 2.08 * outerRadiusProperty.value
        minWidth = 2.08 * outerRadiusProperty.value
    }

    fun addMenuItem(vararg radialMenuItems: RadialMenuItem) {
        radialMenuItems.forEach { radialMenuItem ->
            options += radialMenuItem
            children += createMenuItem(numOptions.value - 1, radialMenuItem)
        }
    }

    private fun createCentre(radialMenuItem: RadialMenuItem) = Circle().apply {
        styleClass += "circle"
        centerXProperty().bind(originX)
        centerYProperty().bind(originX)
        radiusProperty().bind(innerRadiusProperty.subtract(15.0))
        onMouseClicked = EventHandler { radialMenuItem.action.invoke() }
    }

    private fun createCentreNode(radialMenuItem: RadialMenuItem) = Label(radialMenuItem.text).apply {
        graphic = radialMenuItem.graphic
        font = Font.font("System", FontWeight.BOLD, 18.0)
        contentDisplay = ContentDisplay.BOTTOM
        style = "-fx-text-fill: BISQUE"
        this.translateXProperty().bind(originX.subtract(this.widthProperty().divide(2)))
        this.translateYProperty().bind(originY.subtract(this.heightProperty().divide(2)))
        isMouseTransparent = true
    }

    private fun createMenuItem(itemNumber: Int, radialMenuItem: RadialMenuItem) = Group().apply {
        val wedge = createShape(itemNumber)
        styleClass += "radial-menu-item"
        styleClass += "radial-menu-item-$itemNumber"
        maxWidth = outerRadiusProperty.value * 2.0
        children += wedge
        children += Label(radialMenuItem.text, radialMenuItem.graphic).apply {
            isMouseTransparent = true
            styleClass += "contents"
            maxWidthProperty().bind(xBinding(angle, outerRadiusProperty).subtract(originX).multiply(1.5))
            translateXProperty().bind(
                xBinding(
                    angle.multiply(itemNumber * 2.0).subtract(widthProperty().divide(2).divide(outerRadiusProperty)),
                    outerRadiusProperty.multiply(0.98)
                )
            )
            translateYProperty().bind(
                yBinding(
                    angle.multiply(itemNumber * 2.0).subtract(widthProperty().divide(2).divide(outerRadiusProperty)),
                    outerRadiusProperty.multiply((0.98))
                )
            )
            transforms += Rotate(((angle.value * itemNumber) / PI) * 360.0, 0.0, 0.0).apply {
                angleProperty().bind(
                    Bindings.createDoubleBinding(
                        { (this@RadialMenu.angle.value * itemNumber * 360) / PI },
                        this@RadialMenu.angle
                    )
                )
            }
        }
        wedge.onMouseEntered = EventHandler {
            toFront()
            wedge.effect = radialMenuItem.effect
        }
        wedge.onMouseExited = EventHandler { wedge.effect = null }
        wedge.onMouseClicked = EventHandler<MouseEvent> { radialMenuItem.action.invoke() }
    }

    private fun getX(origin: Double, angle: Double, radius: Double) = origin + (radius * sin(angle))
    private fun getY(origin: Double, angle: Double, radius: Double) = origin - (radius * cos(angle))

    private fun xBinding(angle: ObservableDoubleValue, radius: ObservableDoubleValue) =
        Bindings.createDoubleBinding({ getX(originX.value, angle.get(), radius.get()) }, angle, radius, originX)

    private fun yBinding(angle: ObservableDoubleValue, radius: ObservableDoubleValue) =
        Bindings.createDoubleBinding({ getY(originY.value, angle.get(), radius.get()) }, angle, radius, originY)

    private fun createShape(itemNumber: Int) = Path().apply {
        println("Sine 90: ${sin(PI / 2.0)}")
        println("Angle ${angle.value}")
        styleClass += "wedge"
        elements += MoveTo().apply {
            xProperty().bind(xBinding(angle.multiply((itemNumber * 2) - 1), outerRadiusProperty))
            yProperty().bind(yBinding(angle.multiply((itemNumber * 2) - 1), outerRadiusProperty))
        }
        elements += ArcTo().apply {
            radiusXProperty().bind(outerRadiusProperty)
            radiusYProperty().bind(outerRadiusProperty)
            isSweepFlag = true
            isLargeArcFlag = false
            xProperty().bind(xBinding(angle.multiply((itemNumber * 2) + 1), outerRadiusProperty))
            yProperty().bind(yBinding(angle.multiply((itemNumber * 2) + 1), outerRadiusProperty))
        }
        elements += LineTo().apply {
            xProperty().bind(xBinding(angle.multiply((itemNumber * 2) + 1), innerRadiusProperty))
            yProperty().bind(yBinding(angle.multiply((itemNumber * 2) + 1), innerRadiusProperty))
        }
        elements += ArcTo().apply {
            radiusXProperty().bind(innerRadiusProperty)
            radiusYProperty().bind(innerRadiusProperty)
            isSweepFlag = false
            xProperty().bind(xBinding(angle.multiply((itemNumber * 2) - 1), innerRadiusProperty))
            yProperty().bind(yBinding(angle.multiply((itemNumber * 2) - 1), innerRadiusProperty))
        }
        toBack()
    }
}

data class RadialMenuItem(
    val text: String,
    val graphic: Node,
    val action: () -> Unit,
    val effect: Effect = Bloom(0.2)
)