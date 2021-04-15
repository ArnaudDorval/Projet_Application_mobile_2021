package ca.ulaval.ima.mp.ui.home

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.PaginatedResultSerializer
import ca.ulaval.ima.mp.model.RestaurantLight
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: RestaurantsRecyclerViewAdapter
    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    lateinit var restaurantLightList : List<RestaurantLight>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        mRecyclerView = root.findViewById(R.id.recycler_view_Restorants_List)

        getListOfRestaurant()
  /*      adapter.setOnCountryClickListener {

        }*/


        return root
    }

    fun getListOfRestaurant() {
        imaNetworkCenter.getListRestaurant(1,5).enqueue(object :
            Callback<KungryAPI.ContentResponse<PaginatedResultSerializer<RestaurantLight>>> {
            override fun onResponse(
                call: Call<KungryAPI.ContentResponse<PaginatedResultSerializer<RestaurantLight>>>,
                response: Response<KungryAPI.ContentResponse<PaginatedResultSerializer<RestaurantLight>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.content?.results?.let {
                        Log.d("Test:", " Good !!!!!!!!!!!!!!!!")
                    }
                }
                Log.d("Error", "Not Good !!!")
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



    private class RestaurantsRecyclerViewAdapter(private val restaurantLightList : List<RestaurantLight>) : RecyclerView.Adapter<RestaurantsRecyclerViewAdapter.ViewHolder>() {
        lateinit var onItemClickListener: ((RestaurantLight) -> Unit)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            lateinit var view: View
            view = LayoutInflater.from(viewGroup.context).inflate(R.layout.restaurantlight_list, viewGroup, false)
            return ViewHolder(view)
        }

        override fun getItemViewType(position: Int): Int {
            // Just as an example, return 0 or 2 depending on position
            // Note that unlike in ListView adapters, types don't have to be contiguous
            val restaurant = restaurantLightList[position]
            return 1
        }

        override fun onBindViewHolder(holder: ViewHolder, i: Int) {
            val restaurant = restaurantLightList[i]
            holder.mRestaurantName.text = restaurant.name
            //holder.mCuisineType.text = restaurant.cuisine
            Picasso.get().load(restaurant.image).into(holder.mImageView)

            holder.mView.setOnClickListener(){
                onItemClickListener.invoke(restaurant)
            }

        }

        override fun getItemCount(): Int {
            return restaurantLightList.size
        }

        inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
            val mRestaurantName: TextView =  mView.findViewById(R.id.restaurantNameTextView)
            val mCuisineType: TextView =  mView.findViewById(R.id.cuisineTextView)
            val mImageView: ImageView = mView.findViewById(R.id.imageView)

        }

        fun setOnCountryClickListener(onItemClickListener: ((RestaurantLight) -> Unit)) {
            this.onItemClickListener = onItemClickListener
        }



        companion object {
            private const val ASSETS_DIR = "images/"
            private const val SMALL_CELL_TYPE = 0
            private const val LARGE_CELL_TYPE = 1
        }
    }
}