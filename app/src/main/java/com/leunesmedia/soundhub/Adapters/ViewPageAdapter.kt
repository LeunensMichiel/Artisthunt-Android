package com.leunesmedia.soundhub.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPageAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()
    override fun getCount(): Int {
        return titles.count()
    }

    override fun getItem(p0: Int): Fragment {
        return fragments.get(p0)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
}