package ca.pragmaticcoding.dirtyfx

import javafx.beans.property.IntegerPropertyBase


class DirtyIntegerProperty(
    private val bean: Any?,
    private val name: String,
    initialValue: Int? = null
) :
    IntegerPropertyBase(), DirtyProperty<Int> {
    override fun getName() = name
    override fun getBean() = bean
    override val base = DirtyPropertyBase(this.asObject())

    init {
        initialValue?.let { super.setValue(it) }
        rebase()
    }

    constructor() : this(null, "", null)
    constructor(initialValue: Int) : this(null, "", initialValue)
}
