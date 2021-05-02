package ca.ulaval.ima.mp.ui.liste

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ca.ulaval.ima.mp.MainActivity
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.PaginatedResultSerializer
import ca.ulaval.ima.mp.model.RestaurantLight
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import ca.ulaval.ima.mp.ui.RestaurantDetailsActivity
import ca.ulaval.ima.mp.ui.parcelables.ParcelDataAPI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListeFragment : Fragment() {
    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    var restaurantLightList : List<RestaurantLight> = emptyList()
    lateinit var lv: ListView
    var currentLatLng: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_liste, container, false)

        lv = root.findViewById(R.id.mylisteView)
        currentLatLng = (activity as MainActivity).getCurrentLatLng()

        currentLatLng?.let { getRestaurantNearby(it.latitude, currentLatLng!!.longitude) }

        lv.setOnItemClickListener { adapter, view, position, id ->
            val item = adapter.getItemAtPosition(position) as RestaurantLight
            Log.d("demo", "selected Brand:${item.name}, selected Id:${item.id}")
            val intent = Intent(this.context, RestaurantDetailsActivity::class.java)
            var objectID = ParcelDataAPI(item.id, 0.0,0.0)
            intent.putExtra("restoID", objectID)
            startActivity(intent)
        }
        return root
    }

    fun getRestaurantNearby(latitude: Double, longitude: Double){
        imaNetworkCenter.getRestaurantSearchNoText(1, 16, latitude, longitude, 10).enqueue(object :
            Callback<KungryAPI.ContentResponse<PaginatedResultSerializer<RestaurantLight>>> {

            override fun onResponse(
                call: Call<KungryAPI.ContentResponse<PaginatedResultSerializer<RestaurantLight>>>,
                response: Response<KungryAPI.ContentResponse<PaginatedResultSerializer<RestaurantLight>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.results?.let {
                        Log.d("Test:", it.size.toString())
                        restaurantLightList = it
                        val adapter = RestaurantsListAdapter(
                            requireContext(),
                            R.layout.restaurantlight_list,
                            it
                        )
                        lv.adapter = adapter
                    }
                }
            }

            override fun onFailure(
                call: Call<KungryAPI.ContentResponse<PaginatedResultSerializer<RestaurantLight>>>,
                t: Throwable
            ) {
                Log.d("ima-demo", "listRestaurants Failure ${t.message}")
            }
        }
        )
    }


    private class RestaurantsListAdapter(
        context: Context,
        private val cellResourceId: Int,
        private val listeRestaurant: List<RestaurantLight>
    ) : ArrayAdapter<RestaurantLight?>(context, cellResourceId, listeRestaurant) {
        lateinit var restaurantName: TextView
        lateinit var mImageView: ImageView
        lateinit var distanceTextView : TextView
        lateinit var reviewCountTextView : TextView
        lateinit var review_average : RatingBar
        lateinit var cuisineTextView : TextView

        override fun getCount(): Int {
            return listeRestaurant.size
        }

        override fun getItem(position: Int): RestaurantLight {
            return listeRestaurant[position]
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
            val restaurantLight = getItem(position) //Get item
            row?.let {
                restaurantName = row.findViewById(R.id.nameTextView)//Set Name
                mImageView = row.findViewById(R.id.imageView)
                distanceTextView = row.findViewById(R.id.kmTextView)
                reviewCountTextView= row.findViewById(R.id.nbrsReviewTextView)
                review_average = row.findViewById(R.id.ratingBar)
                cuisineTextView = row.findViewById(R.id.cuisineTextView)
            }
            restaurantName.text = restaurantLight.name
            Picasso.get().load(restaurantLight.image).into(mImageView)
            reviewCountTextView.text = "(" + restaurantLight.review_count.toString() + ")"
            distanceTextView.text = restaurantLight.distance
            review_average.rating = restaurantLight.review_average
            cuisineTextView.text = restaurantLight.cuisine[0].name

            return row!!
        }
    }
}