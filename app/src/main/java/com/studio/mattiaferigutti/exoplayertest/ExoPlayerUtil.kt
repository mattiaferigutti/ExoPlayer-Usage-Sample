package com.studio.mattiaferigutti.exoplayertest

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.View
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.AssetDataSource
import com.google.android.exoplayer2.upstream.DataSource.Factory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

/**
 * @author Mattia Ferigutti
 */

class ExoPlayerUtil private constructor() {
    enum class LoadingMode {
        INTERNET,
        ASSETS
    }

    var playWhenReady = false
    var currentWindow = 0
    var playbackPosition: Long = 0

    fun initialize(path: String? = null) {
        var videoSource: ProgressiveMediaSource? = null

        if (mode == LoadingMode.ASSETS) {
            val dataSourceFactory = Factory { AssetDataSource(context) }

            videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(path))
        } else if (mode == LoadingMode.INTERNET) {
            val dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer-pillo")

            videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(path))
        }

        videoPlayer?.player = player
        videoPlayer?.requestFocus()
        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        if (events != null) player?.addListener(events!!)
        if (path != null) player?.prepare(videoSource!!)
    }

    fun release() {
        if (player != null) {
            playbackPosition = player?.currentPosition!!
            currentWindow = player?.currentWindowIndex!!
            playWhenReady = player?.playWhenReady!!
            if (events != null) player?.removeListener(events!!)
            player?.release()
            player = null
        }
    }

    fun pause() {
        player?.playWhenReady = false
        player?.playbackState
    }

    fun play() {
        player?.playWhenReady = true
        player?.playbackState
    }

    fun restart() {
        player?.seekTo(0)
        player?.playWhenReady = true
    }

    fun stop() {
        pause()
        release()
    }

    fun endVideo() {
        player?.seekTo(player?.duration ?: 0)
    }

    fun getPlayer() : SimpleExoPlayer? {
        return player
    }

    companion object {
        private var player: SimpleExoPlayer? = null
        private var context: Context? = null
        private var exoPlayerUtil: ExoPlayerUtil? = null
        private var events: Player.EventListener? = null
        private var videoPlayer: PlayerView? = null
        private var mode: LoadingMode? = null

        fun getInstance(context: Context, videoPlayer: PlayerView?, mode: LoadingMode, events: Player.EventListener? = null) : ExoPlayerUtil {
            Companion.videoPlayer = videoPlayer
            Companion.events = events
            Companion.context = context
            Companion.mode = mode
            if (player == null) {
                val trackSelector = DefaultTrackSelector(context)
                trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd())
                player = SimpleExoPlayer.Builder(context)
                    .setTrackSelector(trackSelector)
                    .build()
            }
            if (exoPlayerUtil == null) {
                exoPlayerUtil = ExoPlayerUtil()
            }
            return exoPlayerUtil!!
        }

        /**
         * set fullscreen
         */
        @SuppressLint("InlinedApi")
        fun hideSystemUi() {
            videoPlayer?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }
}