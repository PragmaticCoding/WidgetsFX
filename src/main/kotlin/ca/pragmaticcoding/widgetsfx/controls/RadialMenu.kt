package ca.pragmaticcoding.widgetsfx.controls

import ca.pragmaticcoding.widgetsfx.labelOf
import javafx.beans.binding.Bindings
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.*
import javafx.beans.value.ObservableDoubleValue
import javafx.collections.FXCollections
import javafx.css.CssMetaData
import javafx.css.SimpleStyleableDoubleProperty
import javafx.css.StyleConverter
import javafx.css.StyleableDoubleProperty
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.effect.Bloom
import javafx.scene.effect.Effect
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.shape.*
import javafx.scene.transform.Rotate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class RadialMenu(
    menuItem1: RadialMenuModel,
    menuItem2: RadialMenuModel,
    centreMenuItem: RadialMenuModel,
    vararg additionalMenuItems: RadialMenuModel
) : Pane() {

    private val menuItems = FXCollections.observableArrayList(menuItem1, menuItem2)
    private val numItems = Bindings.createIntegerBinding({ menuItems.size }, menuItems)
    private val theta: DoubleBinding = Bindings.createDoubleBinding({ PI / numItems.value }, numItems)
    private val innerRadiusProperty: StyleableDoubleProperty =
        SimpleStyleableDoubleProperty(INNER_RADIUS_META_DATA, 60.0)
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
        children += createMenuItem(0, menuItem1)
        children += createMenuItem(1, menuItem2)
        children += createCentre(centreMenuItem)
        children += createCentreLabel(centreMenuItem)
        addMenuItem(*additionalMenuItems)
        minHeight = 2.08 * outerRadiusProperty.value
        minWidth = 2.08 * outerRadiusProperty.value
    }

    fun addMenuItem(vararg radialMenuModels: RadialMenuModel) {
        radialMenuModels.forEach { radialMenuItem ->
            menuItems += radialMenuItem
            children += createMenuItem(numItems.value - 1, radialMenuItem)
        }
    }

    private fun createCentre(radialMenuModel: RadialMenuModel) = Circle().apply {
        styleClass += "centre"
        centerXProperty().bind(originX)
        centerYProperty().bind(originX)
        radiusProperty().bind(innerRadiusProperty.subtract(15.0))
        onMouseClicked = EventHandler { radialMenuModel.action.invoke() }
        onMouseEntered = EventHandler { effect = radialMenuModel.effect }
        onMouseExited = EventHandler { effect = null }
    }

    private fun createCentreLabel(radialMenuModel: RadialMenuModel) =
        labelOf(radialMenuModel.text, "centre-contents", radialMenuModel.graphic).apply {
            this.translateXProperty().bind(originX.subtract(this.widthProperty().divide(2)))
            this.translateYProperty().bind(originY.subtract(this.heightProperty().divide(2)))
            isMouseTransparent = true
        }

    private fun createMenuItem(itemNumber: Int, radialMenuModel: RadialMenuModel) = Group().apply {
        val wedge = createShape(itemNumber)
        disableProperty().bind(radialMenuModel.disable)
        styleClass += "radial-menu-item"
        styleClass += "radial-menu-item-$itemNumber"
        maxWidth = outerRadiusProperty.value * 2.0
        children += wedge
        children += createLabel(radialMenuModel, itemNumber)
        wedge.onMouseEntered = EventHandler {
            toFront()
            wedge.effect = radialMenuModel.effect
        }
        wedge.onMouseExited = EventHandler { wedge.effect = null }
        wedge.onMouseClicked = EventHandler<MouseEvent> { radialMenuModel.action.invoke() }
    }

    private fun createLabel(radialMenuModel: RadialMenuModel, itemNumber: Int) =
        labelOf(radialMenuModel.text, "contents", radialMenuModel.graphic).apply {
            isMouseTransparent = true
            maxWidthProperty().bind(xBinding(theta, outerRadiusProperty).subtract(originX).multiply(1.5))
            translateXProperty().bind(
                xBinding(
                    theta.multiply(itemNumber * 2.0).subtract(widthProperty().divide(2).divide(outerRadiusProperty)),
                    outerRadiusProperty.multiply(0.98)
                )
            )
            translateYProperty().bind(
                yBinding(
                    theta.multiply(itemNumber * 2.0).subtract(widthProperty().divide(2).divide(outerRadiusProperty)),
                    outerRadiusProperty.multiply((0.98))
                )
            )
            transforms += Rotate(((theta.value * itemNumber) / PI) * 360.0, 0.0, 0.0).apply {
                angleProperty().bind(
                    Bindings.createDoubleBinding(
                        { (this@RadialMenu.theta.value * itemNumber * 360) / PI },
                        this@RadialMenu.theta
                    )
                )
            }
        }

    private fun getX(origin: Double, angle: Double, radius: Double) = origin + (radius * sin(angle))
    private fun getY(origin: Double, angle: Double, radius: Double) = origin - (radius * cos(angle))

    private fun xBinding(angle: ObservableDoubleValue, radius: ObservableDoubleValue) =
        Bindings.createDoubleBinding({ getX(originX.value, angle.get(), radius.get()) }, angle, radius, originX)

    private fun yBinding(angle: ObservableDoubleValue, radius: ObservableDoubleValue) =
        Bindings.createDoubleBinding({ getY(originY.value, angle.get(), radius.get()) }, angle, radius, originY)

    private fun createShape(itemNumber: Int) = Path().apply {
        styleClass += "wedge"
        elements += MoveTo().apply {
            xProperty().bind(xBinding(theta.multiply((itemNumber * 2) - 1), outerRadiusProperty))
            yProperty().bind(yBinding(theta.multiply((itemNumber * 2) - 1), outerRadiusProperty))
        }
        elements += ArcTo().apply {
            radiusXProperty().bind(outerRadiusProperty)
            radiusYProperty().bind(outerRadiusProperty)
            isSweepFlag = true
            isLargeArcFlag = false
            xProperty().bind(xBinding(theta.multiply((itemNumber * 2) + 1), outerRadiusProperty))
            yProperty().bind(yBinding(theta.multiply((itemNumber * 2) + 1), outerRadiusProperty))
        }
        elements += LineTo().apply {
            xProperty().bind(xBinding(theta.multiply((itemNumber * 2) + 1), innerRadiusProperty))
            yProperty().bind(yBinding(theta.multiply((itemNumber * 2) + 1), innerRadiusProperty))
        }
        elements += ArcTo().apply {
            radiusXProperty().bind(innerRadiusProperty)
            radiusYProperty().bind(innerRadiusProperty)
            isSweepFlag = false
            xProperty().bind(xBinding(theta.multiply((itemNumber * 2) - 1), innerRadiusProperty))
            yProperty().bind(yBinding(theta.multiply((itemNumber * 2) - 1), innerRadiusProperty))
        }
        toBack()
    }
}

class RadialMenuModel(
    initialText: String,
    initialGraphic: Node,
    val action: () -> Unit,
    isDisable: Boolean = false,
    val effect: Effect = Bloom(0.2)
) {
    val disable: BooleanProperty = SimpleBooleanProperty(isDisable)
    val text: StringProperty = SimpleStringProperty(initialText)
    val graphic: ObjectProperty<Node> = SimpleObjectProperty(initialGraphic)
}