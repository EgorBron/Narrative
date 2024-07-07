package net.blusutils.narrativelib.actor

import java.util.*

/**
 * Base class for all actors
 * @param name Name of the actor
 * @constructor Creates an actor with the specified name
 */
open class AbstractActor(open var name: String) {

    companion object {
        private val actorsCache = mutableMapOf<String, AbstractActor>()
    }

    /**
     * Unique identifier of the actor
     */
    lateinit var uuid: UUID
        private set

    init {
        if (actorsCache.containsKey(name)) {
            uuid = actorsCache[name]!!.uuid
        } else {
            uuid = UUID.randomUUID()
            actorsCache[name] = this
        }
    }
}