package com.example.minirecordaudio

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val REQ_RECORD_AUDIO = 1

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var outputFile: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.start_record_btn)?.setOnClickListener(this)
        findViewById<Button>(R.id.stop_record_btn)?.setOnClickListener(this)

        outputFile = "${externalCacheDir?.absolutePath}/audio.3gp"
    }

    override fun onClick(v: View) {
        if (v.id == R.id.start_record_btn) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO), REQ_RECORD_AUDIO);
        }
        else if (v.id == R.id.stop_record_btn) {
            stopRecordAudio()
            playRecordedAudio()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQ_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecordAudio()
            }
        }
    }

    fun startRecordAudio() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setOutputFile(outputFile)
        mediaRecorder?.prepare()
        mediaRecorder?.start()
    }

    fun stopRecordAudio() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    fun playRecordedAudio() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(outputFile)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}