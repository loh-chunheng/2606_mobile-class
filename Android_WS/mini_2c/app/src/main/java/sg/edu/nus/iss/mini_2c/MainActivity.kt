package sg.edu.nus.iss.mini_2c

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var savedQuote: String? = null

    private val writeQuoteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            savedQuote = result.data?.getStringExtra("quote")
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

        val btnWrite = findViewById<Button>(R.id.btn_write)
        btnWrite.setOnClickListener {
            val intent = Intent(this,WriteQuoteActivity::class.java)
            writeQuoteLauncher.launch(intent)
        }

        val btnShow = findViewById<Button>(R.id.btn_show)
        btnShow.setOnClickListener {
            val intent = Intent(this,ShowQuoteActivity::class.java)
            intent.putExtra("quote", savedQuote)
            startActivity(intent)
        }

    }

}