package exe.weazy.reko.util.extensions

private fun isFieldEmpty(text: String) = text.isEmpty()

fun String.isValidLogin() = !isFieldEmpty(this)
fun String.isValidPassword() = !isFieldEmpty(this)