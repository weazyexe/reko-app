package dev.weazyexe.core.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Interface for entities what can be transformed to [T]
 */
interface Transformable<T> {

    fun transform(): T
}

/**
 * Transforms [Transformable] collection to collection of [T]
 */
fun <T> List<Transformable<T>>.transformCollection() = map { it.transform() }

/**
 * Transforms [Flow] of [Transformable] to [Flow] of [T]
 */
fun <T> Flow<Transformable<T>>.transform() = map { it.transform() }

/**
 * Transforms [Flow] with [Transformable] collection to [Flow] with collection of [T]
 */
fun <T> Flow<List<Transformable<T>>>.transformCollection() = map { it.transformCollection() }