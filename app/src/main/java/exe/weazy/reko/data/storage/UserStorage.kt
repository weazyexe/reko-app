package exe.weazy.reko.data.storage

import android.content.Context
import exe.weazy.reko.data.storage.PreferenceHelper.get
import exe.weazy.reko.data.storage.PreferenceHelper.set
import exe.weazy.reko.model.UserInfo
import exe.weazy.reko.util.values.*

class UserStorage(context: Context) {

    private val prefs = PreferenceHelper.prefs(context)

    fun getAccessToken() = prefs[ACCESS_TOKEN_KEY] ?: EMPTY_STRING

    fun getUserInfo() : UserInfo {
        val id: Int = prefs[USER_ID_KEY] ?: EMPTY_INT
        val username: String = prefs[USER_NAME_KEY] ?: EMPTY_STRING
        val firstName: String = prefs[USER_FIRST_NAME_KEY] ?: EMPTY_STRING
        val lastName: String = prefs[USER_LAST_NAME_KEY] ?: EMPTY_STRING
        val description: String = prefs[USER_DESCRIPTION_KEY] ?: EMPTY_STRING

        return UserInfo(id, username, firstName, lastName, description)
    }

    fun saveAccessToken(token: String) {
        prefs[ACCESS_TOKEN_KEY] = token
    }

    fun saveUserInfo(userInfo: UserInfo) {
        prefs[USER_ID_KEY] = userInfo.id
        prefs[USER_NAME_KEY] = userInfo.username
        prefs[USER_FIRST_NAME_KEY] = userInfo.firstName
        prefs[USER_LAST_NAME_KEY] = userInfo.lastName
        prefs[USER_DESCRIPTION_KEY] = userInfo.description
    }

    fun erase() {
        prefs[ACCESS_TOKEN_KEY] = null
        prefs[USER_ID_KEY] = null
        prefs[USER_NAME_KEY] = null
        prefs[USER_FIRST_NAME_KEY] = null
        prefs[USER_LAST_NAME_KEY] = null
        prefs[USER_DESCRIPTION_KEY] = null
    }
}
