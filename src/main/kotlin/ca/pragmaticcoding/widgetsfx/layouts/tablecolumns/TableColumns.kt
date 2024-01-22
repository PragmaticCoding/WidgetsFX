@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.tablecolumns

import ca.pragmaticcoding.widgetsfx.layouts.dates.formatted
import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback
import java.text.DecimalFormat
import java.time.LocalDate

infix fun <S, T> TableColumn<S, T>.withMinWidth(width: Double) = apply { minWidth = width }
infix fun <S, T> TableColumn<S, T>.withMaxWidth(width: Double) = apply { maxWidth = width }
infix fun <S, T> TableColumn<S, T>.withFixedWidth(width: Double) = apply {
    minWidth = width
    maxWidth = width
}

infix fun <S, T> TableColumn<S, T>.withValueFactory(valueCallback: Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>>) =
    apply {
        cellValueFactory = valueCallback
    }

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
            styleClass.add("data-text-cell")
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
            styleClass.add("data-text")
            textProperty().bind(
                Bindings.createStringBinding({ itemProperty().value?.let { df.format(it) } ?: "" }, itemProperty())
            )
            graphic = null
            alignment = Pos.CENTER_RIGHT
        }
    }
}

class IntegerColumn<TableModel>(
    columnHeading: String = "",
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
            styleClass.add("data-text")
            textProperty()
                .bind(Bindings.createStringBinding({ itemProperty().value?.toString() ?: "" }, itemProperty()))
            graphic = null
            alignment = Pos.CENTER_RIGHT
        }
    }
}

class TextColumn<TableModel>(
    columnHeading: String = "",
    valueCallback: Callback<CellDataFeatures<TableModel, String>, ObservableValue<String>>? = null
) : TableColumn<TableModel, String>(columnHeading) {

    init {
        setCellFactory { TextCell() }
        valueCallback?.let { cellValueFactory = it }
    }

    class TextCell<TableModel> : TableCell<TableModel, String>() {

        init {
            styleClass.add("data-text-cell")
            textProperty().bind(itemProperty())
            isWrapText = true
            graphic = null
        }
    }
}