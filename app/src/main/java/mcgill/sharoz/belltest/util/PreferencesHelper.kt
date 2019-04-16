package mcgill.sharoz.belltest.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import mcgill.sharoz.belltest.util.Constants.ENGLISH

class PreferencesHelper {

    var deviceToken: String
        get() = preferences.getString(DEVICE_TOKEN, "")
        set(value) = preferences.edit().putString(DEVICE_TOKEN, value).apply()

    var langCode: String
        get() = preferences.getString(LANGUAGE, ENGLISH)
        set(value) = preferences.edit().putString(LANGUAGE, value).apply()

    companion object {

        private lateinit var preferences: SharedPreferences
        private val mInstance: PreferencesHelper by lazy { PreferencesHelper() }

        private const val DEVICE_TOKEN = "data.source.prefs.DEVICE_TOKEN"
        private const val LANGUAGE = "data.source.prefs.LANGUAGE"

        internal fun init(mContext: Context): PreferencesHelper {
            preferences = PreferenceManager.getDefaultSharedPreferences(mContext)
            return mInstance
        }

        fun getInstance(): PreferencesHelper {
            return mInstance
        }
    }
}