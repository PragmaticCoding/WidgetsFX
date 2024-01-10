package ca.pragmaticcoding.widgetsfx.dirtyfx

import javafx.beans.property.LongPropertyBase


class DirtyLongProperty(
    private val bean: Any?,
    private val name: String,
    initialValue: Long? = null
) :
    LongPropertyBase(), DirtyProperty<Long> {
    override fun getName() = name
    override fun getBean() = bean
    override val base = DirtyPropertyBase(this.asObject())

    init {
        initialValue?.let { super.setValue(it) }
        rebase()
    }

    constructor() : this(null, "", null)
    constructor(initialValue: Long) : this(null, "", initialValue)
}
