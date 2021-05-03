package ca.ulaval.ima.mp

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ca.ulaval.ima.mp.model.TokenOutput
import ca.ulaval.ima.mp.utils.Token
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var currentLatLng: LatLng? = null
    private var myToken: TokenOutput? = null
    private var client_id : String = " STO4WED2NTDDxjLs8ODios5M15HwsrRlydsMa1t0"
    private var client_secret : String = "YOVWGpjSnHd5AYDxGBR2CIB09ZYM1OPJGnH3ijkKwrUMVvwLprUmLf6fxku06ClUKTAEl5AeZN36V9QYBYvTtrLMrtUtXVuXOGWleQGYyApC2a469l36TdlXFqAG1tpK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(this, "get permission map", Toast.LENGTH_LONG).show();
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_map, R.id.navigation_liste, R.id.navigation_connexion))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun setCurrentLatLng(location: LatLng?) {
        currentLatLng = location
    }

    fun getCurrentLatLng(): LatLng? {
        return currentLatLng
    }

    fun setCurrentToken(token: TokenOutput?) {
        myToken = token
    }

    fun getCurrentToken(): TokenOutput? {
        return myToken
    }

    fun getClientId(): String {
        return client_id
    }

    fun getClientSecret(): String {
        return client_secret
    }
}