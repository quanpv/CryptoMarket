package com.quanpv.cryptomarket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.quanpv.cryptomarket.fragment.HomeFragment
import com.quanpv.cryptomarket.model.CryptoCoin
import com.quanpv.cryptomarket.service.CryptoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openPage(HomeFragment.newInstance(), false)
//        requestAllCoin()
        val param: Map<String, String> = hashMapOf("limit" to "0")
        CryptoService.requestAllCoin(param) {
            onNext {
                handleSuccessAllCoin(it)
            }
            onError {
                Log.e(TAG, "handlerErrorAndroidVersion: ${it.localizedMessage}")
            }
        }
    }


    fun openPage(fragment: Fragment, isBackStack: Boolean) {
        val tx = supportFragmentManager.beginTransaction()
        tx.replace(R.id.container, fragment, fragment.javaClass.simpleName)
        tx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (isBackStack) tx.addToBackStack(null)
        tx.commit()

    }

    // request data tu server
    private fun requestAllCoin() {
        val param: Map<String, String> = hashMapOf("limit" to "0")

        CryptoService.createService("https://api.coinmarketcap.com/").getAllCoins(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        //cú pháp của rxjava trong kotlin
                        { result ->
                            //request thành công
                            handleSuccessAllCoin(result)
                        },
                        { error ->
                            //request thất bai
                            Log.e(TAG, "handlerErrorAndroidVersion: ${error.localizedMessage}")
                        }
                )
    }

    //Xử lí dữ liệu khi request thành công
    private fun handleSuccessAllCoin(result: List<CryptoCoin>) {
        Log.i(TAG, "handleSuccessAllCoin: ${result.get(0).name}")
    }

}
