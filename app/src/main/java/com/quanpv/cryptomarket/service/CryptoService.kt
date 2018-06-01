package com.quanpv.cryptomarket.service

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
        private var retrofit: Retrofit? = null
        private var builder: Retrofit.Builder? = null /*= Retrofit.Builder().baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/
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