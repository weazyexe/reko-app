package dev.weazyexe.core.utils.extensions

/**
 * Добавить к [StringBuilder] строку [text]
 * при выполнении условия [condition]
 */
fun StringBuilder.appendIf(
    condition: Boolean,
    text: CharSequence
): StringBuilder =
    if (condition) {
        append(text)
    } else {
        this
    }