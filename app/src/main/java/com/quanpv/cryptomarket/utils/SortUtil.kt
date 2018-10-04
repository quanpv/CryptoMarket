package com.quanpv.cryptomarket.utils


import android.util.Log
import com.quanpv.cryptomarket.model.CryptoCoin
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator

/**
 * Created by QuanPV on 6/4/2018.
 */

object SortUtil {
    fun sortList(currencyList: ArrayList<CryptoCoin>, number: Int) {
        when (number) {
        // Name A-Z
            0 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs -> lhs.name!!.compareTo(rhs.name!!) }
        // Market Cap
            1 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs -> lhs.rank?.toFloat()!!.compareTo(rhs.rank!!.toFloat()) }
        // Price
            2 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.price_usd == null && rhs.price_usd == null) {
                    Log.i("NULL0", "----->")
                    return@sort 0
                }
                if (lhs.price_usd == null) {
                    Log.i("NULL2", "----->")
                    return@sort 1
                }
                if (rhs.price_usd == null) {
                    Log.i("NULL2", "----->")
                    return@sort -1
                }
                return@sort rhs.price_usd!!.toDouble()!!.compareTo(lhs.price_usd!!.toDouble())

            }
        // Volume 24h
            3 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.day_volume_usd == null && rhs.day_volume_usd == null) {
                    return@sort 0
                }
                if (lhs.day_volume_usd == null) {
                    return@sort 1
                }
                if (rhs.day_volume_usd == null) {
                    return@sort -1
                }
                return@sort rhs.day_volume_usd!!.toFloat()!!.compareTo(lhs.day_volume_usd!!.toFloat())
            }
        // Change 1h
            4 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.percent_change_1h == null && rhs.percent_change_1h == null) {
                    return@sort 0
                }
                if (lhs.percent_change_1h == null) {
                    return@sort 1
                }
                if (rhs.percent_change_1h == null) {
                    return@sort -1
                }
                Log.i("percent_change_1h", "----->")
                return@sort rhs.percent_change_1h!!.toFloat().compareTo(rhs.percent_change_1h!!.toFloat())
            }
        // Change 24h
            5 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.percent_change_24h == null && rhs.percent_change_24h == null) {
                    return@sort 0
                }
                if (lhs.percent_change_24h == null) {
                    return@sort 1
                }
                if (rhs.percent_change_24h == null) {
                    return@sort -1
                }
                return@sort rhs.percent_change_24h!!.toFloat().compareTo(rhs.percent_change_24h!!.toFloat())
            }
        // Change 7d
            6 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.percent_change_7d == null && rhs.percent_change_7d == null) {
                    return@sort 0
                }
                if (lhs.percent_change_7d == null) {
                    return@sort 1
                }
                if (rhs.percent_change_7d == null) {
                    return@sort -1
                }
                return@sort rhs.percent_change_7d!!.toFloat().compareTo(rhs.percent_change_7d!!.toFloat())
            }
        // Name Z-A
            7 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs -> rhs.name!!.compareTo(lhs.name!!) }
        // Market Cap LH
            8 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs -> rhs.rank?.toInt()!!.compareTo(lhs.rank!!.toInt()) }
        // Price LH
            9 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.price_usd == null && rhs.price_usd == null) {
                    return@sort 0
                }
                if (lhs.price_usd == null || rhs.price_usd == null) {
                    return@sort rhs.rank?.toInt()!!.compareTo(lhs.rank!!.toInt())
                }
                return@sort lhs.price_usd!!.toFloat().compareTo(rhs.price_usd!!.toFloat())
            }
        // Volume 24h LH
            10 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.day_volume_usd == null && rhs.day_volume_usd == null) {
                    return@sort 0
                }
                if (lhs.day_volume_usd == null) {
                    return@sort 1
                }
                if (rhs.day_volume_usd == null) {
                    return@sort -1
                }
                lhs.day_volume_usd!!.toFloat().compareTo(rhs.day_volume_usd!!.toFloat())
            }
        // Change 1h LH
            11 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.percent_change_1h == null && rhs.percent_change_1h == null) {
                    return@sort 0
                }
                if (lhs.percent_change_1h == null) {
                    return@sort 1
                }
                if (rhs.percent_change_1h == null) {
                    return@sort -1
                }
                lhs.percent_change_1h!!.toFloat().compareTo(rhs.percent_change_1h!!.toFloat())
            }
        // Change 24h LH
            12 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.percent_change_24h == null && rhs.percent_change_24h == null) {
                    return@sort 0
                }
                if (lhs.percent_change_24h == null) {
                    return@sort 1
                }
                if (rhs.percent_change_24h == null) {
                    return@sort -1
                }
                return@sort lhs.percent_change_24h!!.toFloat().compareTo(rhs.percent_change_24h!!.toFloat())
            }
        // Change 7d LH
            13 -> Collections.sort<CryptoCoin>(currencyList) { lhs, rhs ->
                if (lhs.percent_change_7d == null && rhs.percent_change_7d == null) {
                    return@sort 0
                }
                if (lhs.percent_change_7d == null) {
                    return@sort 1
                }
                if (rhs.percent_change_7d == null) {
                    return@sort -1
                }
                return@sort lhs.percent_change_7d!!.toFloat().compareTo(rhs.percent_change_7d!!.toFloat())

            }
        }
    }

    private fun floatComp(f: Float): Int {
        if (f == 0f) {
            return 0
        } else if (f < 0) {
            return -1
        } else {
            return 1
        }
    }

    private fun floatCompLH(f: Float): Int {
        if (f == 0f) {
            return 0
        } else if (f < 0) {
            return 1
        } else {
            return -1
        }
    }
}
