package net.blusutils.narrativelib.samples.quest

import net.blusutils.narrativelib.samples.narration.DefaultNarrator

/**
 * Creates a new quest with "reward"
 */
fun rewardableQuest(name: String, reward: Int = 100, block: RewardableQuestBuilder.() -> Unit): RewardableQuestBuilder {
    return RewardableQuestBuilder().apply {
        this.name = name
        this.reward = reward
        this.narrate = DefaultNarrator()
        println("New quest: $name (reward: $reward)")
        block()
    }
}