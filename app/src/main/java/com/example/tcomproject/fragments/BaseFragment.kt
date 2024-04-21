package com.example.tcomproject.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tcomproject.adapters.AdapterListener
import com.example.tcomproject.interfaces.BaseCoordinator
import com.example.tcomproject.utils.AUTO
import com.example.tcomproject.utils.Util
import com.example.tcomproject.viewmodels.VehiclesViewModel
import com.example.tcomproject.viewmodels.ViewModelFactory

open class BaseFragment : Fragment(), AdapterListener {

    private var progressDialog: ProgressDialog? = null
    protected var coordinator: BaseCoordinator? = null
    protected var selectedVehicleType = AUTO;
    protected var isAscendingSort = true

    protected val viewModel: VehiclesViewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelFactory(requireActivity().application))[VehiclesViewModel::class.java]
    }

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
        coordinator?.onBottomNavigationVisible(this !is LoginFragment && this !is VehicleDetailsFragment)
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

        override fun onToVehicleDetailsScreen(vehicleId: Int) {
            coordinator?.addFragment(VehicleDetailsFragment.newInstance(vehicleId), true)
        }

        override fun onAddToFavorites(vehicleId: Int) {
            viewModel.addToFavorites(vehicleId)
        }
}