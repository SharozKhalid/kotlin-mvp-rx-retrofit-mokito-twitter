package mcgill.sharoz.belltest.ui.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tweet_general.view.*
import kotlinx.android.synthetic.main.item_tweet_image.view.*
import kotlinx.android.synthetic.main.item_tweet_video.view.*
import mcgill.sharoz.belltest.R
import mcgill.sharoz.belltest.models.tweetModel.TweetsItem

class TweetsAdapter(private val tweets: List<TweetsItem?>, private val context: Context) : Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            1 -> ImageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_tweet_image,
                    parent,
                    false
                )
            )
            2 -> VideoViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_tweet_video,
                    parent,
                    false
                )
            )
            else -> GeneralViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_tweet_general,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (getItemViewType(position)) {

            TweetType.GENERAL.ordinal -> {

                val generalHolder = holder as GeneralViewHolder
                if (tweets[position]?.user?.profileImageUrlHttps ?: "" != "") {
                    val image = tweets[position]?.user?.profileImageUrlHttps?.replace("_normal", "")
                    Picasso.get().load(image).into(generalHolder.avatar)
                }

                generalHolder.handle.text = context.getString(R.string.a, tweets[position]?.user?.screenName ?: "")
                tweets[position]?.text?.let { generalHolder.text.text = it }
            }

            TweetType.IMAGE.ordinal -> {

                val imageHolder = holder as ImageViewHolder
                if (tweets[position]?.entities?.media?.get(0)?.mediaUrlHttps ?: "" != "") {
                    val image = tweets[position]?.entities?.media?.get(0)?.mediaUrlHttps
                    Picasso.get().load(image).into(imageHolder.image)
                }

                imageHolder.handle.text = context.getString(R.string.a, tweets[position]?.user?.screenName ?: "")
                tweets[position]?.text?.let { imageHolder.text.text = it }
            }

            TweetType.VIDEO.ordinal -> {

                val videoHolder = holder as VideoViewHolder
                videoHolder.handle.text = context.getString(R.string.a, tweets[position]?.user?.screenName ?: "")
                tweets[position]?.text?.let { videoHolder.text.text = it }

                if (tweets[position]?.extendedEntities?.media?.get(0)?.videoInfo?.variants?.size ?: 0 > 0) {

                    val videoUrl = getBestVideo(tweets, position)
                    loadVideoView(videoUrl, videoHolder.video, videoHolder.play)
                }
            }
        }
    }

    private fun getBestVideo(tweets: List<TweetsItem?>, position: Int): String? {

        var bitrate = 0
        var tempVideo = ""

        for (item in tweets[position]?.extendedEntities?.media?.get(0)?.videoInfo?.variants!!) {

            if (item?.bitrate ?: 0 > bitrate && "video" in item?.contentType ?: "") {

                item?.url?.let { tempVideo = it }
                item?.bitrate?.let { bitrate = it }
            }
        }

        return tempVideo
    }

    private fun loadVideoView(videoUrl: String?, player: VideoView, play: ImageView) {

        if (videoUrl != "") {

            player.setBackgroundColor(Color.BLACK)
            player.setVideoURI(Uri.parse(videoUrl))
            player.setOnPreparedListener {

                it.isLooping = true
                player.setBackgroundColor(Color.TRANSPARENT)
            }
            play.visibility = View.VISIBLE
        }

        player.setOnClickListener {

            if (player.isPlaying) {
                player.pause()
                play.visibility = View.VISIBLE
            } else {
                player.start()
                play.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when {
            tweets[position]?.extendedEntities?.media?.get(0)?.videoInfo?.variants?.size ?: 0 > 0 -> TweetType.VIDEO.ordinal
            tweets[position]?.entities?.media?.size ?: 0 > 0 -> TweetType.IMAGE.ordinal
            else -> TweetType.GENERAL.ordinal
        }
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    enum class TweetType {
        GENERAL,
        IMAGE,
        VIDEO
    }

    class GeneralViewHolder(view: View) : ViewHolder(view) {

        val handle: TextView = view.handleGen
        val text: TextView = view.textGen
        val avatar: ImageView = view.avatarGen
    }

    class ImageViewHolder(view: View) : ViewHolder(view) {

        val handle: TextView = view.handleImg
        val text: TextView = view.textImg
        val image: ImageView = view.imageImg
    }

    class VideoViewHolder(view: View) : ViewHolder(view) {

        val handle: TextView = view.handleVid
        val text: TextView = view.textVid
        val video: VideoView = view.videoVid
        val play: ImageView = view.playVid
    }
}