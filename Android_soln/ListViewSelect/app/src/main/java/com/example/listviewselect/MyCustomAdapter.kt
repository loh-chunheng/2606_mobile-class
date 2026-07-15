package com.example.listviewselect

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyCustomAdapter(
    private val context: Context,
    protected var toons: Array<String>,
    protected var captions: Array<String>
) : ArrayAdapter<Any?>(
    context, R.layout.row
) {
    init {
        addAll(*arrayOfNulls<Any>(toons.size))
    }

    override fun getView(pos: Int, view: View?, parent: ViewGroup): View {
        var _view = view

        if (_view == null) {
            val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            // if we are not responsible for adding the view to the parent,
            // then attachToRoot should be 'false' (which is in our case)
            _view = inflater.inflate(R.layout.row, parent, false)
        }

        // set the image for ImageView
        val imageView = _view!!.findViewById<ImageView>(R.id.imageView)
        val id = context.resources.getIdentifier(toons[pos],"drawable", context.packageName)
        imageView?.setImageResource(id)

        // set the text for TextView
        val textView = _view.findViewById<TextView>(R.id.textView)
        textView?.text = captions[pos]
        
        return _view
    }
}
