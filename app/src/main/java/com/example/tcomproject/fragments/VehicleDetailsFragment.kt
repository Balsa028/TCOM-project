package com.example.tcomproject.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.tcomproject.R
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.utils.addEuroCurrency
import com.example.tcomproject.utils.formatDoubleForDisplay
import com.example.tcomproject.viewmodels.VehicleDetailsViewModel
import com.example.tcomproject.viewmodels.ViewModelFactory

class VehicleDetailsFragment(val vehicleId: Int) : BaseFragment() {

    private lateinit var viewModel: VehicleDetailsViewModel
    private lateinit var vehicleImageView: ImageView
    private lateinit var favoriteImageView: ImageView
    private lateinit var modelTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var backTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_details, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))[VehicleDetailsViewModel::class.java]
        viewModel.getVehicleDetails(vehicleId)
        observeChanges()
    }

    private fun observeChanges() {
        viewModel.getVehicleById().observe(viewLifecycleOwner) { vehicle ->
            vehicle?.let {
                setVehicleInformation(it)
            } ?: showAlertDialog(getString(R.string.dialog_title), getString(R.string.dialog_message_no_vehicle), getString(R.string.ok))
        }

        viewModel.getFreshStateOfVehiclesList().observe(viewLifecycleOwner) { list ->
            list?.let { vehicleList ->
                allVehicleList = vehicleList
                selectedVehicleList = getFilteredListBySelectedType(isAscendingSort)
            } ?: showAlertDialog(getString(R.string.dialog_title), getString(R.string.dialog_message_vehicles), getString(R.string.ok))

        }

        viewModel.successfullyAddedToFavorites().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                //hvatamo novo stanje liste sto ce automatski azurirati listu koja je dostupna u svim fragmentima (screenovima)
                viewModel.getAllVehiclesList()
                Glide.with(requireActivity())
                    .load(decideFavoriteIcon(isSuccess))
                    .into(favoriteImageView)
            } else {
                //ovde bi handlovao fail api poziva od AddToFavorites ali vidim da non stop vraca 200 (i ako sam promenio naziv parametra u body-u). Svakako ostavio sam ovako
            }
        }

        viewModel.isLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                showProgressDialog(getString(R.string.fetching_data))
            else
                dismissProgressDialog()
        }
    }

    private fun initView(view: View) {
        vehicleImageView = view.findViewById(R.id.vehicle_details_image_view)
        favoriteImageView = view.findViewById(R.id.heart_image_view_details)
        modelTextView = view.findViewById(R.id.model_textview)
        ratingTextView = view.findViewById(R.id.rating_textview)
        priceTextView = view.findViewById(R.id.cena_textview)
        latitudeTextView = view.findViewById(R.id.latitude_textview)
        longitudeTextView = view.findViewById(R.id.longitude_textview)
        backTextView = view.findViewById(R.id.back_button_text_view)
        backTextView.setOnClickListener {
            requireActivity().onBackPressed()
        }
        favoriteImageView.setOnClickListener {
            viewModel.addToFavorites(vehicleId)
        }
    }

    private fun setVehicleInformation(vehicle: Vehicle) {
        modelTextView.text = vehicle.name
        ratingTextView.text = vehicle.rating.toString()
        priceTextView.text = vehicle.price.toString().addEuroCurrency()
        latitudeTextView.text = vehicle.location.latitude.formatDoubleForDisplay()
        longitudeTextView.text = vehicle.location.longitude.formatDoubleForDisplay()

        Glide.with(requireActivity())
            .load(vehicle.imageURL)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(vehicleImageView)

        Glide.with(requireActivity())
            .load(decideFavoriteIcon(vehicle.isFavorite))
            .into(favoriteImageView)
    }

    private fun decideFavoriteIcon(isFavorite: Boolean): Drawable? {
        return if (isFavorite)
            ContextCompat.getDrawable(requireActivity(), R.drawable.heart_solid)
        else ContextCompat.getDrawable(requireActivity(), R.drawable.heart_outlined)
    }

    companion object {
        fun newInstance(id: Int) = VehicleDetailsFragment(id)
    }
}