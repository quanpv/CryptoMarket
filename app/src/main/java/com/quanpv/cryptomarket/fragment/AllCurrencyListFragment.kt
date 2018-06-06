package com.quanpv.cryptomarket.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.quanpv.cryptomarket.MainActivity
import com.quanpv.cryptomarket.R
import com.quanpv.cryptomarket.adapter.AllCurrencyListAdapter
import com.quanpv.cryptomarket.adapter.AllCurrencyListAdapter.OnItemClickListener
import com.quanpv.cryptomarket.database.DatabaseHelperSingleton
import com.quanpv.cryptomarket.fragment.HomeFragment.Companion.SORT_SETTING
import com.quanpv.cryptomarket.model.CryptoCoin
import com.quanpv.cryptomarket.service.CryptoService
import com.quanpv.cryptomarket.utils.SortUtil
import com.quanpv.cryptomarket.utils.SortUtil.sortList
import kotlinx.android.synthetic.main.fragment_all_currency_list.*

/**
 * Created by QuanPham on 5/31/18.
 */
class AllCurrencyListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener {


    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var currencyRecyclerView: RecyclerView? = null
    private var adapter: AllCurrencyListAdapter? = null
    private var currencyItemList: ArrayList<CryptoCoin>? = null
    private val filteredList = ArrayList<CryptoCoin>()
    private var searchItem: MenuItem? = null
    private var searchView: SearchView? = null
    private var rootView: View? = null
    var searchList: ArrayList<CryptoCoin>? = null
    private val searchedSymbols = HashMap<String, String>()
    private var slugToIDMap = HashMap<String, Int>()
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        val TAG = MainActivity::class.java.simpleName
        var currQuery = ""
        var searchViewFocused = false
        val SHAREDPREF_SETTINGS = "cryptobuddy_settings"

        fun newInstance(): AllCurrencyListFragment {
            return AllCurrencyListFragment()
        }

    }

    override fun getViewId(): Int {
        return R.layout.fragment_all_currency_list
    }

    override fun init(view: View) {
        this.rootView = view
        setHasOptionsMenu(true)
        val db = DatabaseHelperSingleton.getInstance(mContext!!)
        sharedPreferences = context!!.getSharedPreferences(SHAREDPREF_SETTINGS, MODE_PRIVATE)
        searchList = ArrayList()
        recycler_view.layoutManager = LinearLayoutManager(mContext)
        currencyItemList = ArrayList()
        adapter = AllCurrencyListAdapter(currencyItemList!!, mContext!!, object : OnItemClickListener {
            override fun onItemClick(position: Int, view: View) {

            }
        })
        adapter?.setOnItemClickListener { position ->
            Log.i("position", "----->" + position)
        }
        recycler_view.adapter = adapter

        swipe_refresh.setColorSchemeResources(R.color.colorAccent)
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.post({
            swipe_refresh.isRefreshing = true
            getCurrencyList()
        })
    }

    fun performAllCoinsSort() {
        val sortType = sharedPreferences?.getInt(HomeFragment.SORT_SETTING, 1)
        SortUtil.sortList(adapter?.currencyList!!, sortType!!)
        adapter?.notifyDataSetChanged()
    }

    override fun onRefresh() {
        getCurrencyList()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        currQuery = query!!
        var query = query.toLowerCase()
        filteredList.clear()
        for (coin in currencyItemList!!) {
            if (coin.symbol?.toLowerCase()!!.contains(query) || coin.symbol?.toLowerCase()!!.contains(query)) {
                filteredList.add(coin)
            }
        }
        adapter?.currencyList = filteredList
        adapter?.notifyDataSetChanged()
        return true
    }


    fun getCurrencyList() {
        swipe_refresh.isRefreshing = true

        val param: Map<String, String> = hashMapOf("limit" to "0")
        CryptoService.requestAllCoin(param) {
            onNext {

                try {
                    if (searchViewFocused) { // Copy some code here to make the checks faster
                        searchedSymbols.clear()
                        searchList?.clear()
                        for (coin in filteredList) {
                            searchedSymbols.put(coin.symbol!!, coin.symbol!!)
                        }
                        it
                                .filter { searchedSymbols[it.symbol] != null }
                                .forEach { searchList?.add(it) }
                    } else {
                        currencyItemList?.clear()
                        currencyItemList?.addAll(it)
                        adapter?.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                getQuickSearch()

            }
            onError {
                swipeRefreshLayout?.isRefreshing = false
                Log.e(TAG, "handlerErrorAndroidVersion: ${it.localizedMessage}")
            }
        }

    }


    fun getQuickSearch() {
        CryptoService.getQuickSearch {
            onNext {

                slugToIDMap = java.util.HashMap()
                val recyclerViewState: Parcelable = recycler_view.layoutManager.onSaveInstanceState()
                for (node in it) {
                    slugToIDMap.put(node.slug!!, node.id)
                }
                if (searchViewFocused) {
                    for (coin in searchList!!) {
                        if (slugToIDMap[coin.id] != null) {
                            coin.quickSearchID = slugToIDMap[coin.id]
                        }
                    }
                    adapter?.currencyList = searchList
                } else {
                    for (coin in currencyItemList!!) {
                        if (coin.id != null && slugToIDMap[coin.id] != null) {
                            coin.quickSearchID = slugToIDMap[coin.id]
                        }
                    }
                    adapter?.currencyList = currencyItemList
                }
                val sortType = sharedPreferences?.getInt(SORT_SETTING, 1)
                sortList(adapter?.currencyList!!, sortType!!)
                adapter?.notifyDataSetChanged()
//                favsUpdateCallback.performFavsSort()
                recycler_view.layoutManager.onRestoreInstanceState(recyclerViewState)
                swipe_refresh.isRefreshing = false

            }

            onError {
                swipe_refresh.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity!!.menuInflater.inflate(R.menu.all_currency_list_tab_menu, menu)
        searchItem = menu!!.findItem(R.id.action_search)
        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView?.setOnQueryTextListener(this)
        // Detect SearchView icon clicks
        searchView?.setOnSearchClickListener(View.OnClickListener {
            searchViewFocused = true
            setItemsVisibility(menu, searchItem!!, false)
//            drawerController.hideHamburger()
        })
        // Detect SearchView close
        searchView?.setOnCloseListener(SearchView.OnCloseListener {
            searchViewFocused = false
            setItemsVisibility(menu, searchItem!!, true)
//            drawerController.showHamburger()
            false
        })
        if (searchViewFocused) (mContext as AppCompatActivity).supportActionBar!!.title = ""
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setItemsVisibility(menu: Menu?, exception: MenuItem, visible: Boolean) {
        for (i in 0 until menu!!.size()) {
            val item = menu.getItem(i)
            if (item !== exception) item.isVisible = visible
        }
        if (!visible) {
            (mContext as AppCompatActivity).supportActionBar!!.title = ""
        } else {
            (mContext as AppCompatActivity).supportActionBar!!.title = resources.getString(R.string.app_name)
        }
    }


    private fun showInputMethod(view: View) {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        if (searchView != null && searchViewFocused) {
            (mContext as AppCompatActivity).supportActionBar!!.title = ""
            searchView?.requestFocusFromTouch()
            searchView?.isIconified = false
            searchView?.setQuery(currQuery, false)
            showInputMethod(rootView!!)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchViewFocused = false
    }

    override fun onResume() {
        super.onResume()
        activity!!.invalidateOptionsMenu()
    }

}