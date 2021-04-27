package ca.ulaval.ima.mp.ui.map

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import ca.ulaval.ima.mp.R


class MapFragment : Fragment() {

    lateinit var locationManager: LocationManager
    private var hasGPS = true
    private var hasNetwork = false
    lateinit var cardView:View
    lateinit var selectedLayout : View
    lateinit var unselectedLayout : View
    private var locationGps : Location? = null
    private var locationNetWork : Location? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        cardView = root.findViewById(R.id.cardView)
        selectedLayout = root.findViewById(R.id.selectedLayout)
        unselectedLayout = root.findViewById(R.id.unselectedLayout)

        selectedLayout.setVisibility(View.GONE)
        getLocation()
        getLocation()


        return root
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getActivity()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGPS || hasNetwork){
            if(hasGPS){
                Log.d("Validation GPS : ", "Has GPS" )
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, object : LocationListener{
                    override fun onLocationChanged(location: Location) {
                        if(location != null){
                            locationGps = location
                            Log.d("Latidude GPS : ", locationGps!!.latitude.toString())
                            Log.d("Logitude GPS : ", locationGps!!.longitude.toString())
                        }
                    }

                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation!=null){
                    locationGps = localGpsLocation
                }
            }

            if(hasNetwork){
                Log.d("Validation Network : ", "Has NetWork" )
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, object : LocationListener{
                    override fun onLocationChanged(location: Location) {
                        if(location != null){
                            locationNetWork = location
                        }
                    }

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



}