package com.example.mediaplayer.view.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mediaplayer.view.dest.PlayListFragment

class PlayListPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
  companion object {
    private const val PLAYLIST_COUNT = 3
  }

  override fun getItemCount(): Int = PLAYLIST_COUNT

  override fun createFragment(position: Int): Fragment = PlayListFragment().apply {
    arguments = bundleOf(
      "root" to when (position) {
        0 -> "ringtone"
        1 -> "user"
        2 -> "app"
        else -> ""
      }
    )
  }
}