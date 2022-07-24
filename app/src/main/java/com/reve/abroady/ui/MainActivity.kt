package com.reve.abroady.ui

import android.widget.Toast
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
import com.reve.abroady.util.ActivityList
import com.reve.abroady.util.LoginInstance
import com.reve.abroady.util.PreferenceManager.login_type

class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        val TAB_LAYOUT_ICON = arrayOf(R.drawable.icon_home_selected, R.drawable.icon_map_default, R.drawable.icon_matching_default,
        R.drawable.icon_chatting_default, R.drawable.icon_mypage_default)
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    private val tabLayoutResourceId: Int
        get() = R.layout.custom_tab_layout

    lateinit var tabLayoutbinding: CustomTabLayoutBinding

    private lateinit var loginInstance: LoginBase

    private var backPressedTime : Long = 0

    override fun initStartView() {
        setLoginType(intent.getStringExtra("loginType"))
        tabLayoutbinding = DataBindingUtil.inflate(layoutInflater, tabLayoutResourceId, null, false)
        ActivityList.actList.add(this)

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

    private fun initTabLayout() {
        TabLayoutMediator(binding.mainTablayout, binding.mainViewPager) { tab, position ->
            tab.setIcon(TAB_LAYOUT_ICON.get(position))
        }.attach()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            ActivityList.finishAllActivities()
        }
        Toast.makeText(this, "Press the back button one more if you want to terminate Abroady.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}