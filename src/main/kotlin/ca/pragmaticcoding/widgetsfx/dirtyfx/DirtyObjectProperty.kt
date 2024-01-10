package ca.pragmaticcoding.widgetsfx.dirtyfx

import javafx.beans.property.ObjectPropertyBase


class DirtyObjectProperty<T>(
    private val bean: Any?,
    private val name: String,
    initialValue: T? = null
) :
    ObjectPropertyBase<T>(), DirtyProperty<T> {
    override fun getName() = name
    override fun getBean() = bean
    override val base = DirtyPropertyBase(this)

    init {
        initialValue?.let { super.setValue(it) }
        rebase()
    }

    constructor() : this(null, "", null)
    constructor(initialValue: T) : this(null, "", initialValue)
}
