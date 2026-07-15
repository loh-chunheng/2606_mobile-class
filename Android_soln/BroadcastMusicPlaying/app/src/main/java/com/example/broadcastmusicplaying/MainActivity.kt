package com.example.broadcastmusicplaying

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val recv = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "on_rand_play") {
                val music_name = intent.getStringExtra("music_name")
                findViewById<TextView>(R.id.music_name)?.text = music_name
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // set event listener for button
        val btn = findViewById<Button>(R.id.play_rand_btn)
        btn.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            intent.action = "rand_play"
            startService(intent)
        }

        // register broadcast receiver
        registerReceiver(recv, IntentFilter("on_rand_play"), RECEIVER_EXPORTED)
    }
}