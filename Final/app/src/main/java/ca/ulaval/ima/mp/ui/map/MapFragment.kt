package ca.ulaval.ima.mp.ui.map

import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.ulaval.ima.mp.R


class MapFragment : Fragment() {

    lateinit var locationManager: LocationManager
    lateinit var cardView:View
    lateinit var selectedLayout : View
    lateinit var unselectedLayout : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        cardView = root.findViewById(R.id.cardView)
        selectedLayout = root.findViewById(R.id.selectedLayout)
        unselectedLayout = root.findViewById(R.id.unselectedLayout)

        selectedLayout.setVisibility(View.GONE)


        return root
    }

}