package net.blusutils.narrativelib.samples.narration

import net.blusutils.narrativelib.actor.AbstractActor
import net.blusutils.narrativelib.narration.NarrationManager
import net.blusutils.narrativelib.samples.data.ActorMessage
import net.blusutils.narrativelib.storage.FactDB
import net.blusutils.narrativelib.samples.narration.selector.DefaultSelector
import java.util.*

/**
 * Default narration manager implementation.
 */
class DefaultNarrator : NarrationManager<ActorMessage> {
    override val story: MutableList<ActorMessage> = mutableListOf()

    override fun AbstractActor.minus(phrase: String): ActorMessage {
        val wrap = ActorMessage(
            UUID.randomUUID(),
            this,
            phrase,
        )
        return wrap.also { story.add(it) }
    }

    override fun AbstractActor.times(phrase: String): ActorMessage {
        return this - phrase
    }

    override fun String.not(): Triple<String, Any?, Boolean> {
        kt {
            if (FactDB.has(this))
                FactDB.remove(this)
            else
                FactDB.add(this)
        }
        return Triple(
            this,
            FactDB.get(this),
            FactDB.has(this)
        )
    }

    override fun String.unaryPlus(): ActorMessage {
//        val msg = ActorMessage(uuid, value, phrase)
//        parent.story.add(msg)
//        return msg
        val actor = AbstractActor("...")
        return actor - this
    }

    override fun String.unaryMinus(): ActorMessage {
        return +this
    }

    override fun String.minus(phrase: String): ActorMessage {
        val actor = AbstractActor(this)
        return actor - phrase
    }

    override fun String.times(phrase: String): ActorMessage {
        return this - phrase
    }


    override fun choice(
        prompt: String,
        vararg options: String,
        block: NarrationManager.SelectorWrapper<ActorMessage>.() -> Unit
    ): NarrationManager.SelectorWrapper<ActorMessage> {
        val selector = DefaultSelector(
            UUID.randomUUID(),
            story,
            this
        ) {
            DefaultNarrator()
        }
        return selector.apply {
            val actor = AbstractActor("?")
            val type = ActorMessage.Type.Selection
            story.add(
                ActorMessage(
                    selectorUUID,
                    actor,
                    prompt,
                    type,
                    options.associateWith {
                        parent
                    }.toMutableMap()
                )
            )
            block()
        }
    }

    override fun kt(block: () -> Any?) {
        story.add(
            ActorMessage(
                UUID.randomUUID(),
                AbstractActor("DEFERRED EXECUTION"),
                "DEFERRED EXECUTION",
                ActorMessage.Type.Deferred,
                action = block
            )
        )
    }
}