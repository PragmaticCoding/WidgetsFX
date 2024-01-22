@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ca.pragmaticcoding.widgetsfx.controls

import ca.pragmaticcoding.widgetsfx.layouts.labels.dataOf
import ca.pragmaticcoding.widgetsfx.layouts.labels.promptOf
import ca.pragmaticcoding.widgetsfx.layouts.textfields.decimalField
import ca.pragmaticcoding.widgetsfx.layouts.textfields.integerField
import ca.pragmaticcoding.widgetsfx.layouts.textfields.stringField
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableStringValue
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.Spinner
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane

/**
 * A GridPane with two columns, the left column contains labels (Text items)
 * which are right justified, and the right column contains nodes to
 * display or input data.
 */
class TwoColumnGridPane : GridPane() {
    init {
        val labelColumn = ColumnConstraints()
        labelColumn.halignment = HPos.RIGHT
        val dataColumn = ColumnConstraints()
        dataColumn.halignment = HPos.LEFT
        columnConstraints.add(labelColumn)
        columnConstraints.add(dataColumn)
        hgap = 4.0
        vgap = 2.0
        padding = Insets(8.0)
    }

    /**
     * Adds a row with text data displayed as a DataText node
     *
     * @param label        the string to put into the label Text
     * @param dataProperty a read only observable string value that is bound to the
     * DataText
     * @return the TwoColumnGridPane
     */
    fun addTextRow(label: String, dataProperty: ObservableStringValue) = addRow(label, dataOf(dataProperty))

    /**
     * A generic method to add a row with a previously defined data node
     *
     * @param label    the string to put in the label Text
     * @param dataNode the data Node to put in the right column
     * @return the TwoColumnGridPane
     */
    fun addRow(label: String, dataNode: Node) = apply {
        val row = rowCount
        add(promptOf(label), 0, row)
        add(dataNode, 1, row)
    }

    /**
     * Adds a row with a TextField for data entry in the right column
     *
     * @param label        the string to put into the label Text
     * @param dataProperty the StringProperty which will be bidirectionally
     * bound to the TextField
     * @return the TwoColumnGridPane
     */
    fun addTextFieldRow(label: String, dataProperty: StringProperty) = addRow(label, stringField(dataProperty))

    /**
     * Adds a row with a DecimalField for data entry in the right column
     *
     * @param label        the string to put into the label Text
     * @param dataProperty the Double property which will be bidirectionally bound to the DecimalField
     * @param maxWidth     the maximum width of the DecimalField
     * @return the TwoColumnGridPane
     */
    fun addDecimalFieldRow(label: String, dataProperty: Property<Number>, maxWidth: Double) =
        addRow(label, decimalField(dataProperty, maxWidth))

    /**
     * Adds a row with a IntegerField for data entry in the right column
     *
     * @param label        the string to put into the label Text
     * @param dataProperty the Integer property which will be bidirectionally bound to the IntegerField
     * @param maxWidth     the maximum width of the IntegerField
     * @return the TwoColumnGridPane
     */
    fun addIntegerFieldRow(label: String, dataProperty: Property<Int>, maxWidth: Double) =
        addRow(label, integerField(dataProperty, maxWidth))


    /**
     * Adds a row with a FixedDecimalField for data entry in the right column
     *
     * @param label        the string to put into the label Text
     * @param dataProperty the Double property which will be bidirectionally bound to the FixedDecimalField
     * @param maxWidth     the maximum width of the FixedDecimalField
     * @return the TwoColumnGridPane
     */
    fun addFixedDecimalFieldRow(
        label: String,
        dataProperty: Property<Double>,
        decimalPlaces: Int,
        maxWidth: Double
    ) = addRow(label, decimalField(dataProperty, decimalPlaces, maxWidth))

    fun addSpinnerRow(
        label: String,
        dataProperty: Property<Double>,
        min: Double,
        max: Double,
        stepSize: Double,
        maxWidth: Double
    ) = addRow(label, Spinner<Double>(min, max, dataProperty.value, stepSize).apply {
        dataProperty.bind(valueProperty())
        this.maxWidth = maxWidth
    })

    fun addSpinnerRow(
        label: String,
        dataProperty: Property<Int>,
        min: Int,
        max: Int,
        stepSize: Int,
        maxWidth: Double
    ) = addRow(label, Spinner<Int>(min, max, dataProperty.value, stepSize).apply {
        dataProperty.bind(valueProperty())
        this.maxWidth = maxWidth
    })
}
