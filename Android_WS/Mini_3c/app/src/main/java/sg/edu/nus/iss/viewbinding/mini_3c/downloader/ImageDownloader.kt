package sg.edu.nus.iss.viewbinding.mini_3c.downloader

import android.content.Context
import android.os.Environment
import java.io.File
import java.net.URL
import java.util.UUID

class ImageDownloader(private val context: Context) {

    fun makeFile(url: String): File {
        var ext = ""
        val pos = url.lastIndexOf(".")
        // searches the URL string from right to left to find the very last period (.)
        // return -1 if no period is found

        if (pos != -1) {
            ext = url.substring(pos)
        }

        // generate unique file name
        val filename = UUID.randomUUID().toString() + ext

        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        // or dir can be internal cache which is for temporary files
        // val dir = context.cacheDir

        return File(dir, filename)
    }

    // Stream-to-stream download. MUST be called from a background thread!
    fun downloadToFile(url: String, file: File) {
        URL(url).openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}