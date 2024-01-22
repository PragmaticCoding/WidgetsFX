@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.mvci

import javafx.concurrent.Task
import javafx.event.EventHandler
import javafx.scene.layout.Region
import javafx.util.Builder

interface MvciController {

    fun getView(): Region
}

abstract class MvciControllerBase<PresentationModelClass> : MvciController {
    protected abstract val viewBuilder: Builder<Region>
    protected abstract val interactor: MvciInteractor<PresentationModelClass>
    private var view: Region? = null

    protected fun load(postFetchGui: () -> Unit) {
        val fetchTask: Task<Void> = object : Task<Void>() {
            override fun call(): Void? {
                interactor.fetchData()
                return null
            }
        }
        fetchTask.onSucceeded = EventHandler {
            interactor.updateModel()
            postFetchGui.invoke()
        }
        val fetchThread = Thread(fetchTask)
        fetchThread.start()
    }

    override fun getView(): Region = view ?: viewBuilder.build()
}

typealias ActionRunner = (() -> Unit) -> Unit
