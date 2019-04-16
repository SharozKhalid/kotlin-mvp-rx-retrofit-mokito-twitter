package mcgill.sharoz.belltest.models.tweetModel

import com.google.gson.annotations.SerializedName

data class TweetsItem(

	@field:SerializedName("coordinates")
	val coordinates: Coordinates? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("retweet_count")
	val retweetCount: Int? = null,

	@field:SerializedName("retweeted")
	val retweeted: Boolean? = null,

	@field:SerializedName("geo")
	val geo: Geo? = null,

	@field:SerializedName("entities")
	val entities: Entities? = null,

	@field:SerializedName("id_str")
	val idStr: String? = null,

	@field:SerializedName("favorite_count")
	val favoriteCount: Int? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("favorited")
	val favorited: Boolean? = null,

	@field:SerializedName("extended_entities")
	val extendedEntities: ExtendedEntities? = null
)