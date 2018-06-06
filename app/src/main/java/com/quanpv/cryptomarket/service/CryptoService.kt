package com.quanpv.cryptomarket.service

import com.quanpv.cryptomarket.model.CMCQuickSearch
import com.quanpv.cryptomarket.model.CryptoCoin
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by QuanPham on 5/30/18.
 */
class CryptoService {

    companion object {

        val COIN_MARKETCAP_ALL_COINS_URL = "https://api.coinmarketcap.com/"
        val COIN_MARKETCAP_CHART_URL_ALL_DATA = "https://graphs2.coinmarketcap.com/"
        val COIN_MARKETCAP_CHART_URL_WINDOW = "https://graphs2.coinmarketcap.com/"
        val COIN_MARKETCAP_QUICK_SEARCH_URL = "https://s2.coinmarketcap.com/"

        private var retrofit: Retrofit? = null
        private var builder: Retrofit.Builder? = null
        private val httpClient = OkHttpClient.Builder()
        fun createService(baseUrl: String?): ApiService {
            builder = Retrofit.Builder().baseUrl(baseUrl!!)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            retrofit = builder?.build()
            return retrofit!!.create(ApiService::class.java)
        }

//        fun createService(authToken: Map<String, String>?): ApiService {
//            if (authToken != null) {
//                var interceptor = AuthenticationInterceptor(authToken!!)
//                if (!httpClient.interceptors().contains(interceptor)) {
//                    httpClient.addInterceptor(interceptor)
//                    builder?.client(httpClient.build())
//                    retrofit = builder?.build()
//                }
//            }
//            retrofit = builder?.build()
//            return retrofit!!.create(ApiService::class.java)
//        }


        fun requestAllCoin(param: Map<String, String>, observer: KObserver<List<CryptoCoin>>.() -> Unit) {
            CryptoService.createService(COIN_MARKETCAP_ALL_COINS_URL).getAllCoins(param)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(KObserver<List<CryptoCoin>>().apply(observer))
        }

        fun getQuickSearch(observer: KObserver<List<CMCQuickSearch>>.() -> Unit) {
            CryptoService.createService(COIN_MARKETCAP_ALL_COINS_URL).getCMCQuickSearch()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(KObserver<List<CMCQuickSearch>>().apply(observer))
        }

    }

    class KObserver<T> : Observer<T> {

        private var onSubscribe: ((Disposable) -> Unit)? = null
        private var onNext: ((T) -> Unit)? = null
        private var onError: ((Throwable) -> Unit)? = null
        private var onCompleted: (() -> Unit)? = null

        override fun onSubscribe(d: Disposable) {
            onSubscribe?.invoke(d)
        }

        override fun onNext(t: T) {
            onNext?.invoke(t)
        }

        override fun onError(e: Throwable) {
            onError?.invoke(e)
        }

        override fun onComplete() {
            onCompleted?.invoke()
        }

        fun onSubscribe(function: (Disposable) -> Unit) {
            this.onSubscribe = function
        }

        fun onNext(function: (T) -> Unit) {
            this.onNext = function
        }

        fun onError(function: (Throwable) -> Unit) {
            this.onError = function
        }

        fun onComplete(function: () -> Unit) {
            this.onCompleted = function
        }

    }

    //định nghĩa headers của request nếu có
    class AuthenticationInterceptor(private val authToken: Map<String, String>) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val builder = original.newBuilder()
            for (key in authToken.keys) {
                builder.header(key, authToken.getValue(key))
            }
            val request = builder.build()
            return chain.proceed(request)
        }
    }

}