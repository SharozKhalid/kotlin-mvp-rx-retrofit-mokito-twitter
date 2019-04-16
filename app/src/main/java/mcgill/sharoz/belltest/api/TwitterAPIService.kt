package mcgill.sharoz.belltest.api

import io.reactivex.Observable
import mcgill.sharoz.belltest.models.SearchResponse
import mcgill.sharoz.belltest.models.TokenResponse
import mcgill.sharoz.belltest.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitterAPIService {

    @POST("oauth2/token")
    fun getToken(
        @Header("Authorization") auth: String,
        @Header("Content-Type") contentType: String,
        @Query("grant_type") grantType: String
    ): Observable<TokenResponse>

    @GET("1.1/search/tweets.json")
    fun searchTweets(
        @Header("Authorization") authorization: String,
        @Query("q") query: String,
        @Query("geocode") geocode: String,
        @Query("result_type") resultType: String,
        @Query("count") count: Int,
        @Query("include_entities") includeEntities: Boolean,
        @Query("max_id") maxId: Long): Observable<SearchResponse>

    companion object {

        fun create(): TwitterAPIService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(TwitterAPIService::class.java)
        }
    }
}