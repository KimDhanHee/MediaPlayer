package com.example.mediaplayer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.media2.common.MediaItem
import androidx.media2.common.MediaMetadata
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.databinding.ViewholderMediaListItemBinding

class PlayListItemAdapter : RecyclerView.Adapter<PlayListItemAdapter.ItemViewHolder>() {
  var playList: List<MediaItem> = listOf()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
    ItemViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(R.layout.viewholder_media_list_item, parent, false)
    )

  override fun getItemCount(): Int = playList.size

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.binding?.let { view ->
      playList[position].metadata?.let { meta ->
        view.album = meta.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI)
        view.title = meta.getString(MediaMetadata.METADATA_KEY_TITLE)
        view.artist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST)
      }
    }
  }

  class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = DataBindingUtil.bind<ViewholderMediaListItemBinding>(view)
  }
}