package net.blusutils.narrativelib.narration

import net.blusutils.narrativelib.actor.AbstractActor
import java.util.*

/**
 * An interface defining everything needed to build a story
 * @param T Type of the message used in this narration
 */
interface NarrationManager<T> {

    val story: MutableList<T>

    /**
     * A wrapper for creating selections in the story
     * @param T Type of the message used in this narration
     */
    abstract class SelectorWrapper<T> : NarrationManager<T> {
        /**
         * Parent narration manager
         */
        abstract val parent: NarrationManager<T>

        /**
         * Creates a new [NarrationManager] that will be used as a selector option.
         * @param option Name of the option.
         * @param block Function that will be used to create the new narration manager.
         * @return New narration manager.
         */
        abstract fun onOption(option: String, block: NarrationManager<T>.() -> Unit): NarrationManager<T>

        /**
         * Creates a new [NarrationManager] that will be used as the default selector option.
         * @param block Function that will be used to create the new narration manager.
         * @see onOption
         */
        abstract fun onDefault(block: NarrationManager<T>.() -> Unit): NarrationManager<T>
    }

    /**
     * Builds a new or appends messages to a current [NarrationManager.story]
     * @param scope Function that will be used to create the story.
     * @return this narration manager
     */
    operator fun invoke(scope: NarrationManager<T>.() -> Unit): NarrationManager<T> {
        scope()
        return this
    }

    /**
     * Add a new message to the [NarrationManager.story]
     * @return the message
     * @see String.unaryMinus
     */
    operator fun String.unaryPlus(): T

    /**
     * Add a new message to the [NarrationManager.story]
     * @return the message
     * @see String.unaryPlus
     */
    operator fun String.unaryMinus(): T

    /**
     * Add a new message of a new named actor to the [NarrationManager.story] that says given [phrase]
     * @param phrase Phrase to add to the message
     * @return the message
     * @see AbstractActor.minus
     * @see AbstractActor.times
     * @see String.times
     */
    operator fun String.minus(phrase: String): T

    /**
     * Add a new message of a new named actor to the [NarrationManager.story] that says given [phrase]
     * @param phrase Phrase to add to the message
     * @return the message
     * @see AbstractActor.minus
     * @see AbstractActor.times
     * @see String.unaryMinus
     */
    operator fun String.times(phrase: String): T

    /**
     * Add a new message of the existing actor to the [NarrationManager.story] that says given [phrase]
     * @param phrase Phrase to add to the message
     * @return the message
     * @see AbstractActor.times
     * @see String.times
     * @see String.unaryMinus
     */
    operator fun AbstractActor.minus(phrase: String): T

    /**
     * Add a new message of the existing actor to the [NarrationManager.story] that says given [phrase]
     * @param phrase Phrase to add to the message
     * @return the message
     * @see AbstractActor.minus
     * @see String.unaryMinus
     * @see String.times
     */
    operator fun AbstractActor.times(phrase: String): T

    /**
     * Adds or removes a boolean/nullable fact to the [net.blusutils.narrativelib.storage.FactDB] implementation.
     * @return Triple of the fact, its new value, and whether it was added or removed.
     */
    operator fun String.not(): Triple<String, Any?, Boolean>

    /**
     * Creates a new [SelectorWrapper] that will be used to create a selection in the story.
     * @param prompt Prompt to show the user
     * @param options Options to select from
     * @param block Function that will be used to create the new selection wrapper
     * @return New selection wrapper
     * @see SelectorWrapper
     */
    fun choice(prompt: String, vararg options: String, block: SelectorWrapper<T>.() -> Unit): SelectorWrapper<T>

    /**
     * Executes the given [block] at story run time (after the story is built and story processor is running).
     * @param block Function to execute
     */
    fun kt(block: () -> Any?)

}