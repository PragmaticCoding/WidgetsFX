@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.mvci

abstract class MvciInteractor<ViewModelClass> protected constructor(protected val viewModel: ViewModelClass) {
    /**
     * Method designed to be run in a background thread called by the
     * ScreenController load() method from inside a Task call() method.
     * Ideally, this method should contain all the initialization of
     * Domain objects from external API's and the persistence layer
     */
    abstract fun fetchData()

    /**
     * Method designed to be run on the FXAT to load data received via
     * the fetchData() method into the ViewModel.  This method is called
     * from the load() method of the ScreenController via the setOnSucceeded()
     * method of a Task.
     */
    abstract fun updateModel()
}
