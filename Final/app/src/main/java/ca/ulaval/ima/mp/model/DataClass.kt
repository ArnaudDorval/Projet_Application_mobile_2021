package ca.ulaval.ima.mp.model

import ca.ulaval.ima.mp.networking.KungryAPI
import com.google.gson.annotations.SerializedName
import java.util.*


data class PaginatedResultSerializer<T> (
        @SerializedName("count") val totalCount: Int,
        @SerializedName("next") val nextPage: Int,
        @SerializedName("previous") val previousPage: Int,
        @SerializedName("results") val results: List<T>,
        @SerializedName("meta") val meta: KungryAPI.Meta
)

data class CreateAccountCreate(
        @SerializedName("client_id") val client_id:String,
        @SerializedName("client_secret")  val client_secret:String,
        @SerializedName("first_name")  val first_name:String,
        @SerializedName("last_name")  val last_name:String,
        @SerializedName("email")  val email:String,
        @SerializedName("password")  val password:String
)

data class TokenOutput(
        @SerializedName("access_token") val access_token:String,
        @SerializedName("token_type")  val token_type:String,
        @SerializedName("refresh_token")  val refresh_token:String,
        @SerializedName("scope")  val scope:String,
        @SerializedName("expires_in")  val expires_in:Int
)

data class AccountLogin(
        @SerializedName("client_id") val client_id:String,
        @SerializedName("client_secret")  val client_secret:String,
        @SerializedName("email")  val email:String,
        @SerializedName("password")  val password:String
)

data class Account(
        @SerializedName("total_review_count")  val total_review_count:Int,
        @SerializedName("last_name") val last_name:String,
        @SerializedName("first_name")  val first_name:String,
        @SerializedName("email")  val email:String,
        @SerializedName("created")  val created:String,
        @SerializedName("updated")  val updated:String,
        @SerializedName("expires_in")  val expires_in:Int
)

data class RefreshTokenInput(
        @SerializedName("refresh_token") val refresh_token:String,
        @SerializedName("client_id")  val client_id:String,
        @SerializedName("client_secret")  val client_secret:String
)

data class Cuisine(
        @SerializedName("id")  val id:Int,
        @SerializedName("name") val name:String

)

data class RestaurantLight(
        @SerializedName("id")  val id:Int,
        @SerializedName("name") val name:String,
        @SerializedName("cuisine")  val cuisine:List<Cuisine>,
        @SerializedName("type") val type:String,
        @SerializedName("review_count")  val review_count:Int,
        @SerializedName("review_average")  val review_average:Float,
        @SerializedName("image")  val image:String,
        @SerializedName("distance")  val distance:String,
        @SerializedName("location")  val location:Location
)

data class Location(
        @SerializedName("latitude") val latitude:Double,
        @SerializedName("longitude") val longitude:Double
)

data class OpeningHour(
        @SerializedName("id")  val id:Int,
        @SerializedName("opening_hour") val opening_hour:String,
        @SerializedName("closing_hour") val closing_hour:String,
        @SerializedName("day") val day:String

)

data class Creator(
        @SerializedName("first_name") val first_name:String,
        @SerializedName("last_name")  val last_name:String
)

data class Review(
        @SerializedName("id")  val id:Int,
        @SerializedName("creator") val creator:Creator,
        @SerializedName("stars") val stars:Int,
        @SerializedName("image") val image:String,
        @SerializedName("comment") val comment:String,
        @SerializedName("date") val date:String
)

data class Restaurant(
        @SerializedName("id")  val id:Int,
        @SerializedName("cuisine")  val cuisine:List<Cuisine>,
        @SerializedName("distance")  val distance:String,
        @SerializedName("review_count")  val review_count:Int,
        @SerializedName("opening_hours")  val opening_hours:List<OpeningHour>,
        @SerializedName("review_average")  val review_average:Float,
        @SerializedName("location")  val location:Location,
        @SerializedName("reviews")  val reviews:List<Review>,
        @SerializedName("name") val name:String,
        @SerializedName("website") val website:String,
        @SerializedName("phone_number") val phone_number:String,
        @SerializedName("image")  val image:String,
        @SerializedName("type") val type:String
)

data class CreateReview(
        @SerializedName("restaurant_id")  val restaurant_id:Int,
        @SerializedName("stars") val stars:Int,
        @SerializedName("comment") val comment:String
)