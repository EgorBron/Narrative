/* TODO: move this script to somewhere, so it would be executed and highlighted by the IDEA */

import net.blusutils.narrativelib.actor.AbstractActor
import net.blusutils.narrativelib.samples.scripting.RW.Companion.rewardableQuest
import net.blusutils.narrativelib.storage.FactDB

class MyActor(override var name: String) : AbstractActor(name)

val theIt = MyActor("The It")

cfg = rewardableQuest("Hello, new world!", 10) {
    narrate {
        - "Welcome to the new world!"

        ! "NarrativeLib_MainRewardableQuest_Welcomed"

        "Me" - "Who is that?"

        theIt * "That is not important."
        theIt - "But who are you?"

        choice("Who are you: ", "Human", "Robot") {
            onOption("Human") {
                kt {
                    FactDB.add("NarrativeLib_MainRewardableQuest_Option", "human")
                }
                "???" - "Nice to see you. Human..."
            }
            onOption("Robot") {
                "???" - "Beep bop."
            }
            onDefault {
                "???" - "Can't understand you."
            }
        }
    }
}

