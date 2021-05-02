package ca.ulaval.ima.mp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import ca.ulaval.ima.mp.MainActivity
import ca.ulaval.ima.mp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng


class MapFragment : Fragment(), OnMapReadyCallback {

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

    private lateinit var root : View


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
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            myMap?.setMyLocationEnabled(true);
            myMap?.getUiSettings()?.setMyLocationButtonEnabled(true);
        } else {
            Toast.makeText(requireContext(), "get permission map sauce", Toast.LENGTH_LONG).show();
        }*/

        getLocation()
        //getLocation()

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

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGPS || hasNetwork){
            if(hasGPS){
                Log.d("Validation GPS : ", "Has GPS")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            locationGps = location
                            Log.d("Latidude GPS : ", locationGps!!.latitude.toString())
                            Log.d("Logitude GPS : ", locationGps!!.longitude.toString())
                        }
                    }

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation!=null){
                    locationGps = localGpsLocation
                }
            }

            if(hasNetwork){
                Log.d("Validation Network : ", "Has NetWork")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (location != null) {
                            locationNetWork = location
                            currentLatLng = LatLng(location.latitude, location.longitude)

                            if (currentActivity is MainActivity) {
                                (currentActivity as MainActivity).setCurrentLatLng(currentLatLng)
                            }

                            //getNearbyRestaurant(currentLatLng?.latitude, currentLatLng?.longitude)
                        }


                    }

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation!=null){
                    locationNetWork = localNetworkLocation
                }
            }
            if (locationGps != null && locationNetWork != null){
                if (locationGps!!.accuracy > locationNetWork!!.accuracy){
                    Log.d("Latidude NetWork : ", locationNetWork!!.latitude.toString())
                    Log.d("Logitude NetWork : ", locationNetWork!!.longitude.toString())
                }else{
                    Log.d("Latidude GPS : ", locationGps!!.latitude.toString())
                    Log.d("Logitude GPS : ", locationGps!!.longitude.toString())
                }

            }
        }else{
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
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

            false
        }

        if (getActivity()?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED &&
                getActivity()?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Activez la localisation GPS de votre appareil", Toast.LENGTH_SHORT).show()
            return
        } else {
            //If we have permission, we get the location

            //If location is changing, update location
            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(myLocation: Location) {
                    //update location informations
                }

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            }

            //Get phone location

            //Get phone location
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)


        }

    }


}