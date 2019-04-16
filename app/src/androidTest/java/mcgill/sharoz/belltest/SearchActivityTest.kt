package mcgill.sharoz.belltest


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.google.gson.Gson
import mcgill.sharoz.belltest.models.SearchResponse
import mcgill.sharoz.belltest.ui.search.SearchActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when` as whenever

/**
 * Instrumented test, which will execute on an Android device.
 */

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @Rule
    @JvmField
    val activity  = ActivityTestRule(SearchActivity::class.java)

    @Test
    fun testButton() {

        onView(withId(R.id.search)).check(matches(not(isEnabled())))
        onView(withId(R.id.editText)).perform(typeText("abc"))
        onView(withId(R.id.search)).check(matches(isEnabled()))
        onView(withId(R.id.editText)).perform(clearText())
        onView(withId(R.id.search)).check(matches(not(isEnabled())))
        onView(withId(R.id.editText)).perform(typeText("xyz"))
        onView(withId(R.id.search)).check(matches(isEnabled()))
        onView(withId(R.id.editText)).perform(clearText(),typeText("   "))
        onView(withId(R.id.search)).check(matches(not(isEnabled())))
    }

    companion object {

        private val context = InstrumentationRegistry.getTargetContext()

        // successSearchResponse.tweets?.let {  .addTweetsToList(it) }

        private val successSearchResponse by lazy {
            Gson().fromJson(getJson("search_response_success.json"), SearchResponse::class.java)
        }

        private fun getJson(fileName: String): String {
            return context.resources.assets
                .open(fileName)
                .bufferedReader()
                .use {
                    it.readText()
                }
        }
    }
}
