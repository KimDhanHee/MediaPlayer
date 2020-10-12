package com.example.mediaplayer.viewmodel

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media2.common.MediaItem
import androidx.media2.session.MediaBrowser
import androidx.media2.session.MediaController
import androidx.media2.session.SessionToken
import com.example.mediaplayer.media.service.MediaService
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class PlayListViewModel(context: Context) : ViewModel() {
  val playlist: LiveData<List<MediaItem>>
    get() = _playlist
  private val _playlist = MutableLiveData<List<MediaItem>>()

  private val mediaBrowser: MediaBrowser = MediaBrowser.Builder(context)
    .setSessionToken(SessionToken(context, ComponentName(context, MediaService::class.java)))
    .setControllerCallback(
      Executors.newCachedThreadPool(),
      object : MediaBrowser.BrowserCallback() {
        override fun onDisconnected(controller: MediaController) {
          controller.close()
        }
      }
    ).build()

  private val connectDeffered by lazy {
    viewModelScope.async {
      while (!mediaBrowser.isConnected) {
        delay(300L)
      }
    }
  }

  fun getPlayList(root: String) {
    viewModelScope.launch {
      connectDeffered.await()

      mediaBrowser.getChildren(root, 0, 100, null).apply {
        addListener(
          Runnable {
            _playlist.postValue(get().mediaItems)
          },
          Executors.newCachedThreadPool()
        )
      }
    }
  }
}