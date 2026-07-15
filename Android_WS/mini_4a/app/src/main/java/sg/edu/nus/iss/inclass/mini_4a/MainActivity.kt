package sg.edu.nus.iss.inclass.mini_4a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Track the toggle state
    private var isMusicPlaying = false

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
}