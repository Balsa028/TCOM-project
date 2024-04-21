package com.example.tcomproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tcomproject.R
import com.example.tcomproject.adapters.VehiclesAdapter
import com.example.tcomproject.models.Vehicle

class FavoritesFragment : BaseFragment() {

    private lateinit var favoritesRecView: RecyclerView
    private lateinit var adapter: VehiclesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setVehicleList(getFavoriteVehicles(viewModel.getAllVehicleList()))
    }

    private fun initView(view: View) {
        favoritesRecView = view.findViewById(R.id.favorite_vehicles_recycler_view)
        favoritesRecView.layoutManager = LinearLayoutManager(requireActivity())
        favoritesRecView.setHasFixedSize(true)
        favoritesRecView.isNestedScrollingEnabled = true

        adapter = VehiclesAdapter(requireActivity())
        adapter.setAdapterListener(this)
        favoritesRecView.adapter = adapter
    }

    private fun getFavoriteVehicles(list: List<Vehicle>): List<Vehicle> = list.filter { it.isFavorite }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

}