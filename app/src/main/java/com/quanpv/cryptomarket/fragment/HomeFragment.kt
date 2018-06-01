package com.quanpv.cryptomarket.fragment

import android.graphics.Color
import android.support.v4.view.ViewPager
import android.text.Layout
import android.view.View
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.quanpv.cryptomarket.R
import com.quanpv.cryptomarket.adapter.HomePagerAdapter
import com.quanpv.cryptomarket.customview.TextDrawable
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by QuanPham on 5/31/18.
 */
class HomeFragment : BaseFragment() {

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }

    }

    private var drawer: Drawer? = null
    private var pagerAdapter: HomePagerAdapter? = null

    override fun getViewId(): Int {
        return R.layout.fragment_home
    }

    override fun init(view: View) {
        setUpDrawer()
        pagerAdapter = HomePagerAdapter(childFragmentManager)
//        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
        view_pager.adapter = pagerAdapter
        view_pager.offscreenPageLimit = 2
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }

        })
        tabs.setupWithViewPager(view_pager)
        tabs.setSelectedTabIndicatorColor(Color.WHITE)
    }


    private fun setUpDrawer() {
        val t = TextDrawable(context!!)
        t.text = "QPV"
        t.textAlign = Layout.Alignment.ALIGN_CENTER
        t.setTextColor(Color.BLACK)
        t.textSize = 10f
        val headerResult = AccountHeaderBuilder()
                .withActivity(activity!!)
                .withHeaderBackground(t).build()
        drawer = DrawerBuilder()
                .withActivity(activity!!)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(FontAwesome.Icon.faw_home),
                        PrimaryDrawerItem().withIdentifier(2).withName("News").withIcon(FontAwesome.Icon.faw_newspaper),
                        PrimaryDrawerItem().withIdentifier(3).withName("About").withIcon(FontAwesome.Icon.faw_question_circle),
                        PrimaryDrawerItem().withIdentifier(4).withName("Open Source").withIcon(FontAwesome.Icon.faw_github_square),
                        PrimaryDrawerItem().withIdentifier(5).withName("Rate on Google Play").withIcon(FontAwesome.Icon.faw_thumbs_up)
                )
                .withTranslucentStatusBar(false)
                .build()
        drawer?.onDrawerItemClickListener = Drawer.OnDrawerItemClickListener { view, position, drawerItem ->
            when (position) {
                1 -> {
                    drawer?.closeDrawer()
                    true
                }
                2 -> {
                    drawer?.closeDrawer()
//                    drawer?.setSelection(2)
//                    startActivity(Intent(context, NewsListActivity::class.java))
                    true
                }
                3 -> {
                    drawer?.closeDrawer()
//                    drawer?.setSelection(3)
//                    startActivity(Intent(context, AboutTheDevActivity::class.java))
                    true
                }
                4 -> {
                    drawer?.closeDrawer()
//                    drawer?.setSelection(4)
//                    libsBuilder.start(context)
                    true
                }
                else -> true
            }
        }
    }
}