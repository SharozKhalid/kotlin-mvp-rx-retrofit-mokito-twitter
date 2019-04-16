package mcgill.sharoz.belltest.ui.main

import android.location.Location
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import mcgill.sharoz.belltest.api.TwitterAPIService
import mcgill.sharoz.belltest.util.Constants.BASIC
import mcgill.sharoz.belltest.util.Constants.BEARER
import mcgill.sharoz.belltest.util.Constants.CONSUMER_KEY
import mcgill.sharoz.belltest.util.Constants.CONSUMER_SECRET
import mcgill.sharoz.belltest.util.PreferencesHelper
import mcgill.sharoz.belltest.util.Utils.base64Encoding

class MainPresenter(private val mView: MainContract.View, private val mApiService: TwitterAPIService) : MainContract.Presenter {

    private val mSubscriptions = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun fetchTweets(location: Location, radius: String) {

        val base64Encoded = base64Encoding(CONSUMER_KEY, CONSUMER_SECRET)

        val subscribe = mApiService.getToken(
            "$BASIC $base64Encoded",
            CONTENT_TYPE,
            GRANT_TYPE
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    Log.d(TAG, "${result.accessToken} - token")

                    result.accessToken?.let {
                        saveToken(it)
                        loadMapTweets(result.accessToken, location, radius)
                    }
                },
                { error ->

                    Log.e(TAG, error.toString())
                    error?.message?.let { mView.generateMessage(it) }
                }
            )

        mSubscriptions.add(subscribe)
    }

    override fun saveToken(accessToken: String) {

        PreferencesHelper.getInstance().deviceToken = accessToken
    }

    override fun loadMapTweets(accessToken: String, lastLocation: Location, radius: String) {

        val geocode = "${lastLocation.latitude},${lastLocation.longitude},$radius"

        val subscribe = mApiService.searchTweets(
            "$BEARER $accessToken", "", geocode, RESULT_TYPE, 100, true, 0
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    Log.d(TAG, "Fetch success")
                    response.tweets?.let { mView.setTweetMarkers(it) }
                },
                { error ->

                    Log.e(TAG, error.toString())
                    error?.message?.let { mView.generateMessage(it) }
                }
            )

        mSubscriptions.add(subscribe)
    }

    companion object {
        private const val TAG = "MainPresenter"
        private const val RESULT_TYPE = "recent"
        private const val CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8"
        private const val GRANT_TYPE = "client_credentials"
    }
}