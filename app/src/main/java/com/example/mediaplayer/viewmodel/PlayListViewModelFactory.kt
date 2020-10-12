package com.example.mediaplayer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlayListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
    modelClass.isAssignableFrom(PlayListViewModel::class.java) -> PlayListViewModel(context) as T
    else -> throw IllegalArgumentException("")
  }
}