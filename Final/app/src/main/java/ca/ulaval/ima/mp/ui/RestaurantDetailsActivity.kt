package ca.ulaval.ima.mp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.Location
import ca.ulaval.ima.mp.model.Restaurant
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import ca.ulaval.ima.mp.ui.parcelables.ParcelDataAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDetailsActivity : AppCompatActivity() {
    private var currentLatLng: Location? = null

    private lateinit var selectedRestaurant : Restaurant
    var objectID = ParcelDataAPI(0, 0.0,0.0)

    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        val intent = intent
        objectID = intent.getParcelableExtra("restoID")!!

        var loginButton = findViewById<Button>(R.id.detailsLoginBtn)
        var evaluationButton = findViewById<Button>(R.id.detailsEvalBtn)
        var loginLayout = findViewById<ConstraintLayout>(R.id.connexionConstraintLayout)
        var detailsConnextionText = findViewById<TextView>(R.id.detailsConnexionText)

        var textName = findViewById<TextView>(R.id.restaurantDetails_nameRestTextView)
        var textType = findViewById<TextView>(R.id.restaurantDetails_typeRestTextView)
        var textNbReviews = findViewById<TextView>(R.id.restaurantDetails_nbrReviewsTextView)
        var textNbReviews2 = findViewById<TextView>(R.id.restaurantDetails_nbrReviews2TextView)
        var textKm = findViewById<TextView>(R.id.restaurantDetails_kmDetailsTextView)
        var textPhone = findViewById<TextView>(R.id.restaurantDetails_phoneTextView)
        var textWebsite = findViewById<TextView>(R.id.restaurantDetails_siteWebTextView)

        var ratingBar = findViewById<RatingBar>(R.id.restaurantDetails_ratingsRatingBar)

        var imageRestaurant = findViewById<ImageView>(R.id.detailsImageView)

        var textMonHour = findViewById<TextView>(R.id.monHourTextView)
        var textTueHour = findViewById<TextView>(R.id.tueHourTextView)
        var textWedHour = findViewById<TextView>(R.id.wedHourTextView)
        var textThuHour = findViewById<TextView>(R.id.thuHourTextView)
        var textFriHour = findViewById<TextView>(R.id.FriHourTextView)
        var textSatHour = findViewById<TextView>(R.id.satHourTextView)
        var textSunHour = findViewById<TextView>(R.id.sunHourTextView)

        context = this;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    fun getRestaurantNearby(id: Int, latitude: Double, longitude: Int){

        imaNetworkCenter.getRestaurantById(id, latitude, longitude).enqueue(object :
                Callback<KungryAPI.ContentResponse<Restaurant>> {

            override fun onResponse(
                    call: Call<KungryAPI.ContentResponse<Restaurant>>,
                    response: Response<KungryAPI.ContentResponse<Restaurant>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.let {
                        Log.d("Test:", it.name)
                        selectedRestaurant = it
                        Toast.makeText(context, "resto " + it.name, Toast.LENGTH_LONG).show();

                    }
                }
            }

            override fun onFailure(
                    call: Call<KungryAPI.ContentResponse<Restaurant>>,
                    t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure ${t.message}")
                Toast.makeText(context, "listRestaurants Failure ${t.message}", Toast.LENGTH_LONG).show();
            }
        }
        )
    }
}