package mcgill.sharoz.belltest.models

import com.google.gson.annotations.SerializedName
import mcgill.sharoz.belltest.models.tweetModel.TweetsItem

data class SearchResponse(

	@field:SerializedName("statuses")
	var tweets: List<TweetsItem?>? = null
)
