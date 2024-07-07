import net.blusutils.narrativelib.narration.NarrationManager
import net.blusutils.narrativelib.samples.data.ActorMessage
import net.blusutils.narrativelib.samples.quest.rewardableQuest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PlainStoryTests {

    @Test
    fun `plain story test`() {
        val quest = rewardableQuest("sample", 100) {
            narrate {
                - "Hello world!"
            }
        }

        assertEquals(quest.name, "sample")
        assertEquals(quest.reward, 100)
        assertIs<NarrationManager<ActorMessage>>(quest.narrate, "narrate is not a NarrationManager")
        assertEquals(quest.narrate.story.size, 1)
        assertEquals(quest.narrate.story.firstOrNull()?.message, "Hello world!")
    }
}