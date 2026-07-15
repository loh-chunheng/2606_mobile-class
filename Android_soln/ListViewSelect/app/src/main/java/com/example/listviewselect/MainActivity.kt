package com.example.listviewselect

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private var lastView: View? = null  // track last selected item

    private val toons = arrayOf("hug", "laugh", "peep",
        "snore", "stop", "tired",
        "full", "what", "afraid", "no_way")

    private val captions = arrayOf("Hug, please!", "So funny!", "You there?",
        "I'm out...", "Stop It!", "I'm beat...",
        "So Full!", "What?!", "Leave me alone...", "No Way!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.listView)
        listView?.adapter = MyCustomAdapter(this, toons, captions)
        listView?.setOnItemClickListener(this)
    }

    override fun onItemClick(av: AdapterView<*>?, v: View, pos: Int, id: Long) {
        // clear last selected item
        if (lastView != null) {
            val lastTextView = lastView?.findViewById<TextView>(R.id.textView)
            lastTextView?.setTypeface(null, Typeface.NORMAL)
            lastTextView?.setBackgroundColor(0x00000000) // transparent
            lastTextView?.isAllCaps = false
        }

        // select clicked item
        val textView = v.findViewById<TextView>(R.id.textView)
        textView?.setTypeface(null, Typeface.BOLD)
        textView?.setBackgroundColor(Color.parseColor("#89cff0")) // blue
        textView?.isAllCaps = true

        // remember last selected item
        lastView = v
    }
}