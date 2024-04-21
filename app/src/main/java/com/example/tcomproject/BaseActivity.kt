package com.example.tcomproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.BackStackEntry
import com.example.tcomproject.fragments.FavoritesFragment
import com.example.tcomproject.fragments.LoginFragment
import com.example.tcomproject.fragments.MapFragment
import com.example.tcomproject.fragments.VehicleDetailsFragment
import com.example.tcomproject.fragments.VehiclesFragment
import com.example.tcomproject.interfaces.BaseCoordinator
import com.example.tcomproject.utils.EMPTY_STRING
import com.example.tcomproject.utils.Util
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
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.maps -> {
                    replaceFragment(MapFragment.newInstance(), false)
                    true
                }
                R.id.vehicle_list -> {
                    replaceFragment(VehiclesFragment.newInstance(), false)
                    true
                }
                R.id.favorites -> {
                    replaceFragment(FavoritesFragment.newInstance(), false)
                    true
                }
                else -> false
            }
        }
    }

    private fun decideEntryScreen() {
        token = Util.getTokenFromSharedPrefs(this) ?: EMPTY_STRING
        replaceFragment(if (token == EMPTY_STRING) LoginFragment.newInstance() else MapFragment.newInstance(), false)
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val manager = supportFragmentManager
        if (!manager.isDestroyed) {
            manager.beginTransaction().apply {
                this.replace(R.id.fragment_container, fragment, fragment.javaClass.name)
                if (addToBackStack) this.addToBackStack(null)
                this.commit()
            }
        }
    }

    override fun onBottomNavigationVisible(isVisible: Boolean) {
        bottomNavigationView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun addFragment(fragment: Fragment, addToBackStack: Boolean) =
        replaceFragment(fragment, addToBackStack)


    private fun getActiveFragment() : Fragment? {
        val backStackEntry: BackStackEntry? = if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1) else null
        return backStackEntry?.let { supportFragmentManager.findFragmentByTag(it.name) }
    }

    override fun popFragmentBackstack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (getActiveFragment() is VehicleDetailsFragment)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }
}