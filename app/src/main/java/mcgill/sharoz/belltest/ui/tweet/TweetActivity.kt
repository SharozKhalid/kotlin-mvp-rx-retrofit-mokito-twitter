package mcgill.sharoz.belltest.ui.tweet

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.activity_tweet.*
import mcgill.sharoz.belltest.R
import mcgill.sharoz.belltest.util.Constants.CONSUMER_KEY
import mcgill.sharoz.belltest.util.Constants.CONSUMER_SECRET
import mcgill.sharoz.belltest.util.Constants.ID
import mcgill.sharoz.belltest.util.Utils.formatDate

class TweetActivity : AppCompatActivity(), TweetContract.View, View.OnClickListener {

    private lateinit var mPresenter: TweetPresenter
    private var mTweetID = 0L
    private var isFavorited = false
    private var isRetweeted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTwitterCore()
        setContentView(R.layout.activity_tweet)
        setTitle(R.string.tweet_details)

        mPresenter = TweetPresenter(this, TwitterCore.getInstance().apiClient)

        mTweetID = intent.getLongExtra(ID, 0)
        mPresenter.loadTweetDetail(mTweetID)

        initLoginFlow()
    }

    private fun initTwitterCore() {
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(
                TwitterAuthConfig(
                    CONSUMER_KEY,
                    CONSUMER_SECRET
                )
            )
            .debug(true)
            .build()
        Twitter.initialize(config)
    }

    private fun initLoginFlow() {

        login.callback = object : Callback<TwitterSession>() {

            override fun success(result: Result<TwitterSession>?) {

                val session = TwitterCore.getInstance().sessionManager.activeSession
                val authToken = session.authToken
                val token = authToken.token
                val secret = authToken.secret

                Log.d(TAG, "token: $token secret: $secret")

                updateUIListeners()
            }

            override fun failure(exception: TwitterException?) {
                Log.e(TAG, exception?.message)
                exception?.message?.let { generateMessage(it) }
            }
        }
    }

    private fun updateUIListeners() {

        login.visibility = View.INVISIBLE
        favorite.visibility = View.VISIBLE
        retweet.visibility = View.VISIBLE

        refreshFavoriteUI()
        refreshRetweetUI()

        favorite.setOnClickListener(this)
        retweet.setOnClickListener(this)
    }

    override fun onClick(id: View?) {

        when (id?.id) {

            R.id.favorite -> {

                if (isFavorited) {
                    mPresenter.removeFavoriteTweet(mTweetID)
                } else {
                    mPresenter.createFavoriteTweet(mTweetID)
                }
            }

            R.id.retweet -> {

                if (isRetweeted) {
                    mPresenter.removeRetweet(mTweetID)
                } else {
                    mPresenter.sendRetweet(mTweetID)
                }
            }
        }
    }

    override fun favoriteUpdate(favorited: Boolean) {

        isFavorited = favorited
        refreshFavoriteUI()
    }

    override fun retweetUpdate(retweeted: Boolean) {

        isRetweeted = retweeted
        refreshRetweetUI()
    }

    private fun refreshFavoriteUI() {

        if (isFavorited) {
            favorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_colored))
        } else {
            favorite.setImageDrawable(getDrawable(R.drawable.ic_favorite_gray))
        }
    }

    private fun refreshRetweetUI() {

        if (isRetweeted) {
            retweet.setImageDrawable(getDrawable(R.drawable.ic_retweet_colored))
        } else {
            retweet.setImageDrawable(getDrawable(R.drawable.ic_retweet_gray))
        }
    }

    override fun generateMessage(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun updateTweetDetail(tweet: Tweet?) {

        handle.text = getString(R.string.a, tweet?.user?.screenName ?: "")
        tweet?.text?.let { text.text = it }
        tweet?.createdAt ?.let { date.text = formatDate(it) }

        if (tweet?.user?.profileImageUrlHttps ?: "" != "") {

            val image = tweet?.user?.profileImageUrlHttps?.replace("_normal", "")

            Log.d(TAG, image)

            Picasso.get()
                .load(image)
                .into(avatar)
        }

        tweet?.favorited?.let { isFavorited = it }
        tweet?.retweeted?.let { isRetweeted = it }

        checkLoginSession()
    }

    private fun checkLoginSession() {

        if (TwitterCore.getInstance().sessionManager?.activeSession?.userId ?: 0L != 0L) {
            updateUIListeners()

        } else {
            login.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "Request Code: $requestCode")
        login.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "TweetActivity"
    }
}