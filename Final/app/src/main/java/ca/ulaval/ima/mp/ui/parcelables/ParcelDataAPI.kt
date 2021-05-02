package ca.ulaval.ima.mp.ui.parcelables
import android.os.Parcel
import android.os.Parcelable
import ca.ulaval.ima.mp.model.Location
import com.google.android.gms.maps.model.LatLng

//Classe générée automatiquement via Android Studio, permet l'envois d'objets parcelables
class ParcelDataAPI (val restoID: Int?, val latitude : Double, val longitude : Double): Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readDouble(),
            parcel.readDouble()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(restoID)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelDataAPI> {
        override fun createFromParcel(parcel: Parcel): ParcelDataAPI {
            return ParcelDataAPI(parcel)
        }

        override fun newArray(size: Int): Array<ParcelDataAPI?> {
            return arrayOfNulls(size)
        }
    }

}

