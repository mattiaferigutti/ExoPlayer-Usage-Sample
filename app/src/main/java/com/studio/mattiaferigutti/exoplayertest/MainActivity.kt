package com.studio.mattiaferigutti.exoplayertest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.util.Util
import com.studio.mattiaferigutti.exoplayertest.ExoPlayerUtil.Companion.hideSystemUi
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Mattia Ferigutti
 */

class MainActivity : AppCompatActivity(), Player.EventListener {

    private val mp4Url = "https://pillouserdata.blob.core.windows.net/videos/pria-support/01_Welcome_To_Pria_1024x600.mp4"
    private val resValue = "assets:///nature.mp4"
    private var playerUtil: ExoPlayerUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //use PilloExoPlayer.LoadingMode.INTERNET to take a video from the internet
        playerUtil = ExoPlayerUtil.getInstance(this, video_view, ExoPlayerUtil.LoadingMode.ASSETS, this)
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            playerUtil?.initialize(resValue)
            playerUtil?.play()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || playerUtil == null) {
            playerUtil?.initialize(resValue)
            playerUtil?.play()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            playerUtil?.release()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            playerUtil?.release()
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)

        //handle the error
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        val stateString: String = when (playbackState) {
            ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
            ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
            ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
            ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
            else -> "UNKNOWN_STATE             -"
        }
        Log.d(TAG, "changed state to " + stateString
                + " playWhenReady: " + playWhenReady)
    }

    companion object {
        private val TAG = MainActivity::class.java.name
    }
}
