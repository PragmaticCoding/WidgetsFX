@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.tablecolumns

import ca.pragmaticcoding.widgetsfx.layouts.dates.formatted
import ca.pragmaticcoding.widgetsfx.layouts.labels.LabelStyle
import ca.pragmaticcoding.widgetsfx.layouts.labels.styleAs
import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback
import java.text.DecimalFormat
import java.time.LocalDate

/**
 * Infix decorator function to set the minimum width of a [TableColumn]
 * @param width Double value to set to the minimum width of the column
 */
infix fun <S, T> TableColumn<S, T>.withMinWidth(width: Double) = apply { minWidth = width }

/**
 * Infix decorator function to set the maximum width of a [TableColumn]
 * @param width Double value to set to the maximum width of the column
 */
infix fun <S, T> TableColumn<S, T>.withMaxWidth(width: Double) = apply { maxWidth = width }

/**
 * Infix decorator function to effectively set fixed width of a [TableColumn]
 * by setting the minWidth and maxWidth properties to the same value
 * @param width Double value to set to the width of the column
 */
infix fun <S, T> TableColumn<S, T>.withFixedWidth(width: Double) = apply {
    minWidth = width
    maxWidth = width
}

/**
 * Infix decorator to establish the ValueFactory for a [TableColumn]
 * @param valueCallback The Callback function to create the [ObservableValue] for the [TableColumn] cells
 */
infix fun <S, T> TableColumn<S, T>.withValueFactory(valueCallback: Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>>) =
    apply {
        cellValueFactory = valueCallback
    }

/**
 * Infix decorator function to set the maximum width of a [TableColumn]
 * @param heading Double value to set to the maximum width of the column
 */
infix fun <S, T> TableColumn<S, T>.withHeading(heading: String) = apply { text = heading }

/**
 * Utility customized [TableColumn] to hold [LocalDate] values.
 * @param columnHeading String heading for the column.  Use "" for no heading
 * @param valueCallback Optional Callback to create the [ObservableValue] for the cells
 */
class DateColumn<TableModel>(
    columnHeading: String,
    valueCallback: Callback<CellDataFeatures<TableModel, LocalDate>, ObservableValue<LocalDate>>? = null
) : TableColumn<TableModel, LocalDate>(columnHeading) {

    init {
        minWidth = 80.0
        maxWidth = 80.0
        setCellFactory { DateCell() }
        cellValueFactory = valueCallback
    }

    class DateCell<TableModel> : TableCell<TableModel, LocalDate>() {

        init {
            styleAs(LabelStyle.DATA)
            textProperty().bind(
                Bindings.createStringBinding(
                    { itemProperty().value?.formatted() ?: "" },
                    itemProperty()
                )
            )
            graphic = null
            alignment = Pos.CENTER_RIGHT
        }
    }
}

/**
 * Utility customized [TableColumn] to hold formatted, right justified decimal values.
 * @param columnHeading String heading for the column.  Use "" for no heading
 * @param valueCallback Optional Callback to create the [ObservableValue] for the cells
 * @param formatString Optional formatting string to convert values, defaults to a fixed two decimal place value
 */
class DecimalColumn<TableModel>(
    columnHeading: String,
    valueCallback: Callback<CellDataFeatures<TableModel, Double>, ObservableValue<Double>>? = null,
    private var formatString: String = "#.00"
) : TableColumn<TableModel, Double>(columnHeading) {

    init {
        setCellFactory { DecimalCell(DecimalFormat(formatString)) }
        cellValueFactory = valueCallback
    }

    fun withFormat(formatString: String) = apply {
        this.formatString = formatString
        setCellFactory { DecimalCell(DecimalFormat(formatString)) }
    }

    class DecimalCell<TableModel>(private var df: DecimalFormat = DecimalFormat("#.00")) :
        TableCell<TableModel, Double>() {

        init {
            styleAs(LabelStyle.DATA)
            textProperty().bind(
                Bindings.createStringBinding({ itemProperty().value?.let { df.format(it) } ?: "" }, itemProperty())
            )
            graphic = null
            alignment = Pos.CENTER_RIGHT
        }
    }
}

/**
 * Utility customized [TableColumn] to hold formatted, right justified integer values.
 * @param columnHeading String heading for the column.  Use "" for no heading
 * @param valueCallback Optional Callback to create the [ObservableValue] for the cells
 */
class IntegerColumn<TableModel>(
    columnHeading: String,
    valueCallback: Callback<CellDataFeatures<TableModel, Int>, ObservableValue<Int>>? = null
) : TableColumn<TableModel, Int>(columnHeading) {
    init {
        setCellFactory { IntegerCell() }
        valueCallback?.let { cellValueFactory = it }
    }

    fun withPreferredWidth(width: Double): IntegerColumn<TableModel> {
        prefWidth = width
        return this
    }

    class IntegerCell<TableModel> : TableCell<TableModel, Int>() {

        init {
            styleAs(LabelStyle.DATA)
            textProperty()
                .bind(Bindings.createStringBinding({ itemProperty().value?.toString() ?: "" }, itemProperty()))
            graphic = null
            alignment = Pos.CENTER_RIGHT
        }
    }
}

/**
 * Utility customized [TableColumn] to hold bound [String] values.
 * @param columnHeading String heading for the column.  Use "" for no heading
 * @param valueCallback Optional Callback to create the [ObservableValue] for the cells
 */
class TextColumn<TableModel>(
    columnHeading: String,
    valueCallback: Callback<CellDataFeatures<TableModel, String>, ObservableValue<String>>? = null
) : TableColumn<TableModel, String>(columnHeading) {

    init {
        setCellFactory { TextCell() }
        valueCallback?.let { cellValueFactory = it }
    }

    class TextCell<TableModel> : TableCell<TableModel, String>() {

        init {
            styleAs(LabelStyle.DATA)
            textProperty().bind(itemProperty())
            isWrapText = true
            graphic = null
        }
    }
}