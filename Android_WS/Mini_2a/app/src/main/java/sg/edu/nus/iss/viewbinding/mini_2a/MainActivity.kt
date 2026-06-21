package sg.edu.nus.iss.viewbinding.mini_2a

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sg.edu.nus.iss.viewbinding.mini_2a.adapters.MyCustomAdapter
import sg.edu.nus.iss.viewbinding.mini_2a.data.ToonItem

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    private lateinit var toonList: List<ToonItem>
    private lateinit var customAdapter: MyCustomAdapter

    private var lastClickedToon: ToonItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toonList = listOf(
            ToonItem(R.drawable.hug, "Hug, please!"),
            ToonItem(R.drawable.laugh, "So funny!"),
            ToonItem(R.drawable.peep, "You there?"),
            ToonItem(R.drawable.snore, "I'm out..."),
            ToonItem(R.drawable.stop, "Stop It!"),
            ToonItem(R.drawable.tired, "I'm beat..."),
            ToonItem(R.drawable.full, "So Full!"),
            ToonItem(R.drawable.what, "What?!"),
            ToonItem(R.drawable.afraid, "Leave me alone..."),
            ToonItem(R.drawable.no_way, "No Way!")
        )

        customAdapter = MyCustomAdapter(this, toonList)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = customAdapter

        listView.onItemClickListener = this

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val clickedToon = toonList[position]

        if (lastClickedToon != clickedToon) {
            lastClickedToon?.isSelected = false
            clickedToon.isSelected = true
            lastClickedToon = clickedToon
        } else {
            // If the user clicked the EXACT SAME row again, just uncheck it
            clickedToon.isSelected = !clickedToon.isSelected
            lastClickedToon = null
        }

        customAdapter.notifyDataSetChanged()

    }
}