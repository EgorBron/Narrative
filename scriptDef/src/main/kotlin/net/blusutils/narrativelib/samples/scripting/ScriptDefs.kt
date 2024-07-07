package net.blusutils.narrativelib.samples.scripting

import net.blusutils.narrativelib.samples.narration.DefaultNarrator
import net.blusutils.narrativelib.samples.quest.rewardableQuest as rwqb
import net.blusutils.narrativelib.samples.quest.RewardableQuestBuilder
import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

/**
 * A definition of a KTScript file
 * (.narrative.kts)
 */
@KotlinScript(
    fileExtension = "narrative.kts",
    compilationConfiguration = ScriptConfiguration::class
)
open class KotlinConfig {
    var cfg = RewardableQuestBuilder()
}

// helper object
class RW {
    companion object {
        fun rewardableQuest(
            name: String,
            reward: Int = 100,
            block: RewardableQuestBuilder.() -> Unit
        ): RewardableQuestBuilder {
            // rwqb - rewardableQuestBuilder
            // see the imports
            return rwqb(name, reward, block)
        }
    }
}

object ScriptConfiguration: ScriptCompilationConfiguration({
    ide {
        acceptedLocations(ScriptAcceptedLocation.Everywhere)
    }
    defaultImports(RewardableQuestBuilder::class, RW::class)
    jvm {
        // Extract the whole classpath from context classloader and use it as dependencies
        dependenciesFromCurrentContext(wholeClasspath = true)
    }
    compilerOptions.append("-Xadd-modules=ALL-MODULE-PATH")
})