package ca.pragmaticcoding.widgetsfx.dirtyfx

import javafx.beans.property.DoublePropertyBase

/**
 * Provides SimpleDoubleProperty functionality with support for flagging changes from a baseline value.
 * See [DirtyPropertyBase] for details about dirty property functions such as reset() and rebase()
 * @param bean The object to which this property is related
 * @param name The name of the property
 * @param initialValue The initial value contained in the property
 */
class DirtyDoubleProperty(
    private val bean: Any?,
    private val name: String,
    initialValue: Double? = null
) :
    DoublePropertyBase(), DirtyProperty<Double> {

    override val base = DirtyPropertyBase<Double>(this.asObject())
    override fun getBean() = bean
    override fun getName() = name

    /**
     * Unitialized constructor with no "bean" information
     */
    constructor() : this(null, "", null)

    /**
     * Constructor without any "bean" information, but with an initial value
     * @param initialValue The initial value contained in the property
     */
    constructor(initialValue: Double) : this(null, "", initialValue)

    init {
        initialValue?.let { super.setValue(it) }
        rebase()
    }
}
