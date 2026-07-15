package sg.edu.nus.iss.inclass.mini_4b

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Track the toggle state
    private var isMusicPlaying = false

    // Define the Broadcast Receiver assistant
    private val musicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Check if the intent action matches our channel
            if (intent?.action == "MUSIC_TRACK_CHANGED") {
                // Extract the song name from the envelope's extras
                val songName = intent.getStringExtra("EXTRA_SONG_NAME")


                val tvSongName = findViewById<TextView>(R.id.tvSongName)
                tvSongName.text = "Now Playing: $songName"
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

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)

            if (!isMusicPlaying) {
                // 1. If not playing, change text/UI and send PLAY command
                button.text = "Stop Music"
                intent.action = "ACTION_PLAY"
                isMusicPlaying = true
            } else {
                // 2. If already playing, reset text/UI and send STOP command
                button.text = "Play Random MP3"
                intent.action = "ACTION_STOP"
                isMusicPlaying = false
            }

            startService(intent)
        }

    }

    // Tune the radio when the Activity starts showing on screen
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        // Create a filter (tuner) to only let "MUSIC_TRACK_CHANGED" messages through
        val filter = IntentFilter("MUSIC_TRACK_CHANGED")

        // Register our receiver with the OS courier
        registerReceiver(musicReceiver, filter, RECEIVER_NOT_EXPORTED)
    }

    // Turn off the radio when the Activity is no longer visible
    override fun onStop() {
        super.onStop()
        // Unregister to prevent memory leaks and save battery!
        unregisterReceiver(musicReceiver)
    }
}