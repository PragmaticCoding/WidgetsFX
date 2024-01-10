package ca.pragmaticcoding.widgetsfx.dirtyfx

import javafx.beans.property.BooleanPropertyBase

/**
 * Provides SimpleBooleanProperty functionality with support for flagging changes from a baseline value.
 * See [DirtyPropertyBase] for details about dirty property functions such as reset() and rebase()
 * @param bean The object to which this property is related
 * @param name The name of the property
 * @param initialValue The initial value contained in the property
 */
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
