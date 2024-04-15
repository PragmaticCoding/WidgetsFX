package ca.pragmaticcoding.widgetsfx.layouts.properties

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.util.Subscription
import java.util.*
import java.util.function.Function

class FlatMappedPropertyBinding<S, T>(
    container: ObservableValue<S>,
    propertyFunction: Function<in S, out Property<out T>?>?
) :
    LazyObjectBinding<T>(), Property<T> {
    private val source: ObservableValue<S>
    private val mapper: Function<in S, out Property<out T>>
    private var indirectSourceSubscription: Subscription
    private var indirectSource: Property<out T>? = null
    private val boundProperties = mutableListOf<Property<T>>()
    private var dummy = SimpleObjectProperty<String>()

    init {
        this.indirectSourceSubscription = Subscription.EMPTY
        this.source = Objects.requireNonNull(container, "source cannot be null")
        this.mapper = Objects.requireNonNull(propertyFunction, "mapper cannot be null")!!
    }

    override fun computeValue(): T? {
        val var1 = source.value
        val var2 = var1?.let { mapper.apply(it) }
        if (this.isObserved && this.indirectSource !== var2) {
            indirectSourceSubscription.unsubscribe()
            this.indirectSourceSubscription =
                var2?.let { it.subscribe(Runnable { this.invalidate() }) } ?: Subscription.EMPTY
            this.indirectSource = var2
        }
        return var2?.value
    }

    override fun observeSources(): Subscription {
        val var1 = source.subscribe(Runnable { this.invalidateAll() })
        return Subscription {
            var1.unsubscribe()
            this.unsubscribeIndirectSource()
        }
    }

    private fun invalidateAll() {
        this.unsubscribeIndirectSource()
        this.invalidate()
    }

    private fun unsubscribeIndirectSource() {
        indirectSourceSubscription.unsubscribe()
        this.indirectSourceSubscription = Subscription.EMPTY
        this.indirectSource = null
    }

    override fun bind(p0: ObservableValue<out T>?) {
        TODO("Not yet implemented")
    }

    override fun getBean(): Any {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun setValue(p0: T) {
        TODO("Not yet implemented")
    }

    override fun unbind() {
        TODO("Not yet implemented")
    }

    override fun isBound(): Boolean {
        TODO("Not yet implemented")
    }

    override fun unbindBidirectional(p0: Property<T>?) {
        TODO("Not yet implemented")
    }

    override fun bindBidirectional(p0: Property<T>?) {
        TODO("Not yet implemented")
    }
}
