//@file:Suppress("unused")

package ca.pragmaticcoding.widgetsfx.layouts.buttons

import javafx.concurrent.Task
import javafx.event.EventHandler
import java.util.function.Consumer

/**
 * Utility class to simplify creation of background actions to
 * support Action type screen elements like Buttons.  Ideally,
 * the ButtonAction would be defined in the Controller and passed
 * to the ViewBuilder through its constructor.
 *
 *
 * The view would call this Consumer inside the onAction event
 * handler for a Button.  Any pre-action GUI actions would happen
 * first, then the accept() method would be called, passing a
 * Runnable containing the post action GUI events.
 *
 *
 * For instance, the Button's event handler might disable the Button
 * to prevent subsequent triggering of the handler while the
 * background task was running, then create a Runnable which enables
 * the Button and pass it to the accept() method of the ButtonAction.
 */

/**
 * Constructor to create both a background job, and a job to run on
 * the FXAT but outside the layout code after completion of the
 * background task.
 *
 * @param backgroundJob    Runnable to execute in a background task
 * @param postNonGuiAction Non-GUI related code to run on the FXAT
 */
class ButtonAction(private val backgroundJob: () -> Unit, private val postNonGuiAction: () -> Unit = {}) :
    Consumer<() -> Unit> {

    /**
     * Executes background and FXAT Runnables, then executes the
     * Runnable passed here on the FXAT.
     *
     * @param postGuiAction Runnable to execute after completion of the
     * background task.
     */
    override fun accept(postGuiAction: () -> Unit) {
        val backgroundTask: Task<Void> = object : Task<Void>() {
            override fun call(): Void? {
                backgroundJob.invoke()
                return null
            }
        }
        backgroundTask.onSucceeded = EventHandler {
            postNonGuiAction.invoke()
            postGuiAction.invoke()
        }
        val backgroundThread = Thread(backgroundTask)
        backgroundThread.isDaemon = true
        backgroundThread.start()
    }
}
