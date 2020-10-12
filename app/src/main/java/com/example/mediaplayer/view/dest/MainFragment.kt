package com.example.mediaplayer.view.dest

import com.example.mediaplayer.R
import com.example.mediaplayer.base.BaseFragment
import com.example.mediaplayer.databinding.FragmentMainBinding
import com.example.mediaplayer.view.adapter.PlayListPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : BaseFragment<FragmentMainBinding>(
  R.layout.fragment_main
) {
  override fun FragmentMainBinding.bindingViewData() {
    viewPager.adapter = PlayListPageAdapter(this@MainFragment)

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
      tab.text = when (position) {
        0 -> getString(R.string.playlist_root_ringtone)
        1 -> getString(R.string.playlist_root_app)
        2 -> getString(R.string.playlist_root_user)
        else -> ""
      }
    }.attach()
  }

  override fun FragmentMainBinding.setEventListener() {
  }
}