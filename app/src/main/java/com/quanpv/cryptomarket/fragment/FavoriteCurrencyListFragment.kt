package com.quanpv.cryptomarket.fragment

import android.view.View
import com.quanpv.cryptomarket.R

/**
 * Created by QuanPham on 5/31/18.
 */
class FavoriteCurrencyListFragment : BaseFragment() {

    companion object {

        fun newInstance(): FavoriteCurrencyListFragment {
            return FavoriteCurrencyListFragment()
        }

    }

    override fun getViewId(): Int {
        return   R.layout.fragment_favorite_currency_list
    }

    override fun init(view: View) {

    }
}