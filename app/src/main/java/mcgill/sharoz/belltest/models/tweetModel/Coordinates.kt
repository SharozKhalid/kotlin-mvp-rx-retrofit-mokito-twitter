package mcgill.sharoz.belltest.models.tweetModel

import com.google.gson.annotations.SerializedName

data class Coordinates(

	@field:SerializedName("coordinates")
	val coordinates: List<Double?>? = null,

	@field:SerializedName("type")
	val type: String? = null
)