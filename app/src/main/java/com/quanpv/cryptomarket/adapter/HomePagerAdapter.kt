package com.quanpv.cryptomarket.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

import com.quanpv.cryptomarket.fragment.AllCurrencyListFragment
import com.quanpv.cryptomarket.fragment.FavoriteCurrencyListFragment

/**
 * Created by QuanPham on 5/31/18.
 */
class HomePagerAdapter constructor(private val mFragmentManager: FragmentManager) : FragmentPagerAdapter(mFragmentManager) {

    private val mFragmentTags: SparseArray<String>

    init {
        mFragmentTags = SparseArray()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val obj = super.instantiateItem(container, position)
        if (obj is Fragment) {
            val tag = obj.tag
            mFragmentTags.put(position, tag)
        }
        return obj
    }

    fun getFragment(position: Int): Fragment? {
        var fragment: Fragment? = null
        val tag = mFragmentTags.get(position)
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag)
        }
        return fragment
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return AllCurrencyListFragment.newInstance()
            1 -> return FavoriteCurrencyListFragment.newInstance()
        }
        return null
    }

    override fun getCount(): Int {
        // Total pages to show
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "All Coins"
            1 -> "Favorites"
            else -> null
        }
    }
}
