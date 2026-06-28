package sg.edu.nus.iss.inclass.webview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sg.edu.nus.iss.inclass.webview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initButtons()
    }

    private fun initButtons() {
        binding.launchButton.setOnClickListener {
            val externalUrl ="https://nus.edu.sg"
            launchExternalPage(externalUrl)
        }
    }
    private fun launchExternalPage(externalUrl: String) {

        // to launch webpage in a webview
        val intent = Intent(this, WebviewActivity::class.java).apply {
            putExtra("URL", externalUrl)
        }
        startActivity(intent)

        // to launch in a browser
        /*
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(externalUrl))
        startActivity(intent)
         */
    }


}