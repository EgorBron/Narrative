package net.blusutils.narrativelib.samples.quest

import net.blusutils.narrativelib.quest.QuestBuilder
import net.blusutils.narrativelib.samples.data.ActorMessage

/**
 * A default quest builder implementation for quests with "rewards"
 */
class RewardableQuestBuilder : QuestBuilder<ActorMessage>() {
    /**
     * Reward of the quest. You deserve it.
     */
    var reward = 100

    /**
     * Build the quest
     */
    operator fun invoke(scope: RewardableQuestBuilder.() -> Unit): RewardableQuestBuilder {
        scope()
        return this
    }
}