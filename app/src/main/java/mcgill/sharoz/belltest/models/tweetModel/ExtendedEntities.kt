package mcgill.sharoz.belltest.models.tweetModel

import com.google.gson.annotations.SerializedName

data class ExtendedEntities(

	@field:SerializedName("media")
	val media: List<MediaItem?>? = null
)