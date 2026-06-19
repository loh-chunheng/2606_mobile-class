package sg.edu.nus.iss.viewbinding.mini_2a.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import sg.edu.nus.iss.viewbinding.mini_2a.R

class MyCustomAdapter(
    private val context: Context,
    protected var toons: Array<String>,
    protected var captions: Array<String>
) : ArrayAdapter<Any?>(context, R.layout.row_layout, captions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Reuse the view if possible, otherwise inflate a new row layout
        val rowView = convertView ?: LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false)

        // Find the ImageView and TextView inside your row layout
        val imageView = rowView.findViewById<ImageView>(R.id.imageView)
        val textView = rowView.findViewById<TextView>(R.id.textView)

        // Set the text caption for the current row
        textView.text = captions[position]

        // Set the image
        // Tip: If 'toons' contains drawable resource names, you can dynamically find them like this:
        val imageName = toons[position]
        val imageId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

        if (imageId != 0) {
            imageView.setImageResource(imageId)
        } else {
            imageView.setImageResource(R.drawable.placeholder) // Fallback image if not found
        }

        return rowView
    }
}