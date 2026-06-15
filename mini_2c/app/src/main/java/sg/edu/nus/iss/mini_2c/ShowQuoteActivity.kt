package sg.edu.nus.iss.mini_2c

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ShowQuoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_quote)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.show_quote)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvDisplay = findViewById<TextView>(R.id.vw_quote)

        // Extract the string using the EXACT same key name used in MainActivity
        val finalQuote = intent.getStringExtra("quote")

        if (!finalQuote.isNullOrEmpty()) {
            tvDisplay.text = finalQuote
        } else {
            tvDisplay.text = "No quote was provided!"
        }

        val btnOk = findViewById<Button>(R.id.btn_ok_vw)
        btnOk.setOnClickListener {
            finish()
        }
    }
}