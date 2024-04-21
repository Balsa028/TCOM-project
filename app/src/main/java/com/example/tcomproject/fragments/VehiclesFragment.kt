package com.example.tcomproject.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcomproject.R
import com.example.tcomproject.adapters.AdapterListener
import com.example.tcomproject.adapters.VehiclesAdapter
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.utils.AUTO
import com.example.tcomproject.utils.EMPTY_STRING
import com.example.tcomproject.utils.MOTOR
import com.example.tcomproject.utils.TRUCK
import com.example.tcomproject.utils.Util
import com.example.tcomproject.viewmodels.VehiclesViewModel
import com.example.tcomproject.viewmodels.ViewModelFactory


class VehiclesFragment : BaseFragment() {

    private lateinit var viewModel: VehiclesViewModel
    private lateinit var sortTextView: TextView
    private lateinit var autoTextView: TextView
    private lateinit var motorTextView: TextView
    private lateinit var truckTextView: TextView
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VehiclesAdapter
    private var popupMenu : PopupMenu? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vehicles, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application))[VehiclesViewModel::class.java]
        observeChanges()
    }

    override fun onResume() {
        super.onResume()

        if (allVehicleList.isEmpty())
            viewModel.getAllVehiclesList()
        else
            adapter.setVehicleList(getFilteredListBySelectedType(isAscendingSort))

    }

    private fun observeChanges() {
        viewModel.getFreshStateOfVehiclesList().observe(viewLifecycleOwner) { list ->
            list?.let { vehicleList ->
                allVehicleList = vehicleList
                selectedVehicleList = getFilteredListBySelectedType(isAscendingSort)
                adapter.setVehicleList(selectedVehicleList)
            } ?: run {
                adapter.clearList()
                showAlertDialog(getString(R.string.dialog_title), getString(R.string.dialog_message_vehicles), getString(R.string.ok))
            }
        }

        viewModel.isLoading().observe(viewLifecycleOwner) {
            viewModel.isLoading().observe(viewLifecycleOwner) { isLoading ->
                if (isLoading)
                    showProgressDialog(getString(R.string.fetching_data))
                else
                    dismissProgressDialog()
            }
        }

        viewModel.successfullyAddedToFavorites().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                //hvatamo novo stanje liste sto ce automatski azurirati listu koja je dostupna u svim fragmentima (screenovima)
                viewModel.getAllVehiclesList()
            } else {
                //ovde bi handlovao fail api poziva od AddToFavorites ali vidim da non stop vraca 200 (i ako sam promenio naziv parametra u body-u). Svakako ostavio sam ovako
            }
        }
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.vehicles_recycler_view)
        searchEditText = view.findViewById(R.id.search_edit_text)
        sortTextView = view.findViewById(R.id.sort_text_view)
        sortTextView.setOnClickListener { handleDropMenu(it) }
        setupRecyclerView()

        autoTextView = view.findViewById(R.id.car_text_view)
        motorTextView = view.findViewById(R.id.motor_text_view)
        truckTextView = view.findViewById(R.id.truck_text_view)
        decideTextColorBasedOnType(selectedVehicleType)

        autoTextView.setOnClickListener { handleTypesClickListenerEvent(AUTO) }
        motorTextView.setOnClickListener { handleTypesClickListenerEvent(MOTOR) }
        truckTextView.setOnClickListener { handleTypesClickListenerEvent(TRUCK) }

        searchEditText.doOnTextChanged { text, _ , _ , _ ->
            //search samo odradio sa contains metodom (mozda je trebalo sa startwith ali kontam da nema veze cim nije nigde specifirano)
            adapter.setVehicleList(filterListBySearch(text ?: EMPTY_STRING))
        }
    }

    private fun setupRecyclerView() {
        adapter = VehiclesAdapter(requireActivity())
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true

        adapter.setAdapterListener(object : AdapterListener {
            override fun onToVehicleDetailsScreen(vehicleId: Int) {
                coordinator?.addFragment(VehicleDetailsFragment.newInstance(vehicleId), true)
            }

            override fun onAddToFavorites(vehicleId: Int) {
                viewModel.addToFavorites(vehicleId)
            }
        })
        recyclerView.adapter = adapter
    }

    private fun handleDropMenu(view: View) {
        popupMenu?.show() ?: run {
            popupMenu = PopupMenu(requireActivity(), view)
            popupMenu?.menuInflater?.inflate(R.menu.sorting_menu, popupMenu?.menu)

            //defaultno preselektovano prvo jeftinije kao na dizajnu
            popupMenu?.menu?.findItem(R.id.first_cheapest)?.isChecked = true

            popupMenu?.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.first_cheapest -> {
                        menuItem.isChecked = true
                        popupMenu?.menu?.findItem(R.id.first_expensive)?.isChecked = false
                        isAscendingSort = true
                        adapter.setVehicleList(sortListAndReturn(isAscendingSort))
                        true
                    }

                    R.id.first_expensive -> {
                        menuItem.isChecked = true
                        popupMenu?.menu?.findItem(R.id.first_cheapest)?.isChecked = false
                        isAscendingSort = false
                        adapter.setVehicleList(sortListAndReturn(isAscendingSort))
                        true
                    }

                    else -> false
                }
            }
            popupMenu?.show()
        }
    }

    private fun decideTextColorBasedOnType(type: Int) {
        when (type) {
            AUTO -> {
                autoTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.light_orange))
                motorTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                truckTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            }
            MOTOR -> {
                autoTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                motorTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.light_orange))
                truckTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            }
            TRUCK -> {
                autoTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                motorTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                truckTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.light_orange))
            }
        }
    }

    private fun handleTypesClickListenerEvent(type: Int) {
        Util.saveSelectedVehicleType(type, requireActivity())
        selectedVehicleType = type
        selectedVehicleList = getFilteredListBySelectedType(isAscendingSort)
        decideTextColorBasedOnType(selectedVehicleType)
        adapter.setVehicleList(selectedVehicleList)
    }

    companion object {
        fun newInstance() = VehiclesFragment()
    }
}