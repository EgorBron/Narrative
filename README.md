# Narrative(Lib)

Build a story with intuitive Kotlin DSL.

```kt
val me = AbstractActor("Me")
val it = AbstractActor("It")

val quest = rewardableQuest("Main", 100) {
    - "Hello to you!"
    me - "Uhm, hi?"
    it - "This is a test quest."
}
```

Check the [Main.kt](./src/main/kotlin/Main.kt) file for full example.

> [!IMPORTANT]  
> `Narrative` acts like a base for your own implementation. It is not a standalone library.
> (I will make some later)

> [!WARNING]  
> Work-in-progress

```

Copyright 2024 EgorBron

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```