package ca.pragmaticcoding.widgetsfx.layouts.properties

import javafx.beans.InvalidationListener
import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ChangeListener
import javafx.util.Subscription

abstract class LazyObjectBinding<T> : ObjectBinding<T>() {
    private var subscription: Subscription? = null
    private var wasObserved = false

    override fun addListener(var1: ChangeListener<in T>) {
        super.addListener(var1)
        this.updateSubscriptionAfterAdd()
    }

    override fun removeListener(var1: ChangeListener<in T>) {
        super.removeListener(var1)
        this.updateSubscriptionAfterRemove()
    }

    override fun addListener(var1: InvalidationListener) {
        super.addListener(var1)
        this.updateSubscriptionAfterAdd()
    }

    override fun removeListener(var1: InvalidationListener) {
        super.removeListener(var1)
        this.updateSubscriptionAfterRemove()
    }

    override fun allowValidation(): Boolean {
        return this.isObserved
    }

    private fun updateSubscriptionAfterAdd() {
        if (!this.wasObserved) {
            this.subscription = this.observeSources()
            this.wasObserved = true
        }
    }

    private fun updateSubscriptionAfterRemove() {
        if (this.wasObserved && !this.isObserved) {
            subscription?.unsubscribe()
            this.subscription = null
            this.invalidate()
            this.wasObserved = false
        }
    }

    protected abstract fun observeSources(): Subscription
}
