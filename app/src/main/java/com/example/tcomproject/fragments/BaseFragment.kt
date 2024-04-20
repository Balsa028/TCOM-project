package com.example.tcomproject.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tcomproject.interfaces.BaseCoordinator
import com.example.tcomproject.utils.Util

open class BaseFragment : Fragment() {

    protected var coordinator: BaseCoordinator? = null
    private var progressDialog: ProgressDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
}