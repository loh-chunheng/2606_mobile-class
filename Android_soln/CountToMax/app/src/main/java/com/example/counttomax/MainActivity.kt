package com.example.counttomax

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var bgThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBtn()
    }

    private fun initBtn() {
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener(this)
    }

    private fun updateUI(result_text: String, btn_text: String) {
        val tv = findViewById<TextView>(R.id.result)
        tv.text = result_text

        val btn = findViewById<Button>(R.id.btn)
        btn.text = btn_text
    }

    private fun startCompute() : Thread {
        Log.d("CountToMax", "Current sum: ${Int.MAX_VALUE}")

        return Thread {
            var sum = 0L

            for (i in 0 until Int.MAX_VALUE) {
                sum += i

                if (Thread.interrupted()) {
                    bgThread = null
                    break   // exit for-loop
                }
            }

            // refresh UI
            if (bgThread != null) {
                bgThread = null
                runOnUiThread { updateUI(sum.toString(), getString(R.string.start)) }
            }
            else {
                runOnUiThread { updateUI("", getString(R.string.start)) }
            }
        }
    }

    private fun stopCompute() {
        bgThread?.interrupt()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn) {
            if (bgThread == null) {
                bgThread = startCompute()
                bgThread?.start()
                updateUI(getString(R.string.computing), getString(R.string.stop))
            }
            else {
                stopCompute()
                updateUI("", getString(R.string.start))
            }
        }
    }
}