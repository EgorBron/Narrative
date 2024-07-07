import net.blusutils.narrativelib.actor.AbstractActor
import net.blusutils.narrativelib.narration.NarrationManager
import net.blusutils.narrativelib.samples.narration.buildNarration
import net.blusutils.narrativelib.samples.data.ActorMessage
import net.blusutils.narrativelib.samples.quest.rewardableQuest
import net.blusutils.narrativelib.samples.scripting.KotlinConfig
import net.blusutils.narrativelib.samples.scripting.RW
import net.blusutils.narrativelib.storage.FactDB
import java.io.FileNotFoundException
import java.nio.charset.Charset
import kotlin.script.experimental.api.EvaluationResult
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost
import kotlin.script.experimental.jvmhost.createJvmCompilationConfigurationFromTemplate

/**
 * Read a string resource included in the project JAR
 * @param name Name or path of the resource to read (relative to the /resources/ directory)
 * @return The string content of the resource
 * @throws FileNotFoundException if the resource cannot be found
 */
fun readResource(name: String): String {
    return RW::class.java.classLoader.getResource(name)?.openStream()
        ?.readAllBytes()?.toString(Charset.forName("utf-8")) ?: throw FileNotFoundException("Cannot find resource $name")
}

/**
 * Evaluate a string as a Kotlin script and return the result
 * @param str The script source string
 * @return The evaluation result ([ResultWithDiagnostics]<[EvaluationResult]>)
 * @throws Exception if the script cannot be evaluated
 */
fun evaulate(str: String): ResultWithDiagnostics<EvaluationResult> {
    val scriptConf = createJvmCompilationConfigurationFromTemplate<KotlinConfig>()
    val evalResult = BasicJvmScriptingHost().eval(str.toScriptSource(), scriptConf, null)
    return evalResult
}

/**
 * An example class that implements the [AbstractActor]
 */
class MyActor(override var name: String, val age: UInt) : AbstractActor(name)

val theIt = MyActor("The It", UInt.MAX_VALUE)

/**
 * An example quest definition.
 * Why it is assigned to the `cfg` variable?
 * Because someone just copy/pasted it from a script and forgot to rename it :)
 */
var cfg = rewardableQuest("Hello, new world!", 10) {
    /* TBD
    -- next(uuid) to jump to a quest/narration,
    -- input(message, fact) to store a fact from the input (just like readln()),
    -- end() to end the quest (aka fail),
    -- blocks for quest setup and event handling,
    -- scope for deferred blocks,
    -- deferred variables (val choice by deferredQuestVar("raceChoice")),
    -- deferred syntax (like deferredIf(condition) { ... }, where condition is a deferred boolean)
    */

//    setup {
//        TODO()
//        defaultSelfActor = "Me"
//        defaultActor = "The It"
//    }

    // The `narrate` block is used to define the narration (storyboard) of the quest
    // For now, you can define messages and choices
    narrate {
        // Unary minus/plus operator is used to add a message to the narration
        // By default, the message will belong to an unknown actor ("...")
        - "Welcome to the new world!"

        // "Narrative" introduces its own database for all data you could set through the narration
        // This is called `FactDB`, and you can use it via:
        //
        // FactDB.add(fact, value) or FactDB.remove(fact),
        // !"Fact String", deferred shorthand for method above (will swap the fact value, resolves at story run time)
        ! "NarrativeLib_MainRewardableQuest_Welcomed" // TODO change return type to FactRef

        // in the future (when Kotlin compiler is fixed),
        // variable increment (++) or decrement (--) will be used to store facts at run time
        //++ ""

        // Via the `-` operator (or `*`) you can prefix the message with an actor name
        "Me" - "Who is that?"


        // Like in Ren'Py, you can create a global actor definition, and then use it in the narration
        // You can derive [AbstractActor] class to create your actor definitions,
        // but it will have sense only in story run implementation
        theIt * "That is not important."
        theIt - "But who are you?"

        choice("Who are you: ", "Human", "Robot") {
            // By default, options handled recursively
            onOption("Human") {
                // By the way, you can execute any Kotlin code at both build and run time
                // For run time expressions, you should use "deferred" block (`kt { ... }`)
                kt {
                    // As you remember, the shorter way to store a fact is to use !"fact".
                    // But manual way guarantees exactly only fact value set/removal, not the swapping.
                    // And also (for now) `Any` value can be stored only via manual call to FactDB
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

/**
 * Example story processing function
 * @param narr Implementation of the [NarrationManager] interface based on [ActorMessage] message model
 */
fun processIt(narr: NarrationManager<ActorMessage>) {
    // We will iterate over the story entries
    narr.story.forEach { entry ->

        // [ActorMessage] model has a `type` property,
        // which is used to determine what kind of message it is.
        when (entry.type) {

            // In case of a Message, we just print a message content and actor name.
            // Also, we print the message and actor UUIDs, because this is a sample code, and I need to show them to you.
            ActorMessage.Type.Message -> {
                var actorPrefix = entry.actor.name
                // if it is a MyActor, we will print its age, cus why not?
                if (entry.actor is MyActor)
                    actorPrefix += " (${(entry.actor as MyActor).age})"
                actorPrefix += ": "
                print(actorPrefix)
                println(" (actor: ${entry.actor.uuid}; msg: ${entry.uuid})")

                println(entry.message)
                println("-".repeat(50)) // delimiter :)
            }

            // Ask user for input
            ActorMessage.Type.Input -> TODO()

            // Select from a list of options
            ActorMessage.Type.Selection -> {
                // First of all, we print options and the selection prompt
                println(entry.extra.keys.filter { it.isNotBlank() }.joinToString())
                println(entry.message)

                // Then we ask for user input
                val input = readlnOrNull() ?: ""
                // Now we need some checks to make sure that the input is valid
                val _selected = entry.extra.filter { it.key == input }.toList()

                // Check that the input has exactly one or none matching options
                // Sorry, but I forgot to implement this at story build time :(
                if (_selected.size > 1)
                    throw CloneNotSupportedException("Multiple options for the same variant")

                var selected = _selected.firstOrNull()

                // Handle the default option
                if (selected == null || selected.first == "")
                    // If you can restrict option select to a certain ones,
                    // you can omit `onDefault` block and default option processing
                    selected = "" to entry.extra.getOrDefault("", buildNarration {
                        +"Invalid input"
                    })

                // Then process [NarrationManager] in a selected option recursively.
                // And since it recursively calls itself, you should not nest multiple [onOption] blocks to avoid any issues
                processIt(selected.second)
            }

            // Execute a deferred block (any Kotlin code that executes at story run time)
            ActorMessage.Type.Deferred -> {
                entry.action()
            }

            // Jump to another quest
            ActorMessage.Type.Jump -> {
                TODO()
            }

            // End the quest
            ActorMessage.Type.End -> {
                // TODO
                println("Quest ended")
                return
            }
        }
    }
}

fun main() {
    println("NarrativeLib showcase")
    println()
    println("quest log:")
    processIt(cfg.narrate)
    println()
    println("fact db:")
    FactDB.getView().forEach { (k, v) ->
        println("$k: ${v ?: "\b\b"}")
    }
}