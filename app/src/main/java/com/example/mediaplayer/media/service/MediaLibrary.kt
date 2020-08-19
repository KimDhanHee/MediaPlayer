package com.example.mediaplayer.media.service

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import androidx.media2.common.MediaMetadata
import androidx.media2.common.UriMediaItem

object MediaLibrary {
  fun playList(context: Context): List<UriMediaItem> = mutableListOf<UriMediaItem>().apply {
    val ringtoneManager = RingtoneManager(context).apply { setType(RingtoneManager.TYPE_ALL) }
    val cursor = ringtoneManager.cursor

    cursor.use {
      val first = cursor.moveToFirst()

      while (first && cursor.moveToNext()) {
        val uri = ringtoneManager.getRingtoneUri(cursor.position)
        val retreiver = MediaMetadataRetriever().apply { setDataSource(context, uri) }
        val title =
          retreiver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "unknown"
        val duration =
          retreiver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()

        add(
          UriMediaItem.Builder(uri)
            .setMetadata(
              MediaMetadata.Builder()
                .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, uri.toString())
                .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, uri.toString())
                .putString(MediaMetadata.METADATA_KEY_TITLE, title)
                .putLong(MediaMetadata.METADATA_KEY_DURATION, duration)
                .putLong(MediaMetadata.METADATA_KEY_BROWSABLE, MediaMetadata.BROWSABLE_TYPE_NONE)
                .putLong(MediaMetadata.METADATA_KEY_PLAYABLE, 1)
                .build()
            )
            .build()
        )
      }
    }
  }
}