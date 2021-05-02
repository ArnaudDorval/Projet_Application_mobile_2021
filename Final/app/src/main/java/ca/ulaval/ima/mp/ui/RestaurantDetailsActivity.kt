package ca.ulaval.ima.mp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.Location
import ca.ulaval.ima.mp.model.Restaurant
import ca.ulaval.ima.mp.model.Review
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import ca.ulaval.ima.mp.ui.parcelables.ParcelDataAPI
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDetailsActivity : AppCompatActivity() {
    private var currentLatLng: Location? = null

    private lateinit var selectedRestaurant : Restaurant
    var objectID = ParcelDataAPI(0, 0.0,0.0,"")

    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    var context: Context? = null

    private var mapView: MapView? = null
    private var myMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        val intent = intent
        objectID = intent.getParcelableExtra<ParcelDataAPI>("restaurant")!!

        var loginButton = findViewById<Button>(R.id.detailsLoginBtn)
        var evaluationButton = findViewById<Button>(R.id.detailsEvalBtn)
        var loginLayout = findViewById<ConstraintLayout>(R.id.connexionConstraintLayout)
        var detailsConnextionText = findViewById<TextView>(R.id.detailsConnexionText)

        var textName = findViewById<TextView>(R.id.restaurantCommentaires_EvalTitleTextView)
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

        if(objectID != null){
            objectID.restoID?.let { getSelectedRestaurant(it, objectID.latitude, objectID.longitude) }


        }else{
            Toast.makeText(context, "No valid intent", Toast.LENGTH_LONG).show();
        }

/**
        mapView = this.findViewById<MapView>(R.id.restaurantDetails_mapView)
        if (mapView != null) {
            mapView!!.onCreate(null)
            mapView!!.onResume()
            //mapView!!.getMapAsync(this)
        }*/

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



    fun getSelectedRestaurant(id: Int, latitude: Double, longitude: Double){

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
                        displayRestaurant(selectedRestaurant)
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

    private fun displayRestaurant(restaurant: Restaurant) {
        val nameTextView = findViewById<TextView>(R.id.restaurantDetails_nomRestoTitleTextView)
        val imageView = findViewById<ImageView>(R.id.detailsImageView)
        val cuisineTextView = findViewById<TextView>(R.id.restaurantDetails_typeRestTextView)
        val ratingBar = this.findViewById<RatingBar>(R.id.restaurantDetails_ratingsRatingBar)
        val nbrsReviewTextView = this.findViewById<TextView>(R.id.restaurantDetails_nbrReviewsTextView)
        val kmTextView = this.findViewById<TextView>(R.id.restaurantDetails_kmDetailsTextView)

        val telephonetxtview = this.findViewById<TextView>(R.id.restaurantDetails_phoneTextView)
        val siteWeb = this.findViewById<TextView>(R.id.restaurantDetails_siteWebTextView)

        val lundiTxt = this.findViewById<TextView>(R.id.monHourTextView)
        val mardiTxt = this.findViewById<TextView>(R.id.tueHourTextView)
        val mercrediTxt = this.findViewById<TextView>(R.id.wedHourTextView)
        val jeudiTxt = this.findViewById<TextView>(R.id.thuHourTextView)
        val vendrediTxt = this.findViewById<TextView>(R.id.FriHourTextView)
        val samediTxt = this.findViewById<TextView>(R.id.satHourTextView)
        val dimancheTxt = this.findViewById<TextView>(R.id.sunHourTextView)

        val mapDetail = this.findViewById<View>(R.id.restaurantDetails_mapView)
        val nbrTxtReview = this.findViewById<TextView>(R.id.restaurantDetails_nbrReviews2TextView)

        //Set text and image
        nameTextView.setText(restaurant.name)
        cuisineTextView.setText(restaurant.type)
        nbrsReviewTextView.text = "(" + Integer.toString(restaurant.review_count) + ")"
        kmTextView.setText(objectID.distance)
        ratingBar.numStars = 5
        ratingBar.rating = restaurant.review_average
        Picasso.get().load(restaurant.image).into(imageView)



        telephonetxtview.setText(restaurant.phone_number)
        siteWeb.setText(restaurant.website)

        lundiTxt.setText(formatHours("MON", restaurant))
        mardiTxt.setText(formatHours("TUE", restaurant))
        mercrediTxt.setText(formatHours("WED", restaurant))
        jeudiTxt.setText(formatHours("THU", restaurant))
        vendrediTxt.setText(formatHours("FRI", restaurant))
        samediTxt.setText(formatHours("SAT", restaurant))
        dimancheTxt.setText(formatHours("SUN", restaurant))
/*
        var currentLatLng = LatLng(objectID.latitude, objectID.longitude)
        var zoomMap = 12.0f

        myMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoomMap))

        myMap?.apply {
            addMarker(
                    MarkerOptions()
                            .position(currentLatLng!!)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
        }*/


    }

    //"SUN" , "MON", "TUE", "WED", "THU", "FRI", "SAT"

    fun formatHours(day : String, restaurant: Restaurant) : String{

        var time = "Unknown"
        for (item in restaurant.opening_hours){
            if( item.day == day){
                time = item.opening_hour + " - " + item.closing_hour

            }
        }

        return time

    }
}

