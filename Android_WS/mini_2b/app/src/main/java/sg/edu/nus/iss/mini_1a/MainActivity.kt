package sg.edu.nus.iss.mini_1a

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVisit = findViewById<Button>(R.id.btn_visit)

        btnVisit.setOnClickListener {
            val uri = Uri.parse("https://www.nus.edu.sg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        val btnLocate = findViewById<Button>(R.id.btn_locate)
        btnLocate.setOnClickListener {
            val uri = Uri.parse("geo:1.296643,103.776398");
            val intent = Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        val btnCall = findViewById<Button>(R.id.btn_call)
        btnCall.setOnClickListener {
            val uri = Uri.parse("tel:+65 6516 6666");
            val intent = Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
        }

        val btnEmail = findViewById<Button>(R.id.btn_email)
        btnEmail.setOnClickListener {
            val email = "someone@somewhere.com"
            val subject = "For Enquiry"
            val body = "Please state your enquiry..."

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, body)
            }

            startActivity(intent);
        }

    }

}