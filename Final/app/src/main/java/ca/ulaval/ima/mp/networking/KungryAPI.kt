package ca.ulaval.ima.mp.networking


import ca.ulaval.ima.mp.model.*
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface KungryAPI {

    companion object{
        const val API_V1 = "/api/v1/"
        const val BASE_URL: String = "https://kungry.ca"
    }

    @Headers("Authorization: Bearer XXXXXX")
    @POST(API_V1 + "account/")
    fun postAccount(@Body createAccountCreate: CreateAccountCreate): Call<ContentResponse<TokenOutput>>

    @POST(API_V1 + "account/login/")
    fun postAccountLogin(@Body accountLogin: AccountLogin): Call<ContentResponse<TokenOutput>>

    @GET(API_V1 + "account/me/")
    fun getAccountMe(): Call<ContentResponse<Account>>

    @POST(API_V1 + "account/refresh_token/")
    fun postAccountRefreshToken(@Body RefreshTokenInput: RefreshTokenInput): Call<ContentResponse<TokenOutput>>

    @GET(API_V1 + "restaurant/")
    fun getListRestaurant(@Query("page") page:Int,
                          @Query("page_size") page_size:Int): Call<ContentResponse<PaginatedResultSerializer<RestaurantLight>>>

    @GET(API_V1 + "restaurant/direct_search/")
    fun getRestaurantDirectSearch(@Query("page") page:Int,
                                  @Query("page_size") page_size:Int,
                                  @Query("latitude") latitude:Number,
                                  @Query("longitude") longitude:Number,
                                  @Query("radius") radius:Int,
                                  @Query("text") text:String): Call<ContentResponse<PaginatedResultSerializer<RestaurantLight>>>

    // Legit Aucune idee c'est quoi la difference entre direct search et search...
    @GET(API_V1 + "restaurant/search/")
    fun getRestaurantSearch(@Query("page") page:Int,
                                  @Query("page_size") page_size:Int,
                                  @Query("latitude") latitude:Number,
                                  @Query("longitude") longitude:Number,
                                  @Query("radius") radius:Int,
                                  @Query("text") text:String): Call<ContentResponse<PaginatedResultSerializer<RestaurantLight>>>

    @GET(API_V1 + "restaurant/search/")
    fun getRestaurantSearchNoText(@Query("page") page:Int,
                            @Query("page_size") page_size:Int,
                            @Query("latitude") latitude:Number,
                            @Query("longitude") longitude:Number,
                            @Query("radius") radius:Int) : Call<ContentResponse<PaginatedResultSerializer<RestaurantLight>>>


    @GET(API_V1 + "restaurant/{id}/")
    fun getRestaurantById(@Path("id") restaurant_id: Int,
                          @Query("latitude") latitude:Number,
                          @Query("longitude") longitude:Number): Call<ContentResponse<Restaurant>>

    @GET(API_V1 + "restaurant/{id}/reviews")
    fun getRestaurantReviewById(@Path("id") restaurant_id: Int): Call<ContentResponse<Review>>

    @POST(API_V1 + "reviews")
    fun postReview(@Body createReview: CreateReview): Call<ContentResponse<Review>>

    @GET(API_V1 + "reviews")
    fun getReviewMine(@Query("page") page:Int,
                      @Query("page_size") page_size:Int): Call<ContentResponse<Review>>

    @Headers("Authorization: Bearer XXXXXX")
    @Multipart
    @POST(API_V1 + "review/{id}/image/")
    fun postReviewPhoto(@Path("id") reviewId: Int,@Part image:MultipartBody.Part): Call<ContentResponse<Review>>

    data class ContentResponse<T> (
        @SerializedName("content") val content : T,
        @SerializedName("meta") val meta: Meta,
        @SerializedName("error") var error: Error
    )

    data class Error (
        @SerializedName("display") val displayMessage: String
    )

    data class Meta (
        @SerializedName("status_code") val statusCode: Int
    )

}