package net.blusutils.narrativelib.samples.narration

import net.blusutils.narrativelib.narration.NarrationManager

/**
 * Creates a new narration manager with default type [DefaultNarrator]
 */
inline fun<T, N: NarrationManager<T>> buildNarration(create: ()->N = { DefaultNarrator() as N }, block: N.() -> Unit): N {
    return create().apply(block)
}