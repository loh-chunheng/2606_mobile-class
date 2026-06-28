package sg.edu.nus.iss.viewbinding.mini_3c

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sg.edu.nus.iss.viewbinding.mini_3c.downloader.ImageDownloader
import java.io.File

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

        val urlEditText = findViewById<EditText>(R.id.editText)
        val fetchButton = findViewById<Button>(R.id.button)
        val downloader = ImageDownloader(this)

        fetchButton.setOnClickListener {
            val url = urlEditText.text.toString()
            val file = downloader.makeFile("downloaded_image.jpg")

            // Recommended method: Start a coroutine to handle background threading
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Download runs on the Background (IO) thread
                    downloader.downloadToFile(url, file)

                    // Switch back to the Main UI thread to update the image view
                    withContext(Dispatchers.Main) {
                        updateImageView(file)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // traditional method
            /*
            Thread {
                try {
                    downloader.downloadToFile(url, file)

                    runOnUiThread {
                        updateImageView(file)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
            */
        }


    }

    private fun updateImageView(file: File) {
        val imageView = findViewById<ImageView>(R.id.imageView)

        if (file.exists()) {
            // Convert the file on disk into a Bitmap that ImageView can read
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imageView.setImageBitmap(bitmap)
        }
    }
}