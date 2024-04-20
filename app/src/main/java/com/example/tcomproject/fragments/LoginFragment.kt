package com.example.tcomproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tcomproject.R
import com.example.tcomproject.utils.Util
import com.example.tcomproject.viewmodels.LoginViewModel
import com.example.tcomproject.viewmodels.ViewModelFactory
import androidx.fragment.app.viewModels


class LoginFragment : BaseFragment() {

    private lateinit var emailEditText: EditText
    private lateinit var loginBtn: Button
    private val viewModel: LoginViewModel by viewModels { ViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        emailEditText = view.findViewById(R.id.email_edit_text)
        loginBtn = view.findViewById(R.id.btnLogin)

        loginBtn.setOnClickListener {
            viewModel.initiateLogin(emailEditText.text.toString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeChanges()
    }

    private fun observeChanges() {
        viewModel.isLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading)
                showProgressDialog(getString(R.string.checking_credentials))
            else
                dismissProgressDialog()
        }

        viewModel.isLoginSuccess().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireActivity(), getString(R.string.successfully_logged_in), Toast.LENGTH_SHORT).show()
                coordinator?.addFragment(MapFragment.newInstance())
            } else {
                showAlertDialog(getString(R.string.dialog_title), getString(R.string.dialog_message), getString(R.string.ok))
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}