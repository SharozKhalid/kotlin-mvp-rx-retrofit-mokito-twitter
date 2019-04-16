package mcgill.sharoz.belltest

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import mcgill.sharoz.belltest.util.Constants.ENGLISH
import mcgill.sharoz.belltest.util.Constants.FRENCH
import mcgill.sharoz.belltest.util.PreferencesHelper
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 */

@RunWith(AndroidJUnit4::class)
class PreferenceHelperTest {

    @Test
    fun preferenceHelper_setTokenValue_getTokenValue() {

        preferences.deviceToken = "This my My First Value"
        assertThat(preferences.deviceToken, `is`("This my My First Value"))

        preferences.deviceToken = "This my My Second Value"
        assertThat(preferences.deviceToken, `is`("This my My Second Value"))
    }

    @Test
    fun preferenceHelper_setLangValue_getLangValue() {

        preferences.langCode = FRENCH
        assertThat(preferences.langCode, `is`(FRENCH))

        preferences.langCode = ENGLISH
        assertThat(preferences.langCode, `is`(ENGLISH))
    }


    companion object {
        private val context = InstrumentationRegistry.getContext()
        private val preferences = PreferencesHelper.init(context)
    }
}
