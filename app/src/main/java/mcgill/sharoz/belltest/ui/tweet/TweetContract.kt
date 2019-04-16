package mcgill.sharoz.belltest.ui.tweet

import com.twitter.sdk.android.core.models.Tweet
import mcgill.sharoz.belltest.ui.base.BaseContract

class TweetContract {

    interface View : BaseContract.View {
        fun generateMessage(message: String)
        fun updateTweetDetail(tweet: Tweet?)
        fun favoriteUpdate(favorited: Boolean)
        fun retweetUpdate(retweeted: Boolean)
    }

    interface Presenter : BaseContract.Presenter{
        fun loadTweetDetail(tweetID: Long)
        fun createFavoriteTweet(tweetID: Long?)
        fun removeFavoriteTweet(tweetID: Long?)
        fun sendRetweet(tweetID: Long?)
        fun removeRetweet(tweetID: Long?)
    }
}