package net.blusutils.narrativelib.samples.narration.selector

import net.blusutils.narrativelib.actor.AbstractActor
import net.blusutils.narrativelib.samples.data.ActorMessage
import net.blusutils.narrativelib.narration.NarrationManager
import java.lang.UnsupportedOperationException
import java.util.*


/**
 * A sample wrapper around a [NarrationManager.SelectorWrapper] that allows for the creation of
 * a string selector.
 * @param selectorUUID Unique identifier of the selector.
 * @param story List of original story messages.
 * @param parent Parent narration manager.
 * @param createNewParent Function that creates a new narration manager of the same type for the given option.
 */
class DefaultSelector(
    val selectorUUID: UUID,
    override val story: MutableList<ActorMessage>,
    override val parent: NarrationManager<ActorMessage>,
    val createNewParent: (String) -> NarrationManager<ActorMessage>
) : NarrationManager.SelectorWrapper<ActorMessage>() {

    /**
     * Creates a new [NarrationManager] that will be used as a selector option.
     * @param option Name of the option.
     * @param block Function that will be used to create the new narration manager.
     * @return New narration manager.
     */
    override fun onOption(
        option: String,
        block: NarrationManager<ActorMessage>.() -> Unit
    ): NarrationManager<ActorMessage> {
        val selector = parent.story.find { it.uuid == selectorUUID }
        val newP = createNewParent(option)
        selector?.extra?.set(option, newP)
        return newP.apply {
            block()
        }
    }

    /**
     * Creates a new [NarrationManager] that will be used as the default selector option.
     * @param block Function that will be used to create the new narration manager.
     * @see onOption
     */
    override fun onDefault(block: NarrationManager<ActorMessage>.() -> Unit): NarrationManager<ActorMessage> {
        return onOption("", block)
    }

    override fun String.unaryPlus(): ActorMessage {
        return parent.run {
            +this@unaryPlus
        }
    }

    override fun String.unaryMinus(): ActorMessage {
        return parent.run {
            -this@unaryMinus
        }
    }

    override fun String.minus(phrase: String): ActorMessage {
        return parent.run {
            this@minus - phrase
        }
    }

    override fun AbstractActor.minus(phrase: String): ActorMessage {
        return parent.run {
            this@minus - phrase
        }
    }

    override fun String.times(phrase: String): ActorMessage {
        return parent.run {
            this@times - phrase
        }
    }

    override fun AbstractActor.times(phrase: String): ActorMessage {
        return parent.run {
            this@times - phrase
        }
    }

    override fun String.not(): Triple<String, Any?, Boolean> {
        parent.run {
            return !this@not
        }
    }

    /**
     * Please do not use this method.
     */
    override fun choice(
        prompt: String,
        vararg options: String,
        block: NarrationManager.SelectorWrapper<ActorMessage>.() -> Unit
    ): NarrationManager.SelectorWrapper<ActorMessage> {
        throw UnsupportedOperationException("Creating selections outside of an onOption scope is forbidden")
    }

    override fun kt(block: () -> Any?) {
        parent.kt(block)
    }
}