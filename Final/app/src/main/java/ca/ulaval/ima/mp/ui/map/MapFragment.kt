package ca.ulaval.ima.mp.ui.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import ca.ulaval.ima.mp.MainActivity
import ca.ulaval.ima.mp.R
import ca.ulaval.ima.mp.model.PaginatedResultSerializer
import ca.ulaval.ima.mp.model.RestaurantLight
import ca.ulaval.ima.mp.networking.KungryAPI
import ca.ulaval.ima.mp.networking.NetworkCenter
import ca.ulaval.ima.mp.ui.RestaurantDetailsActivity
import ca.ulaval.ima.mp.ui.parcelables.ParcelDataAPI
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment(), OnMapReadyCallback {

    val imaNetworkCenter = NetworkCenter.buildService(KungryAPI::class.java)

    private var mapView: MapView? = null
    private var myMap: GoogleMap? = null
    lateinit var locationManager: LocationManager
    private var hasGPS = true
    private var hasNetwork = false
    lateinit var cardView:View
    lateinit var selectedLayout : View
    lateinit var unselectedLayout : View
    private var locationGps : Location? = null
    private var locationNetWork : Location? = null

    private var currentActivity: Activity? = null
    private var currentLatLng: LatLng? = null

    var zoomMap = 12.0f
    var restaurantList: List<RestaurantLight> = listOf()
    private var locationBitmap: Bitmap? = null
    private var personBitmap: Bitmap? = null
    private var blackBitmap: Bitmap? = null
    private lateinit var root : View
    private var thisMarker: Marker? = null

    lateinit var selectedRestaurant: RestaurantLight

    var restaurantLightList : List<RestaurantLight> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_map, container, false)


        cardView = root.findViewById(R.id.cardView)
        selectedLayout = root.findViewById(R.id.selectedLayout)
        unselectedLayout = root.findViewById(R.id.unselectedLayout)

        selectedLayout.setVisibility(View.GONE)
        locationManager = getActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        /**
        val pinIcon = resources.getDrawable(R.drawable.ic_icone_pin)
        val canvasLocation = Canvas()
        locationBitmap = Bitmap.createBitmap(pinIcon.intrinsicWidth, pinIcon.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvasLocation.setBitmap(locationBitmap)
        pinIcon.setBounds(0, 0, pinIcon.intrinsicWidth, pinIcon.intrinsicHeight)
        pinIcon.draw(canvasLocation)

        val personIcon = resources.getDrawable(R.drawable.ic_person_pin)
        val canvasperson = Canvas()
        personBitmap = Bitmap.createBitmap(personIcon.intrinsicWidth, personIcon.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvasperson.setBitmap(personBitmap)
        personIcon.setBounds(0, 0, personIcon.intrinsicWidth, personIcon.intrinsicHeight)
        personIcon.draw(canvasperson)*/
        /**
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            myMap?.setMyLocationEnabled(true);
            myMap?.getUiSettings()?.setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(requireContext(), "get permission map sauce", Toast.LENGTH_LONG).show();
        }*/

        //getLocation()
        //getLocation()
        cardView.setOnClickListener {



            if (selectedRestaurant != null) {
                //Start other activity to display detailed offer
                val restaurantDetailsActivity = Intent(context, RestaurantDetailsActivity::class.java)
                // Gotta do another request for more detailed information
                val fucku = ParcelDataAPI(selectedRestaurant.id, selectedRestaurant.location.latitude, selectedRestaurant.location.longitude, selectedRestaurant.distance);
                restaurantDetailsActivity.putExtra("restaurant", fucku)
                startActivity(restaurantDetailsActivity)
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        if (mapView != null) {
            mapView!!.onCreate(null)
            mapView!!.onResume()
            mapView!!.getMapAsync(this)
        }
    }

    override fun onMapReady(p0: GoogleMap?) {

        MapsInitializer.initialize(context)

        //Set location (LatLng)
        currentActivity = activity
        myMap = p0

        //create listner to display restaurant summary
        myMap!!.setOnMarkerClickListener { marker ->
            var selectedRestaurantId = 0
            unselectedLayout.visibility = View.GONE
            selectedLayout.visibility = View.VISIBLE
            val blackDrawable = resources.getDrawable(R.drawable.ic_icone_pin_noer)
            val canvasB = Canvas()
            blackBitmap = Bitmap.createBitmap(blackDrawable.intrinsicWidth, blackDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            canvasB.setBitmap(blackBitmap)
            blackDrawable.setBounds(0, 0, blackDrawable.intrinsicWidth, blackDrawable.intrinsicHeight)
            blackDrawable.draw(canvasB)

            if (thisMarker != null && thisMarker !== marker) {
                thisMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(locationBitmap))
            }

            if (marker != thisMarker) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(blackBitmap))
                thisMarker = marker
            }

            for (item in restaurantList){
                if(item.name == marker.title){
                    selectedRestaurant = item
                    displayRestaurant(item);
                    break
                }
            }


            false
        }

        if (currentActivity?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED &&
                currentActivity?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Activez la localisation GPS de votre appareil", Toast.LENGTH_SHORT).show()
            return
        } else {
            //If we have permission, we get the location

            //If location is changing, update location
            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationGps = location
                    Log.d("Latidude GPS : ", locationGps!!.latitude.toString())
                    Log.d("Logitude GPS : ", locationGps!!.longitude.toString())


                    currentLatLng = LatLng(location.latitude, location.longitude)

                    if (currentActivity is MainActivity) {
                        if(currentLatLng != null){
                            (currentActivity as MainActivity).setCurrentLatLng(currentLatLng)
                        }
                    }

                    getRestaurantNearby(locationGps!!.latitude, locationGps!!.longitude);
                }

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            }

            //Get phone location

            //Get phone location
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)

            if(locationGps != null){
                getRestaurantNearby(locationGps!!.latitude, locationGps!!.longitude);

                currentLatLng = LatLng(locationGps!!.latitude, locationGps!!.longitude)

                if (currentActivity is MainActivity) {
                    if(currentLatLng != null){
                        (currentActivity as MainActivity).setCurrentLatLng(currentLatLng)
                    }

                }
            }


        }

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
                            Toast.makeText(requireContext(), "nb restaurant proche " + it.size.toString(), Toast.LENGTH_LONG).show();
                            restaurantList = emptyList()

                            //Display the map and zoom near user's location
                            myMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoomMap))


                            //Convert icon to bitmap to display on the map

                            val personIconDrawable = resources.getDrawable(R.drawable.ic_person_pin)
                            val canvas = Canvas()
                            personBitmap = Bitmap.createBitmap(personIconDrawable.intrinsicWidth, personIconDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                            canvas.setBitmap(personBitmap)
                            personIconDrawable.setBounds(0, 0, personIconDrawable.intrinsicWidth, personIconDrawable.intrinsicHeight)
                            personIconDrawable.draw(canvas)

                            val pinIconDrawable = resources.getDrawable(R.drawable.ic_icone_pin)
                            val canvasA = Canvas()
                            locationBitmap = Bitmap.createBitmap(pinIconDrawable.intrinsicWidth, pinIconDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                            canvasA.setBitmap(locationBitmap)
                            pinIconDrawable.setBounds(0, 0, pinIconDrawable.intrinsicWidth, pinIconDrawable.intrinsicHeight)
                            pinIconDrawable.draw(canvasA)
                            //myMap!!.addMarker(MarkerOptions().position(currentLatLng!!).title("You")))

                            myMap?.apply {
                                addMarker(
                                        MarkerOptions()
                                                .position(currentLatLng!!)
                                                .title("you")
                                                .icon(BitmapDescriptorFactory.fromBitmap(personBitmap))
                                )
                            }

                            restaurantList = it
                            for (item in it) {
                                println(item.name)

                                val newLatlng = LatLng(item.location.latitude, item.location.longitude)
                                myMap?.apply {
                                    addMarker(
                                            MarkerOptions()
                                                    .position(newLatlng)
                                                    .title(item.name)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(locationBitmap))
                                    )
                                }
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


    private fun displayRestaurant(restaurant: RestaurantLight) {
        val nameTextView = root!!.findViewById<TextView>(R.id.mapNameTextView)
        val imageView = root!!.findViewById<ImageView>(R.id.mapImageView)
        val cuisineTextView = root!!.findViewById<TextView>(R.id.mapCuisineTextView)
        val ratingBar = root!!.findViewById<RatingBar>(R.id.mapRatingBar)
        val nbrsReviewTextView = root!!.findViewById<TextView>(R.id.mapNbrsReviewTextView)
        val kmTextView = root!!.findViewById<TextView>(R.id.mapKmTextView)

        //Set text and image
        nameTextView.setText(restaurant.name)
        cuisineTextView.setText(restaurant.type)
        nbrsReviewTextView.text = "(" + Integer.toString(restaurant.review_count) + ")"
        kmTextView.setText(restaurant.distance)
        ratingBar.numStars = 5
        ratingBar.rating = restaurant.review_average
        Picasso.get().load(restaurant.image).into(imageView)
    }
}

//
//46.782995
//D/Logitude GPS :: -71.28606833333333