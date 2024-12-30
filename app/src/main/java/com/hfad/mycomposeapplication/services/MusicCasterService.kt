package com.hfad.mycomposeapplication.services

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MusicCasterService : MediaSessionService() {

    private lateinit var exoPlayer: ExoPlayer
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, exoPlayer).build()

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val trackUriString = intent?.getStringExtra("TRACK_URI")
        if (trackUriString != null) {
            val trackUri = Uri.parse(trackUriString)
            playTrack(trackUri)
        }

        return START_STICKY
    }

    private fun playTrack(trackUri: Uri) {
        val mediaItem = MediaItem.fromUri(trackUri)

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun onDestroy() {
        exoPlayer.stop()
        exoPlayer.release()

        mediaSession?.run {
            player.release()
            release()

        }
        mediaSession = null
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

}
