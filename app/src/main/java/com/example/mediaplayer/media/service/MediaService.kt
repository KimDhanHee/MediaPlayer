package com.example.mediaplayer.media.service

import androidx.media2.player.MediaPlayer
import androidx.media2.session.*
import java.util.concurrent.Executors

class MediaService : MediaLibraryService() {
  private var mediaSession: MediaLibrarySession? = null
  private val mediaPlayer: MediaPlayer by lazy { MediaPlayer(this) }

  override fun onCreate() {
    super.onCreate()

    mediaSession = MediaLibrarySession.Builder(
      this,
      mediaPlayer,
      Executors.newCachedThreadPool(),
      object : MediaLibrarySession.MediaLibrarySessionCallback() {
        override fun onConnect(
          session: MediaSession,
          controller: MediaSession.ControllerInfo
        ): SessionCommandGroup? {
          return SessionCommandGroup.Builder()
            // Library Related Command
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_GET_CHILDREN))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_GET_ITEM))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_GET_LIBRARY_ROOT))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_GET_SEARCH_RESULT))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_SEARCH))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_SUBSCRIBE))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_LIBRARY_UNSUBSCRIBE))
            // Player Related Command
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_GET_CURRENT_MEDIA_ITEM))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_GET_PLAYLIST))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_GET_PLAYLIST_METADATA))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_PLAY))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_PAUSE))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_PREPARE))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_SEEK_TO))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_SET_MEDIA_ITEM))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_SET_REPEAT_MODE))
            .addCommand(SessionCommand(SessionCommand.COMMAND_CODE_PLAYER_SET_SHUFFLE_MODE))
            .build()
        }

        override fun onGetChildren(
          session: MediaLibrarySession,
          controller: MediaSession.ControllerInfo,
          parentId: String,
          page: Int,
          pageSize: Int,
          params: LibraryParams?
        ): LibraryResult = LibraryResult(
          LibraryResult.RESULT_SUCCESS, MediaLibrary.playList(applicationContext, parentId), null
        )
      }).build()
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? =
    mediaSession

  override fun onDestroy() {
    mediaSession?.close()
    super.onDestroy()
  }
}