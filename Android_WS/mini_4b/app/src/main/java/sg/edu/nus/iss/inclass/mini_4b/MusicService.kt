package sg.edu.nus.iss.inclass.mini_4b

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    // Define a list of all your audio files in the raw folder
    private val songs = arrayOf(
        R.raw.acoustic_breeze,
        R.raw.buddy,
        R.raw.creative_minds,
        R.raw.inspire,
        R.raw.memories,
        R.raw.once_again,
        R.raw.sunny,
        R.raw.tenderness,
        R.raw.ukulele
    )

    // This is a Started Service, so we return null for binding
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        when (action) {
            "ACTION_PLAY" -> startMusic()
            "ACTION_STOP" -> stopMusic()
        }

        // START_STICKY ensures the system recreates the service if it gets killed due to low memory
        return START_STICKY
    }

    private fun startMusic() {
        // Only create a new player if it doesn't already exist or isn't playing
        if (mediaPlayer == null) {
            // Select a completely random resource ID from the array
            val randomSongResId = songs.random()

            // Create the player with the chosen random song
            mediaPlayer = MediaPlayer.create(this, randomSongResId).apply {
                isLooping = false // Usually better to false for random lists so it can switch next

                setOnCompletionListener {
                    // 1. Clean up the current finished player resources
                    it.release()
                    mediaPlayer = null

                    // 2. Call startMusic() again to pick a fresh random song!
                    startMusic()
                }

                start()
            }
        } else if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }

    private fun stopMusic() {
        // Safe check: stop and release resource using your code reference
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }

        // Tell the Android OS that the service can now be dismissed completely
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        // CRITICAL: Clean up the player assistant if the OS unexpectedly kills the service!
        mediaPlayer?.let {
            it.release()
            mediaPlayer = null
        }
    }
}