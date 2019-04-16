package mcgill.sharoz.belltest.models.tweetModel

import com.google.gson.annotations.SerializedName

data class MediaItem(

	@field:SerializedName("display_url")
	val displayUrl: String? = null,

	@field:SerializedName("id_str")
	val idStr: String? = null,

	@field:SerializedName("media_url_https")
	val mediaUrlHttps: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("media_url")
	val mediaUrl: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("video_info")
	val videoInfo: VideoInfo? = null
)