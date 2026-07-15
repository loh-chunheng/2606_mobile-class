package com.example.broadcastmusicplaying

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {

    private val mp3s = arrayListOf(
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

    private var player : MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "rand_play") {
            val pos = (0 until mp3s.size).random()

            stop()
            play(pos)
            broadcast(pos)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun play(pos: Int) {
        player = MediaPlayer.create(this, mp3s[pos])
        player?.start()
    }

    private fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    private fun broadcast(pos: Int) {
        val intent = Intent("on_rand_play")
        intent.putExtra("music_name", resources.getResourceEntryName(mp3s[pos]))
        sendBroadcast(intent)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}