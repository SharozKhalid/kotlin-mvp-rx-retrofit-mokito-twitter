package mcgill.sharoz.belltest.ui.search

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import mcgill.sharoz.belltest.api.TwitterAPIService
import mcgill.sharoz.belltest.util.Constants.BEARER

class SearchPresenter(private val mView: SearchContract. View, private val mApiService: TwitterAPIService) : SearchContract.Presenter {

    private val mSubscriptions = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun fetchListTweets(accessToken: String, query: String, maxId: Long, resultType: String) {

        val subscribe = mApiService.searchTweets(
            "$BEARER $accessToken", query, "", resultType, 100, true, maxId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    Log.d(TAG,"Fetch success")
                    response?.tweets?.let { mView.addTweetsToList(it) }

                },
                { error ->

                    Log.e(TAG, error.toString())
                    error?.message?.let { mView.showFailedLoading(it) }
                }
            )

        mSubscriptions.add(subscribe)
    }

    override fun textChanged(text: String) {

        if(text.trim() == ""){
            mView.updateSearchButton(false)
        }else{
            mView.updateSearchButton(true)
        }
    }

    companion object {
        private const val TAG = "SearchPresenter"
    }
}