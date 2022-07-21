package com.reve.abroady.ui

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.reve.abroady.R
import com.reve.abroady.base.BaseActivity
import com.reve.abroady.databinding.ActivityMainBinding
import com.reve.abroady.databinding.CustomTabLayoutBinding
import com.reve.abroady.ui.chat.ChatFragment
import com.reve.abroady.ui.community.CommunityFragment
import com.reve.abroady.ui.login.classfile.LoginBase
import com.reve.abroady.ui.map.MapFragment
import com.reve.abroady.ui.match.MatchFragment
import com.reve.abroady.ui.my.MyFragment
import com.reve.abroady.util.LoginInstance
import com.reve.abroady.util.PreferenceManager.login_type

class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        val TAB_LAYOUT_ICON = arrayOf(R.drawable.icon_search, R.drawable.icon_notification, R.drawable.icon_notification,
        R.drawable.icon_search, R.drawable.icon_search)
        val TAB_LAYOUT_TEXT = arrayOf("Home", "Map", "Match", "Chat", "My")
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    private val tabLayoutResourceId: Int
        get() = R.layout.custom_tab_layout

    lateinit var tabLayoutbinding: CustomTabLayoutBinding

    private lateinit var loginInstance: LoginBase

    override fun initStartView() {
        setLoginType(intent.getStringExtra("loginType"))
        tabLayoutbinding = DataBindingUtil.inflate(layoutInflater, tabLayoutResourceId, null, false)

        // 임시 로그인 확인 (kakao)
        // 구글 로그인으로 사용자 정보 얻어오기 처리 필요
        loginInstance = LoginInstance.getLoginInstance(this, this@MainActivity, login_type ?: "")
        if (login_type != "google")
            loginInstance.getUserInfo()

        val fragments = arrayOf<Fragment>(
            CommunityFragment(),
            MapFragment(),
            MatchFragment(),
            ChatFragment(),
            MyFragment()
        )

        val viewPagerAdapter = ViewPagerAdapter(
            fragments,
            supportFragmentManager,
            lifecycle
        )
        binding.mainViewPager.offscreenPageLimit = fragments.size
        binding.mainViewPager.adapter = viewPagerAdapter
        binding.mainViewPager.isUserInputEnabled = false

        initTabLayout()
    }

    private fun setLoginType(type : String?) {
        type?.let {  login_type = type }
    }

    /*  아이콘 추후 수정
    private fun createTabLayoutView(tabName: String): View {
       tabLayoutbinding.tabText.text = tabName
        when (tabName) {
            "Home" -> {
                tabLayoutbinding.tabIcon.setImageResource(R.drawable.search_icon)
                return tabLayoutbinding.root
            }
            "Map" -> {
                tabLayoutbinding.tabIcon.setImageResource(R.drawable.notification_icon)
                return tabLayoutbinding.root
            }
            "Match" -> {
                tabLayoutbinding.tabIcon.setImageResource(R.drawable.write_post_icon)
                return tabLayoutbinding.root
            }
            "Chat" -> {
                tabLayoutbinding.tabIcon.setImageResource(R.drawable.community_post_like_icon)
                return tabLayoutbinding.root
            }
            "My" -> {
                tabLayoutbinding.tabIcon.setImageResource(R.drawable.party_person_icon)
                return tabLayoutbinding.root
            }
            else -> {
                return tabLayoutbinding.root
            }
        }
    }  */

    private fun initTabLayout() {
        TabLayoutMediator(binding.mainTablayout, binding.mainViewPager) { tab, position ->
            tab.setIcon(TAB_LAYOUT_ICON.get(position))
           // var tabText = TAB_LAYOUT_TEXT.get(position)
           // tab.customView = createTabLayoutView(tabText)
        }.attach()
    }
}