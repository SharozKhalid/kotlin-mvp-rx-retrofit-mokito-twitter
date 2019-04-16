package mcgill.sharoz.belltest.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(

    @field:SerializedName("token_type")
    val tokenType: String? = null,

    @field:SerializedName("access_token")
    val accessToken: String? = null
)