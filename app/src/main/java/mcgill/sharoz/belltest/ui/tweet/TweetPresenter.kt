package mcgill.sharoz.belltest.ui.tweet

import android.util.Log
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiClient
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import io.reactivex.disposables.CompositeDisposable

class TweetPresenter(private val mView: TweetContract.View, private val mApiService: TwitterApiClient) : TweetContract.Presenter {

    private val mSubscriptions = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun loadTweetDetail(tweetID: Long) {

        mApiService.statusesService.show(tweetID, false, false, true)
            .enqueue(object : Callback<Tweet>() {

                override fun success(result: Result<Tweet>?) {

                    Log.d(TAG, "Fetch success")
                    mView.updateTweetDetail(result?.data)
                }

                override fun failure(exception: TwitterException?) {
                    Log.e(TAG, exception?.message)
                    exception?.message?.let { mView.generateMessage(it) }
                }
            })
    }

    override fun createFavoriteTweet(tweetID: Long?) {

        mApiService.favoriteService.create(tweetID, true)
            .enqueue(object : Callback<Tweet>() {

                override fun success(result: Result<Tweet>?) {

                    Log.e(TAG, "Create favorite success")
                    mView.favoriteUpdate(true)
                }

                override fun failure(exception: TwitterException?) {
                    Log.e(TAG, exception?.message)
                    exception?.message?.let { mView.generateMessage(it) }
                }

            })
    }

    override fun removeFavoriteTweet(tweetID: Long?) {

        mApiService.favoriteService.destroy(tweetID, true)
            .enqueue(object : Callback<Tweet>() {

                override fun success(result: Result<Tweet>?) {

                    Log.e(TAG, "remove favorite success")
                    mView.favoriteUpdate(false)
                }

                override fun failure(exception: TwitterException?) {
                    Log.e(TAG, exception?.message)
                    exception?.message?.let { mView.generateMessage(it) }
                }

            })
    }

    override fun sendRetweet(tweetID: Long?) {

        mApiService.statusesService.retweet(tweetID, false)
            .enqueue(object : Callback<Tweet>() {

                override fun success(result: Result<Tweet>?) {

                    Log.e(TAG, "Send retweet success")
                    mView.retweetUpdate(true)
                }

                override fun failure(exception: TwitterException?) {
                    Log.e(TAG, exception?.message)
                    exception?.message?.let { mView.generateMessage(it) }
                }

            })
    }

    override fun removeRetweet(tweetID: Long?) {

        mApiService.statusesService.unretweet(tweetID, false)
            .enqueue(object : Callback<Tweet>() {

                override fun success(result: Result<Tweet>?) {

                    Log.e(TAG, "Remove retweet success")
                    mView.retweetUpdate(false)
                }

                override fun failure(exception: TwitterException?) {
                    Log.e(TAG, exception?.message)
                    exception?.message?.let { mView.generateMessage(it) }
                }

            })
    }

    companion object {
        private const val TAG = "TweetPresenter"
    }
}