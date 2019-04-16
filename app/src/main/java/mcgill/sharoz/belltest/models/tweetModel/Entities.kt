package mcgill.sharoz.belltest.models.tweetModel

import com.google.gson.annotations.SerializedName

data class Entities(

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null
)