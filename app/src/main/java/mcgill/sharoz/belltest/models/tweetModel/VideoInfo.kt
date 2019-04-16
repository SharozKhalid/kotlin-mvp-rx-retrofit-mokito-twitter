package mcgill.sharoz.belltest.models.tweetModel

import com.google.gson.annotations.SerializedName

data class VideoInfo(

	@field:SerializedName("aspect_ratio")
	val aspectRatio: List<Int?>? = null,

	@field:SerializedName("variants")
	val variants: List<VariantsItem?>? = null
)