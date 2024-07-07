package net.blusutils.narrativelib.quest

import net.blusutils.narrativelib.narration.NarrationManager

/**
 * An abstract quest builder, where the quest is the point in the story.
 * @param T Type of the message used in this quest
 */
abstract class QuestBuilder<T> {
    /**
     * Name of the quest
     */
    lateinit var name: String

    /**
     * Quest assignee
     */
    lateinit var assignedBy: String

    /**
     * Narration manager for this quest
     */
    lateinit var narrate: NarrationManager<T>
}