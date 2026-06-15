package sg.edu.nus.iss.mini_2c

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WriteQuoteActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_writequote)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.write_quote)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etQuote = findViewById<EditText>(R.id.et_quote)
        val btnOk = findViewById<Button>(R.id.btn_ok)
        btnOk.setOnClickListener {
            val quote = etQuote.text.toString()
            val returnIntent = Intent()
            returnIntent.putExtra("quote", quote)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }
}