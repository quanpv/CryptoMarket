package com.quanpv.cryptomarket.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by QuanPham on 5/31/18.
 */
abstract class BaseFragment : Fragment() {

    var mContext: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getViewId(), container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    @Override
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

    }

    abstract fun getViewId(): Int

    abstract fun init(view: View)
}