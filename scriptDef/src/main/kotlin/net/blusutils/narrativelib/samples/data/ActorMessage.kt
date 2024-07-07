package net.blusutils.narrativelib.samples.data

import net.blusutils.narrativelib.actor.AbstractActor
import net.blusutils.narrativelib.narration.NarrationManager
import java.util.*

/**
 * An example class of a message.
 * @param uuid UUID of the message
 * @param actor Actor who sent the message
 * @param message Message content/phrase
 * @param type Type of the message (see [Type])
 * @param extra Extra data for this message (used in selections)
 * @param action Action to be executed when this message is shown
 */
data class ActorMessage(
    val uuid: UUID,
    var actor: AbstractActor,
    var message: String = "Hello, world!",
    var type: Type = Type.Message,
    var extra: MutableMap<String, NarrationManager<ActorMessage>> = mutableMapOf(),
    var action: () -> Any? = {}
) {
    /**
     * Types of messages that can be sent.
     * @property Message Plain message
     * @property Input Input request
     * @property Selection Selection request
     * @property Deferred Deferred code execution
     * @property Jump Jump to another quest/story
     * @property End End of the quest/story
     */
    enum class Type {
        Message,
        Input,
        Selection,
        Deferred,
        Jump,
        End
    }
}