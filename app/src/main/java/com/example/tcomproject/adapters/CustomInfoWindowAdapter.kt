package com.example.tcomproject.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.tcomproject.R
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.utils.addEuroCurrency
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

class CustomInfoWindowAdapter(val context: Context, private val vehicles: List<Vehicle>) : GoogleMap.InfoWindowAdapter {

    private val view: View
    private var adapterListener: AdapterListener? = null
    private lateinit var parent: FrameLayout
    private lateinit var vehicleImageView: ImageView
    private lateinit var vehicleName: TextView
    private lateinit var vehiclePrice: TextView
    private lateinit var vehicleRating: TextView

    init {
        view = LayoutInflater.from(context).inflate(R.layout.vehicle_info_window_layout, null)
    }

    private fun setViewDetails(marker: Marker, view: View) {
        val vehicle = getVehicleFromList(marker.tag as Int)
        initView(view)
        vehicleName.text = vehicle.name
        vehicleRating.text = vehicle.rating.toString()
        vehiclePrice.text = vehicle.price.toString().addEuroCurrency()

        //ovde sam pokusao sa pikasom jer bolje loaduje slike u slucaju info window-a na google mapama (ne znam sto)
        //i dalje je ocaj loading... nisam stigao duze da pogledam ovo pa sam ostavio ovako i fokusirao se na funkcionalnost
        Picasso.get()
            .load(vehicle.imageURL)
            .resize(350, 170)
            .centerCrop()
            .into(vehicleImageView)

        parent.setOnClickListener {
            adapterListener?.onToVehicleDetailsScreen(vehicle.vehicleID)
        }

        //ikonicu srca za dodavanje na favorite nisam stavio u info window-u zato sto svakako Google ne dozvoljava click listenere unutar Info Window-a
        //iz istog razloga buttoni nemogu da se dodaju i imaju funkcionalnost registrovanja clicka
        //klik na ceo info window vodi na details screen gde moze da se doda u favorites
    }

    private fun getVehicleFromList(tag: Int): Vehicle = vehicles.first { it.vehicleID == tag }

    fun setAdapterListener(listener: AdapterListener) {
        this.adapterListener = listener
    }

    private fun initView(view: View) {
        parent = view.findViewById(R.id.parent)
        vehicleImageView = view.findViewById(R.id.vehicle_details_image_layout)
        vehicleName = view.findViewById(R.id.car_name_text_view)
        vehiclePrice = view.findViewById(R.id.price_text_view)
        vehicleRating = view.findViewById(R.id.rating_text_view)
    }

    override fun getInfoContents(marker: Marker): View? {
        setViewDetails(marker, view)
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        setViewDetails(marker, view)
        return view
    }
}