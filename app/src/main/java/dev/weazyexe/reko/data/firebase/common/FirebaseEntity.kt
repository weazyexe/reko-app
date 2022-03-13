package dev.weazyexe.reko.data.firebase.common

/**
 * Base interface for firebase entities
 */
interface FirebaseEntity {

    /**
     * Converts the [FirebaseEntity] to Firebase Firestore map format
     */
    fun asMap(): Map<String, Any?>
}

/**
 * Converts list of [FirebaseEntity] to Firebase Firestore map format
 */
fun List<FirebaseEntity>.asMap(): List<Map<String, Any?>> = map { it.asMap() }