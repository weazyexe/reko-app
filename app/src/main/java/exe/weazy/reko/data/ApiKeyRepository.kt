package exe.weazy.reko.data

import android.content.Context
import android.content.SharedPreferences
import exe.weazy.reko.data.firebase.RxFirebase
import exe.weazy.reko.data.prefs.PreferenceHelper
import exe.weazy.reko.data.prefs.PreferenceHelper.get
import exe.weazy.reko.data.prefs.PreferenceHelper.set
import exe.weazy.reko.util.values.APPLICATION_KEY
import exe.weazy.reko.util.values.APPLICATION_SECRET_KEY
import exe.weazy.reko.util.values.EMPTY_STRING

class ApiKeyRepository(context: Context) {

    private val prefs: SharedPreferences = PreferenceHelper.prefs(context)

    fun getApplicationKey() = prefs[APPLICATION_KEY] ?: EMPTY_STRING

    fun getApplicationSecretKey() = prefs[APPLICATION_SECRET_KEY] ?: EMPTY_STRING

    fun updateApiKeys() = RxFirebase.getApiKeys()

    fun saveApplicationKey(key: String) {
        prefs[APPLICATION_KEY] = key
    }

    fun saveApplicationSecretKey(key: String) {
        prefs[APPLICATION_SECRET_KEY] = key
    }
}