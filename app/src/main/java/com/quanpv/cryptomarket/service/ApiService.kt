package com.quanpv.cryptomarket.service

import com.quanpv.cryptomarket.model.CMCQuickSearch
import com.quanpv.cryptomarket.model.CryptoCoin
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by QuanPham on 5/30/18.
 */
interface ApiService {

    @GET("v1/ticker/")
    fun getAllCoins(@QueryMap param: Map<String, String>): Observable<List<CryptoCoin>>

    @GET("generated/search/quick_search.json")
    fun getCMCQuickSearch(): Observable<List<CMCQuickSearch>>

}