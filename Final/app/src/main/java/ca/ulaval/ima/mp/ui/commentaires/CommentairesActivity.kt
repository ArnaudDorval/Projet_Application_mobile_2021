package ca.ulaval.ima.mp.ui.commentaires

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.PaginatedResultSerializer
import ca.ulaval.ima.mp.model.RestaurantLight
import ca.ulaval.ima.mp.model.Review
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import ca.ulaval.ima.mp.ui.liste.ListeFragment
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentairesActivity : AppCompatActivity() {
    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    var reviews : List<Review> = emptyList()
    lateinit var lv: ListView
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commentaires)
        lv = findViewById(R.id.restaurantDetails_nestedScrollView)
    }
    fun getReviews(){
        imaNetworkCenter.getRestaurantReviewById(1).enqueue(object :
            Callback<KungryAPI.ContentResponse<PaginatedResultSerializer<Review>>> {

            override fun onResponse(
                call: Call<KungryAPI.ContentResponse<PaginatedResultSerializer<Review>>>,
                response: Response<KungryAPI.ContentResponse<PaginatedResultSerializer<Review>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.results?.let {
                        Log.d("Test:", it.size.toString())
                        reviews = it
                        val adapter = ReviewsListAdapter(applicationContext,
                            R.layout.restaurant_reviews_list,
                            it
                        )
                        lv.adapter = adapter
                    }
                }
            }

            override fun onFailure(
                call: Call<KungryAPI.ContentResponse<PaginatedResultSerializer<Review>>>,
                t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure ${t.message}")
            }
        }
        )
    }
    private class ReviewsListAdapter(
        context: Context,
        private val cellResourceId: Int,
        private val listeReviews: List<Review>
    ) : ArrayAdapter<Review?>(context, cellResourceId, listeReviews) {
        lateinit var id: TextView
        lateinit var mImageView: ImageView
        lateinit var dateTextView : TextView
        lateinit var commentTextView: TextView
        lateinit var review_rating : RatingBar
        lateinit var nomTextView : TextView
        lateinit var prenomTextView : TextView

        override fun getCount(): Int {
            return listeReviews.size
        }

        override fun getItem(position: Int): Review {
            return listeReviews[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            var row= convertView
            if (row == null) {
                Log.d("ima", "Create new row")
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                row = inflater.inflate(cellResourceId, parent, false)
            }
            val review = getItem(position) //Get item
            row?.let {
                dateTextView = row.findViewById(R.id.restaurantReviews_dateTextView)
                nomTextView = row.findViewById(R.id.restaurantReviews_nomTextView)
                prenomTextView = row.findViewById(R.id.restaurantReviews_prenomTextView)
                mImageView = row.findViewById(R.id.restaurantReviews_imageView)
                review_rating = row.findViewById(R.id.restaurantReviews_ratingBar)
                commentTextView = row.findViewById(R.id.restaurantReviews_commentTextView)
            }
            dateTextView.text = review.date
            Picasso.get().load(review.image).into(mImageView)
            review_rating.rating = review.stars
            commentTextView.text = review.comment
            nomTextView.text = review.creator.last_name
            prenomTextView.text = review.creator.first_name


            return row!!
        }
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<KungryAPI.ContentResponse<PaginatedResultSerializer<Review>>>) {

}

