package sg.edu.nus.iss.mini_3a

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val REQ_RECORD_AUDIO = 1

    private lateinit var outputFile: String

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        outputFile = "${cacheDir.absolutePath}/audio.mp4"

        val startRecordButton = findViewById<Button>(R.id.btnStart)

        startRecordButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
                // If already granted, go straight to recording
                startRecordingAudio()
            } else {
                // If not granted, request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQ_RECORD_AUDIO
                )
            }
        }

        val stopRecordButton = findViewById<Button>(R.id.btnStop)
        stopRecordButton.setOnClickListener {
            stopRecordingAudio()
            playBackAudio()
        }
    }

    // Intercept the user's permission choice
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if this result matches our audio request code
        if (requestCode == REQ_RECORD_AUDIO) {
            // Check if the user clicked "Allow" (index 0 because we only asked for 1 permission)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecordingAudio()
            } else {
                Toast.makeText(this, "Permission denied to record audio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startRecordingAudio() {
        Toast.makeText(this, "Recording started...", Toast.LENGTH_SHORT).show()

        try {
            // Check if the user's device is running Android 12 (API 31) or higher
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(this) // Modern way for API 31+
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder() // Legacy way for API 30 and below
            }

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile)
                prepare()
                start()
            }

            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecordingAudio() {
        Toast.makeText(this, "Recording stopped.", Toast.LENGTH_SHORT).show()

        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        Toast.makeText(this, "Recording stopped.", Toast.LENGTH_SHORT).show()
    }

    private fun playBackAudio() {
        mediaPlayer?.release()
        mediaPlayer = null

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(outputFile)

                // Add an error listener to catch playback bugs
                setOnErrorListener { mp, what, extra ->
                    Toast.makeText(this@MainActivity, "Player Error: $what", Toast.LENGTH_SHORT).show()
                    true
                }

                prepare()
                start()
            }
            Toast.makeText(this, "Playing audio...", Toast.LENGTH_SHORT).show()

            // Automatically clean up when the audio finishes playing
            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
                Toast.makeText(this, "Playback finished", Toast.LENGTH_SHORT).show()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Could not play audio file", Toast.LENGTH_SHORT).show()
        }
    }
}