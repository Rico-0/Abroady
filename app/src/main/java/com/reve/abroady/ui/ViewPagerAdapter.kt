package com.reve.abroady.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter (
    private val fragments: Array<Fragment>, // 화면 Fragment 배열
    fa: FragmentManager, // FragmentStateAdapter 상속 시 필요
    lifecycle: Lifecycle
) : FragmentStateAdapter(fa, lifecycle){

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        // TODO : if (position == PAGE_HOME) ...
        /* if (position == PAGE_MY) {
             return fragments[position].apply {
                 arguments = bundleOf(
                     "argument" to MyPageFragment.Argument(
                         profileName = userName,
                         profileImage = userProfileImage
                     )
                 )
             }
         } */
        return fragments[position]
    }
}