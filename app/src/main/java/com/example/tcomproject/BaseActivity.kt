package com.example.tcomproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.BackStackEntry
import com.example.tcomproject.fragments.LoginFragment
import com.example.tcomproject.fragments.MapFragment
import com.example.tcomproject.interfaces.BaseCoordinator
import com.example.tcomproject.utils.EMPTY_STRING
import com.google.android.material.bottomnavigation.BottomNavigationView

class BaseActivity : AppCompatActivity(), BaseCoordinator {

    private var token = ""
    private lateinit var bottomNavigationView :BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        decideEntryScreen()
    }

    private fun initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
    }

    private fun decideEntryScreen() =
        replaceFragment(if (token == EMPTY_STRING) LoginFragment.newInstance() else MapFragment.newInstance())

    private fun replaceFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        if (!manager.isDestroyed) {
            manager.beginTransaction()
                .replace(R.id.fragment_container, fragment, fragment.javaClass.name)
                .commit()
        }
    }

    override fun onBottomNavigationVisible(isVisible: Boolean) {
        bottomNavigationView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun addFragment(fragment: Fragment) = replaceFragment(fragment)

}