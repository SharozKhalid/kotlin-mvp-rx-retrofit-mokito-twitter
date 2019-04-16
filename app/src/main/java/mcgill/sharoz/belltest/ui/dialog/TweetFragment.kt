package mcgill.sharoz.belltest.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tweet.view.*
import mcgill.sharoz.belltest.R
import mcgill.sharoz.belltest.models.tweetModel.TweetsItem
import mcgill.sharoz.belltest.ui.tweet.TweetActivity
import mcgill.sharoz.belltest.util.Constants.HANDLE
import mcgill.sharoz.belltest.util.Constants.ID
import mcgill.sharoz.belltest.util.Constants.IMAGE
import mcgill.sharoz.belltest.util.Constants.TEXT

class TweetFragment : DialogFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_tweet, container)

        view.handle.text = getString(R.string.a, arguments?.getString(HANDLE))
        view.text.text = arguments?.getString(TEXT)

        view.button.setOnClickListener(this)
        view.close.setOnClickListener(this)

        arguments?.getString(IMAGE).let { img ->

            Log.d(TAG, "Image: $img")

            Picasso.get()
                .load(img)
                .into(view.avatar)
        }

        return view
    }

    private fun openTweetDetails() {

        val intent = Intent(activity, TweetActivity::class.java)
        intent.putExtra(ID, arguments?.getLong(ID))
        startActivity(intent)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.button -> {
                dialog.dismiss()
                openTweetDetails()
            }

            R.id.close -> {
                dialog.dismiss()
            }
        }
    }

    companion object {

        private const val TAG = "TweetFragment"

        fun newInstance(tweet: TweetsItem): TweetFragment {

            val fragment = TweetFragment()
            val args = Bundle()
            args.putLong(ID, tweet.id ?: 0)
            args.putString(HANDLE, tweet.user?.screenName ?: "")
            args.putString(TEXT, tweet.text ?: "")
            args.putString(IMAGE, tweet.user?.profileImageUrlHttps ?: "")
            fragment.arguments = args
            return fragment
        }
    }
}