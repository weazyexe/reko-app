package dev.weazyexe.core.network

/**
 * Interface for entities what can be transformed to [T]
 */
interface Transformable <T> {

    fun transform(): T
}

/**
 * Transforms [Transformable] collection to collection of [T]
 */
fun <T> List<Transformable<T>>.transform() = map { it.transform() }