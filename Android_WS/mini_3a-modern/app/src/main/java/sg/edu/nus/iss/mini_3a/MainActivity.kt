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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var outputFile: String

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startRecordingAudio()
        } else {
            Toast.makeText(this, "Permission required to record audio", Toast.LENGTH_SHORT).show()
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

        outputFile = "${cacheDir?.absolutePath}/audio.mp4"

        val startRecordButton = findViewById<Button>(R.id.btnStart)

        startRecordButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startRecordingAudio()
                }

                // Show a rationale if the user denied it once before
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) -> {
                    Toast.makeText(this, "Audio permission is needed to record your voice.", Toast.LENGTH_LONG).show()
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }

                // Ask for permission directly
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        }

        val stopRecordButton = findViewById<Button>(R.id.btnStop)
        stopRecordButton.setOnClickListener {
            stopRecordingAudio()
            playBackAudio()
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

        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    private fun playBackAudio() {
        Toast.makeText(this, "Playing back audio...", Toast.LENGTH_SHORT).show()

        mediaPlayer?.release()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(outputFile)
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