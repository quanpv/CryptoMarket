package com.quanpv.cryptomarket.fragment

import android.view.View
import com.quanpv.cryptomarket.R

/**
 * Created by QuanPham on 5/31/18.
 */
class AllCurrencyListFragment : BaseFragment() {

    companion object {

        fun newInstance(): AllCurrencyListFragment {
            return AllCurrencyListFragment()
        }

    }

    override fun getViewId(): Int {
        return R.layout.fragment_all_currency_list
    }

    override fun init(view:View) {

    }
}