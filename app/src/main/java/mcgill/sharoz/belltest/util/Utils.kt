package mcgill.sharoz.belltest.util

import android.content.Context
import android.util.Base64
import android.util.Log
import mcgill.sharoz.belltest.util.Constants.UTF8
import mcgill.sharoz.belltest.util.Constants.UTIL
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun formatDate(dateString: String): String {

        if (dateString.isEmpty()) return ""

        val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH)
        val targetFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

        originalFormat.timeZone = TimeZone.getTimeZone("UTC")
        targetFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date
        try {
            date = originalFormat.parse(dateString)
        } catch (e: ParseException) {
            Log.e("$UTIL.formatDate", e.message)
            return ""
        }
        return targetFormat.format(date)
    }

    fun base64Encoding(key: String, secret: String): String {

        if (key.isEmpty() or secret.isEmpty()) return ""

        lateinit var urlApiKey: String
        lateinit var urlApiSecret: String

        try {
            urlApiKey = URLEncoder.encode(key, UTF8)
            urlApiSecret = URLEncoder.encode(secret, UTF8)

        } catch (e: UnsupportedEncodingException) {
            Log.e("$UTIL.base64Encoding", e.message)
            return ""
        }

        val combined = "$urlApiKey:$urlApiSecret"
        return Base64.encodeToString(combined.toByteArray(), Base64.NO_WRAP)
    }

    @Suppress("deprecation")
    fun applyLanguageLocale(context: Context, newLang: String) {

        val locale = Locale(newLang)
        val config = context.resources.configuration
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        PreferencesHelper.getInstance().langCode = newLang
    }

}