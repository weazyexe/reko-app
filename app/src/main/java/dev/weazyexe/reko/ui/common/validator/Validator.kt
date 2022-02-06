package dev.weazyexe.reko.ui.common.validator

/**
 * Base validator interface for [T] type
 */
interface Validator<T> {

    /**
     * Validates [data]
     *
     * @param data data to validate
     * @return if [data] is invalid then returns error message, else returns null
     */
    fun validate(data: T): String?
}