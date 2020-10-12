package com.example.mediaplayer.media.service

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import androidx.media2.common.MediaMetadata
import androidx.media2.common.UriMediaItem
import com.example.mediaplayer.R

object MediaLibrary {
  fun playList(context: Context, parentId: String): List<UriMediaItem> = when (parentId) {
    "ringtone" -> ringtonePlayList(context)
    "user" -> userPlayList(context)
    "app" -> appPlayList(context)
    else -> listOf()
  }

  private fun ringtonePlayList(context: Context) = mutableListOf<UriMediaItem>().apply {
    val ringtoneManager = RingtoneManager(context).apply { setType(RingtoneManager.TYPE_ALL) }
    val cursor = ringtoneManager.cursor

    cursor.use {
      val first = cursor.moveToFirst()

      while (first && cursor.moveToNext()) {
        val uri = ringtoneManager.getRingtoneUri(cursor.position)
        val retriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
        val title =
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "unknown"
        val duration =
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()

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

  private fun userPlayList(context: Context) = mutableListOf<UriMediaItem>().apply {
    val projection = arrayOf(
      MediaStore.Audio.Media._ID,
      MediaStore.Audio.Media.TITLE,
      MediaStore.Audio.Media.DISPLAY_NAME,
      MediaStore.Audio.Media.DURATION
    )

    context.contentResolver.query(
      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
      projection,
      null,
      null,
      "${MediaStore.Audio.Media.TITLE} ASC"
    )?.use { cursor ->
      val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
      val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
      val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
      val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

      while (cursor.moveToNext()) {
        val id = cursor.getLong(idColumn)
        val title = cursor.getString(titleColumn)
        val duration = cursor.getString(durationColumn).toLong()
        val uri = Uri.withAppendedPath(
          MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
          id.toString()
        )

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

  private fun appPlayList(context: Context) = mutableListOf<UriMediaItem>().apply {
    context.resources.getStringArray(R.array.playlist_app_title).forEach { title ->
      val uri = Uri.parse(
        context.resources.getString(R.string.app_media_uri_format)
          .format(context.packageName, title)
      )
      val retriever = MediaMetadataRetriever().apply { setDataSource(context, uri) }
      add(
        UriMediaItem.Builder(uri)
          .setMetadata(
            MediaMetadata.Builder()
              .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, uri.toString())
              .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, uri.toString())
              .putString(MediaMetadata.METADATA_KEY_TITLE, title.replace("_", " "))
              .putLong(
                MediaMetadata.METADATA_KEY_DURATION,
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
              )
              .putLong(MediaMetadata.METADATA_KEY_BROWSABLE, MediaMetadata.BROWSABLE_TYPE_NONE)
              .putLong(MediaMetadata.METADATA_KEY_PLAYABLE, 1)
              .build()
          )
          .build()
      )
      retriever.release()
    }
  }
}