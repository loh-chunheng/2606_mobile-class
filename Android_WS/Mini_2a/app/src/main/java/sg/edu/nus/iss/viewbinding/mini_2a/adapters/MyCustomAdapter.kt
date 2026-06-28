package sg.edu.nus.iss.viewbinding.mini_2a.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import sg.edu.nus.iss.viewbinding.mini_2a.R
import sg.edu.nus.iss.viewbinding.mini_2a.data.ToonItem

class MyCustomAdapter(
    context: Context,
    private val toonList: List<ToonItem>
) : ArrayAdapter<ToonItem>(context, 0, toonList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var rowView = convertView
        val holder: ViewHolder

        // If the recyclable view is null, inflate the custom layout and create a ViewHolder
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false)
            holder = ViewHolder(rowView)

            rowView.tag = holder
        } else {
            holder = rowView.tag as ViewHolder
        }

        val currentItem = getItem(position)

        if (currentItem == null) {
            holder.imageView.setImageResource(0)
            holder.textView.text = ""
            return rowView
        }

        holder.imageView.setImageResource(currentItem.imageResId)

        // Apply Styling Based on the Boolean State of isSelected
        if (currentItem.isSelected) {
            holder.textView.setTypeface(null, Typeface.BOLD)
            holder.textView.text = currentItem.caption.uppercase()
            holder.textView.setBackgroundColor(0xFF89CFF0.toInt()) // blue
        } else {
            holder.textView.setTypeface(null, Typeface.NORMAL)
            holder.textView.text = currentItem.caption
            holder.textView.setBackgroundColor(0x00000000)
        }


        return rowView
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView: TextView = view.findViewById(R.id.textView)    }
}