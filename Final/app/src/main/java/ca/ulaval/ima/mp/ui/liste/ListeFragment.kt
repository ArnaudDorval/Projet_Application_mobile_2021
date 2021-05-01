package ca.ulaval.ima.mp.ui.liste

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.PaginatedResultSerializer
import ca.ulaval.ima.mp.model.RestaurantLight
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import ca.ulaval.ima.mp.ui.RestaurantDetailsActivity
import ca.ulaval.ima.mp.ui.parcelables.ParcelDataAPI
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListeFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var recyclerviewadapter: RestaurantsRecyclerViewAdapter
    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)
    var restaurantLightList : List<RestaurantLight> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_liste, container, false)

        mRecyclerView = root.findViewById(R.id.recycler_view_Liste)
        mRecyclerView.layoutManager = LinearLayoutManager(this.context)

        getListOfRestaurant()



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
                        Log.d("Test:", it.size.toString())
                        restaurantLightList = it
                        recyclerviewadapter = RestaurantsRecyclerViewAdapter(restaurantLightList)
                        mRecyclerView.adapter = recyclerviewadapter
                        recyclerviewadapter.setOnClickListener {
                            val restoID = ParcelDataAPI(it.id,it.location)
                            val intent = Intent(context, RestaurantDetailsActivity::class.java)
                            intent.putExtra("restoID", restoID);
                            startActivity(intent)
                        }
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



    private class RestaurantsRecyclerViewAdapter(private val restaurantLightList : List<RestaurantLight>) : RecyclerView.Adapter<RestaurantsRecyclerViewAdapter.ViewHolder>() {
        lateinit var onItemClickListener: ((RestaurantLight) -> Unit)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            lateinit var view: View
            view = LayoutInflater.from(viewGroup.context).inflate(R.layout.restaurantlight_list, viewGroup, false)
            return ViewHolder(view)
        }


        override fun onBindViewHolder(holder: ViewHolder, i: Int) {
            val restaurant = restaurantLightList[i]
            holder.mItem = restaurant
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
            val mRestaurantName: TextView =  mView.findViewById(R.id.nameTextView)
            //val mCuisineType: TextView =  mView.findViewById(R.id)
            val mImageView: ImageView = mView.findViewById(R.id.imageView)
            var mItem: RestaurantLight? = null
        }

        fun setOnClickListener(onItemClickListener: ((RestaurantLight) -> Unit)) {
            this.onItemClickListener = onItemClickListener
        }

        companion object {
            private const val ASSETS_DIR = "images/"
            private const val SMALL_CELL_TYPE = 0
            private const val LARGE_CELL_TYPE = 1
        }
    }

}