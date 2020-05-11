package exe.weazy.reko.data

import android.content.Context
import exe.weazy.reko.data.prefs.PreferenceHelper
import exe.weazy.reko.data.prefs.PreferenceHelper.set
import exe.weazy.reko.data.prefs.PreferenceHelper.get
import exe.weazy.reko.util.values.EMPTY_STRING
import exe.weazy.reko.util.values.RECOGNIZER_KEY

class SettingsRepository(context: Context) {

    private val prefs = PreferenceHelper.prefs(context)

    fun getRecognizer(): String = prefs[RECOGNIZER_KEY] ?: EMPTY_STRING

    fun saveRecognizer(recognizer: String) {
        prefs[RECOGNIZER_KEY] = recognizer
    }
}