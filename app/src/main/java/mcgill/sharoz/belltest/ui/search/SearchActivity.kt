package mcgill.sharoz.belltest.ui.search

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*
import mcgill.sharoz.belltest.R
import mcgill.sharoz.belltest.api.TwitterAPIService
import mcgill.sharoz.belltest.models.tweetModel.TweetsItem
import mcgill.sharoz.belltest.ui.adapter.TweetsAdapter
import mcgill.sharoz.belltest.util.PreferencesHelper

class SearchActivity : AppCompatActivity(), SearchContract.View {

    private lateinit var mPresenter: SearchPresenter
    private lateinit var mTweetsAdapter: TweetsAdapter
    private var mTweets = ArrayList<TweetsItem?>()
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)
        setTitle(R.string.search_tweets)

        mPresenter = SearchPresenter(this, TwitterAPIService.create())

        initEditText()
        initSearchListener()
        initRecyclerView()
    }

    private fun initEditText() {

        editText.imeOptions = EditorInfo.IME_ACTION_SEARCH
        editText.setOnEditorActionListener { _, id, _ ->

            if (id == EditorInfo.IME_ACTION_SEARCH && editText.text.toString().isNotEmpty()) {
                performNewSearch(editText.text.toString())
            }
            true
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mPresenter.textChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun updateSearchButton(enabled: Boolean) {
        search.isEnabled = enabled
    }

    private fun initSearchListener() {

        search.setOnClickListener {

            if (editText.text.toString().isNotEmpty()) {
                performNewSearch(editText.text.toString())
            }
        }
    }

    private fun initRecyclerView() {

        recyclerView.layoutManager = LinearLayoutManager(this)
        mTweetsAdapter = TweetsAdapter(mTweets, this)
        recyclerView.adapter = mTweetsAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {

                if (isLoading) return

                val visibleItemCount = rv.layoutManager!!.childCount
                val totalItemCount = rv.layoutManager!!.itemCount
                val pastVisibleItems = (rv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {

                    isLoading = true
                    generateMessage(getString(R.string.loading_data))
                    mPresenter.fetchListTweets(
                        PreferencesHelper.getInstance().deviceToken,
                        editText.text.toString(), mTweets[mTweets.size - 1]?.id ?: 0, RESULT_TYPE
                    )
                }
            }
        })
    }

    override fun performNewSearch(query: String) {

        editText.hideKeyboard()
        mTweets.clear()
        recyclerView.adapter?.notifyDataSetChanged()

        isLoading = true
        generateMessage(getString(R.string.loading_data))
        mPresenter.fetchListTweets(PreferencesHelper.getInstance().deviceToken, query, 0, RESULT_TYPE)
    }

    override fun generateMessage(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showFailedLoading(message: String) {

        isLoading = false
        generateMessage(message)
    }

    override fun addTweetsToList(tweets: List<TweetsItem?>) {

        val newTweets = ArrayList<TweetsItem>(tweets)

        // Getting a duplicate item in case of pagination data - Removing it
        if (mTweets.size > 0 && newTweets.size > 0) newTweets.removeAt(0)

        mTweets.addAll(newTweets)

        if (mTweets.isEmpty()) {
            generateMessage(getString(R.string.no_tweets_found))
        }

        recyclerView.adapter?.notifyDataSetChanged()
        isLoading = false

        Log.d(TAG, "List updated")
    }

    override fun onDestroy() {

        super.onDestroy()
        mPresenter.unsubscribe()
    }

    override fun onPause() {

        super.onPause()
        isLoading = false
    }

    companion object {

        private const val TAG = "SearchActivity"
        private const val RESULT_TYPE = "mixed"

        fun View.hideKeyboard() {

            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}