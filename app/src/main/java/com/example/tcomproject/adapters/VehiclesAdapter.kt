package com.example.tcomproject.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.tcomproject.R
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.utils.addEuroCurrency

class VehiclesAdapter(val context: Context) : RecyclerView.Adapter<VehiclesAdapter.VehicleViewHolder>() {

    private var vehicleList: List<Vehicle> = emptyList()
    private var adapterListener: AdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_list_item, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        holder.vehicleName.text = vehicleList.get(position).name
        holder.vehicleRating.text = vehicleList.get(position).rating.toString()
        holder.vehiclePrice.text = vehicleList.get(position).price.toString().addEuroCurrency()

        Glide.with(context)
            .load(vehicleList.get(position).imageURL)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(holder.vehicleImageView)

        Glide.with(context)
            .load(decideFavoriteDrawable(vehicleList.get(position)))
            .into(holder.favoritesImageView)

        holder.favoritesImageView.setOnClickListener() {
            if (!vehicleList.get(position).isFavorite) {
                adapterListener?.onAddToFavorites(vehicleList.get(position).vehicleID)
            } else {
                // Ovde bi isla logika za Unfavorite ali vidim da nema api poziva za to.
            }
        }

        holder.parent.setOnClickListener {
            adapterListener?.onToVehicleDetailsScreen(vehicleList.get(position).vehicleID)
        }
    }

    override fun getItemCount() = vehicleList.size

    fun setVehicleList(newList: List<Vehicle>) {
        vehicleList = newList
        notifyDataSetChanged()
    }

    fun setAdapterListener(listener: AdapterListener) {
        this.adapterListener = listener
    }

    fun clearList() {
        vehicleList = emptyList()
    }

    private fun decideFavoriteDrawable(vehicle: Vehicle) : Drawable {
        return if (vehicle.isFavorite)
            ContextCompat.getDrawable(context, R.drawable.heart_solid)!!
        else
            ContextCompat.getDrawable(context, R.drawable.heart_outlined)!!
    }

    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parent: CardView
        val vehicleImageView: ImageView
        val favoritesImageView: ImageView
        val vehicleName: TextView
        val vehiclePrice: TextView
        val vehicleRating: TextView

        init {
            parent = itemView.findViewById(R.id.parent)
            vehicleImageView = itemView.findViewById(R.id.vehicle_details_image_layout)
            favoritesImageView = itemView.findViewById(R.id.heart_image_view)
            vehicleName = itemView.findViewById(R.id.car_name_text_view)
            vehiclePrice = itemView.findViewById(R.id.price_text_view)
            vehicleRating = itemView.findViewById(R.id.rating_text_view)
        }
    }

}