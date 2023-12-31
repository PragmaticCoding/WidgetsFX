package ca.pragmaticcoding.dirtyfx

import javafx.beans.property.BooleanPropertyBase


class DirtyBooleanProperty(
    private val bean: Any?,
    private val name: String,
    initialValue: Boolean? = null
) :
    BooleanPropertyBase(), DirtyProperty<Boolean> {
    override val base = DirtyPropertyBase(this.asObject())
    override fun getBean() = bean
    override fun getName() = name

    init {
        initialValue?.let { super.setValue(it) }
        rebase()
    }

    constructor() : this(null, "", null)
    constructor(initialValue: Boolean) : this(null, "", initialValue)
}
