package mcgill.sharoz.belltest.models.tweetModel

import com.google.gson.annotations.SerializedName

data class VariantsItem(

	@field:SerializedName("content_type")
	val contentType: String? = null,

	@field:SerializedName("bitrate")
	val bitrate: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)