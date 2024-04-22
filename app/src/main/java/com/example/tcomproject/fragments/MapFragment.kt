package com.example.tcomproject.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tcomproject.R
import com.example.tcomproject.adapters.CustomInfoWindowAdapter
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.utils.BELGRADE_LATITUDE
import com.example.tcomproject.utils.BELGRADE_LONGITUDE
import com.example.tcomproject.utils.addEuroCurrency
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : BaseFragment(), OnMapReadyCallback {

    //kada se klikne na marker prvi put posle loadovanja info window se ne otvara (ako se samo malo pomeri mapa on ce se pojaviti lol :)
    //uglavnom svaki sledeci pokusaj klika radi kako treba ali za ovo nisam imao vremena malo vise da istrazim.

    private var googleMaps: GoogleMap? = null
    private lateinit var customInfoAdapter: CustomInfoWindowAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getVehiclesData()
        observeChanges()

        val googleMapFragment = childFragmentManager.findFragmentById(R.id.map_view) as? SupportMapFragment
        googleMapFragment?.getMapAsync(this)
    }

    private fun observeChanges() {
        viewModel.getFreshStateOfVehiclesList().observe(viewLifecycleOwner) { list ->
            list?.let {
                viewModel.updateAllVehicleList(list)
                viewModel.updateSelectedVehicleList(viewModel.getFilteredListBySelectedType(isAscendingSort, selectedVehicleType))
            }
        }
        viewModel.selectedVehicleListLiveData.observe(viewLifecycleOwner) { list ->
            updateMarkersOnMap(googleMaps, viewModel.checkSearchFieldAndReturnList(viewModel.searchText, list))
        }

        viewModel.successfullyAddedToFavorites().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                viewModel.getAllVehiclesList()
            } else {
                //ovde bi handlovao fail api poziva od AddToFavorites ali vidim da non stop vraca 200 (i ako sam promenio naziv parametra u body-u). Svakako ostavio sam ovako
            }
        }
    }

    private fun updateMarkersOnMap(googleMap: GoogleMap?, selectedVehicles: List<Vehicle>) {
        if (googleMap != null && selectedVehicles.isNotEmpty()) {
            initializeCustomWindowAdapter(googleMap)
            selectedVehicles.forEach { vehicle ->
                val marker = googleMap.addMarker(getMarkerOptions(vehicle))
                marker?.tag = vehicle.vehicleID
            }
        }
    }

    private fun getVehiclesData() {
        if (viewModel.getAllVehicleList().isEmpty())
            viewModel.getAllVehiclesList()
    }

    private fun getMarkerOptions(vehicle: Vehicle): MarkerOptions {
        val vehicleLatLng = LatLng(vehicle.location.latitude, vehicle.location.longitude)
        return MarkerOptions()
            .position(vehicleLatLng)
            .icon(createCustomMarker(requireActivity(), vehicle.price.toString().addEuroCurrency()))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMaps = googleMap
        val belgradeLocation = LatLng(BELGRADE_LATITUDE, BELGRADE_LONGITUDE)
        updateMarkersOnMap(googleMaps, viewModel.checkSearchFieldAndReturnList(viewModel.searchText, viewModel.getSelectedVehicleList()))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(belgradeLocation, 11F))
    }

    private fun initializeCustomWindowAdapter(googleMap: GoogleMap) {
        customInfoAdapter = CustomInfoWindowAdapter(requireActivity(), viewModel.getSelectedVehicleList()).apply {
            setAdapterListener(this@MapFragment)
        }
        googleMap.setInfoWindowAdapter(customInfoAdapter)
        googleMap.setOnInfoWindowClickListener {
            onToVehicleDetailsScreen(viewModel.findVehicleId(it.tag as Int))
        }
    }

    private fun createCustomMarker(context: Context, price: String): BitmapDescriptor {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_marker_layout, null)
        val priceTextView = view.findViewById<TextView>(R.id.marker_price_textview)
        priceTextView.text = price

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0 ,0 , view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}