package com.example.tcomproject.interfaces

import androidx.fragment.app.Fragment

interface BaseCoordinator {
    fun onBottomNavigationVisible(isVisible: Boolean)
    fun addFragment(fragment: Fragment, addToBackStack: Boolean)
}