package sg.edu.nus.iss.mini_2c

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etCount: EditText
    private lateinit var btnToggle: Button
    private var computationThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etCount = findViewById(R.id.etCount)
        btnToggle = findViewById(R.id.btnToggle)

        btnToggle.setOnClickListener {
            if (computationThread == null) {
                startComputation()
            } else {
                stopComputation()
            }
        }
    }

    private fun startComputation() {
        // Update UI to state: Running
        btnToggle.text = "STOP"
        etCount.setText("") // Clear any previous values
        etCount.hint = "Computing ..."

        // Create and start the background thread
        computationThread = Thread {
            var count = 0

            // Loop up to MAX, checking for interrupt signal constantly
            while (count < Int.MAX_VALUE && !Thread.interrupted()) {
                count++
            }

            val completedSuccessfully = (count == Int.MAX_VALUE)

            if (completedSuccessfully) {
                // Thread finished naturally
                runOnUiThread {
                    etCount.setText(count.toString())
                    btnToggle.text = "START"
                    computationThread = null // Reset thread tracking
                }
            } // Do nothing if it's interrupted. Let stopComputation handles
        }.apply { start() } // Initialize and run immediately
    }

    private fun stopComputation() {
        // Interrupt the active thread
        computationThread?.interrupt()
        computationThread = null

        // Reset the UI immediately on the main thread
        resetUI()
    }

    private fun resetUI() {
        etCount.setText("")
        etCount.hint = ""
        btnToggle.text = "START"
    }

    override fun onDestroy() {
        super.onDestroy()
        // Make sure we kill the thread if the activity destroys
        computationThread?.interrupt()
    }

}