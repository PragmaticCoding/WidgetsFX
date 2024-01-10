package ca.pragmaticcoding.widgetsfx.dirtyfx

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.StringPropertyBase


class DirtyStringProperty(
    private val bean: Any?,
    private val name: String,
    initialValue: String? = null
) :
    StringPropertyBase(), DirtyProperty<String> {
    override val base = DirtyPropertyBase<String>(this)
    override fun getBean() = bean
    override fun getName() = name

    init {
        initialValue?.let {
            super.setValue(it)
            rebase()
        }
        val fred: SimpleDoubleProperty
    }

    constructor() : this(null, "", null)
    constructor(initialValue: String) : this(null, "", initialValue)
}
