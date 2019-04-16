package mcgill.sharoz.belltest.ui.main

import android.location.Location
import mcgill.sharoz.belltest.models.tweetModel.TweetsItem
import mcgill.sharoz.belltest.ui.base.BaseContract

class MainContract {

    interface View : BaseContract.View {
        fun generateMessage(message: String)
        fun animateMapCamera(location: Location)
        fun setTweetMarkers(tweets: List<TweetsItem?>)
        fun openTweetDialog(tweet: TweetsItem)
        fun toggleLanguage()
    }

    interface Presenter : BaseContract.Presenter {
        fun fetchTweets(location: Location, radius: String)
        fun saveToken(accessToken: String)
        fun loadMapTweets(accessToken: String, lastLocation: Location, radius: String)
    }
}