package com.example.mediaplayer.view.dest

import android.Manifest
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mediaplayer.R
import com.example.mediaplayer.base.BaseFragment
import com.example.mediaplayer.databinding.FragmentMediaListBinding
import com.example.mediaplayer.view.adapter.PlayListItemAdapter
import com.example.mediaplayer.viewmodel.PlayListViewModel
import com.example.mediaplayer.viewmodel.PlayListViewModelFactory
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class PlayListFragment : BaseFragment<FragmentMediaListBinding>(
  R.layout.fragment_media_list
) {
  private val viewModel by viewModels<PlayListViewModel> { PlayListViewModelFactory(requireContext()) }
  private val playListAdapter = PlayListItemAdapter()

  override fun FragmentMediaListBinding.bindingViewData() {
    rvPlayList.adapter = playListAdapter

    viewModel.playlist.observe(this@PlayListFragment, Observer {
      playListAdapter.playList = it
    })

    val root = arguments?.getString("root") ?: ""

    if (root == "ringtone") viewModel.getPlayList(root)
    else fetchPlayList(root)
  }

  override fun FragmentMediaListBinding.setEventListener() {
  }

  @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
  fun fetchPlayList(root: String) {
    viewModel.getPlayList(root)
  }

  @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
  fun onMediaNeverAskAgain() {
    Toast.makeText(context, "권한허용해라ㅅㅂ", Toast.LENGTH_SHORT).show()
  }

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<out String>, grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    onRequestPermissionsResult(requestCode, grantResults)
  }
}