package exe.weazy.reko.data

import android.content.Context
import exe.weazy.reko.data.prefs.PreferenceHelper
import exe.weazy.reko.data.prefs.PreferenceHelper.get
import exe.weazy.reko.data.prefs.PreferenceHelper.set
import exe.weazy.reko.model.RecognizerName
import exe.weazy.reko.util.values.RECOGNIZER_KEY

class SettingsRepository(context: Context) {

    private val prefs = PreferenceHelper.prefs(context)

    fun getRecognizer(): RecognizerName {
        val recognizer: String? = prefs[RECOGNIZER_KEY]

        return if (recognizer == RecognizerName.LOCAL.name) {
            RecognizerName.LOCAL
        } else {
            RecognizerName.SKY_BIOMETRY
        }
    }

    fun saveRecognizer(recognizer: RecognizerName) {
        prefs[RECOGNIZER_KEY] = recognizer.name
    }
}