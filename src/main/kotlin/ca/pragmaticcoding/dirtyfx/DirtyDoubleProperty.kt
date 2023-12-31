package ca.pragmaticcoding.dirtyfx

import javafx.beans.property.DoublePropertyBase


class DirtyDoubleProperty(
    private val bean: Any?,
    private val name: String,
    initialValue: Double? = null
) :
    DoublePropertyBase(), DirtyProperty<Double> {

    override val base = DirtyPropertyBase<Double>(this.asObject())
    override fun getBean() = bean
    override fun getName() = name

    constructor() : this(null, "", null)
    constructor(initialValue: Double) : this(null, "", initialValue)

    init {
        initialValue?.let { super.setValue(it) }
        rebase()
    }
}
