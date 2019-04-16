package mcgill.sharoz.belltest.ui.search

import mcgill.sharoz.belltest.models.tweetModel.TweetsItem
import mcgill.sharoz.belltest.ui.base.BaseContract

class SearchContract {

    interface View: BaseContract.View {
        fun generateMessage(message: String)
        fun addTweetsToList(tweets: List<TweetsItem?>)
        fun showFailedLoading(message: String)
        fun performNewSearch(query: String)
        fun updateSearchButton(enabled: Boolean)
    }

    interface Presenter : BaseContract.Presenter {
        fun fetchListTweets(accessToken: String, query: String, maxId: Long, resultType: String)
        fun textChanged(text: String)
    }
}