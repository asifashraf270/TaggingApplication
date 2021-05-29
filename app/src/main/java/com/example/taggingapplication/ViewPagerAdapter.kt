package com.example.taggingapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = TagFragment.getInstance()
            }
            1 -> {
                fragment = LocationFragment.getInstance()
            }
        }
        return fragment!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var string: String? = null
        when (position) {
            0 -> {
                string = "Hit List"
            }
            1 -> {
                string = "Location "
            }

        }
        return string

    }
}