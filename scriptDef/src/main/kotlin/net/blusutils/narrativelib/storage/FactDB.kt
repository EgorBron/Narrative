package net.blusutils.narrativelib.storage

/**
 * Simple key-value database for storing "facts".
 */
object FactDB {
    private val facts = mutableMapOf<String, Any?>()
    fun add(fact: String, value: Any? = null) {
        facts[fact] = value
    }
    fun get(fact: String): Any? {
        return facts[fact]
    }
    fun has(fact: String): Boolean {
        return facts.containsKey(fact)
    }
    fun remove(fact: String) {
        facts.remove(fact)
    }

    fun getView(): Map<String, Any?> {
        return facts
    }
}