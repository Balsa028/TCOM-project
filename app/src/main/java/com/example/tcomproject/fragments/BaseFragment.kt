package com.example.tcomproject.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tcomproject.interfaces.BaseCoordinator
import com.example.tcomproject.models.Vehicle
import com.example.tcomproject.utils.AUTO
import com.example.tcomproject.utils.Util

open class BaseFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null
    protected var coordinator: BaseCoordinator? = null

    protected var selectedVehicleType = AUTO;
    protected var selectedVehicleList: List<Vehicle> = emptyList()
    protected var allVehicleList: List<Vehicle> = emptyList()
    protected var isAscendingSort = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //uzimanje zadnje selektovanog tipa vozila iz sher prefs radi cuvanja podatka ukoliko user izadje iz app-a pa udje. Isto je moglo da se uradi sa sortiranjem ali nisam :)
        selectedVehicleType = Util.getSelectedVehicleTypeFromSharedPrefs(context)

        if (context is BaseCoordinator)
            coordinator = context
    }

    override fun onResume() {
        super.onResume()
        handleBottomNavigationVisibility()
    }

    private fun handleBottomNavigationVisibility() {
        coordinator?.onBottomNavigationVisible(this !is LoginFragment)
    }

    protected fun showProgressDialog(message: String) {
        if (progressDialog == null) progressDialog = ProgressDialog(requireActivity())
        progressDialog?.setMessage(message)
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    protected fun dismissProgressDialog() = progressDialog?.dismiss()

    protected fun showAlertDialog(title: String, message: String, buttonText: String) {
        AlertDialog.Builder(requireActivity())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(buttonText) { dialogInterface, _ -> dialogInterface.dismiss() }
            .create().show()
    }

    protected fun getFilteredListBySelectedType(isAscending: Boolean): List<Vehicle> {
        val returnList = allVehicleList.filter { it.vehicleTypeID == selectedVehicleType }
        return if (isAscending) returnList.sortedBy { it.price } else returnList.sortedByDescending { it.price }
    }

    protected fun sortListAndReturn(isAscending: Boolean): List<Vehicle> =
        if (isAscending) selectedVehicleList.sortedBy { it.price }
        else selectedVehicleList.sortedByDescending { it.price }

    protected fun filterListBySearch(searchText: CharSequence) = selectedVehicleList.filter { it.name.contains(searchText, ignoreCase = true) }
}