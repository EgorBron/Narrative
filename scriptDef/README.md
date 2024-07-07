# The QuestBuilder definition

This module contains everything related to the QuestBuilder class,
including QuestBuilder itself and NarrationManager interface.

There are three reasons why it is separated from the host module:
1. Script development also requires a host, which needs to be separated from the script definitions.
    This module would be compiled to a separate JAR, so it will be actual NarrativeLib.
2. I need my syntax highlighting!
3. The custom scripting docs said I should.


## How you should implement your quest host

1. First of all, create a quest builder based on the `QuestBuilder` class.
2. For each quest you need to define or use own `NarrationManager` - it is responsible for story creation. You need to implement:
   * all operator funcs
   * selector, input and deferred blocks functions
3. Each `NarrationManager` require implementations of custom message type and base actor types.
4. Lastly, you write your story processor, which is usually iterates over `story` list in your `NarrationManager` and applies some actions to your messages.

## File structure

* `net/blusutils/narrativelib/`
  * `actor/` - Actor-related definitions
  * `narration/` - `NarrationManager`s
  * `quest/` - Quests and its builders
  * `util/` - Utility classes (not added yet)
  * `storage/` - FactDB implementations & utilities
  * `samples/` - Sample implementations
    * `data/` - Messages and so on
    * `narration/` - Sample `NarrationManager`s
    * `quest/` - Quest types classes
    * `storage/` - Custom (advanced) FactDB implementations (not added yet)
    * `scripting/` - KotlinScript setup