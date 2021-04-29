package ca.ulaval.ima.mp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import ca.ulaval.ima.mp.R

class RestaurantDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

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
    }
}