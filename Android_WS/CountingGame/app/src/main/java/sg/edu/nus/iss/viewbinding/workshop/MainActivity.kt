package sg.edu.nus.iss.viewbinding.workshop

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var counterTextView: TextView
    private lateinit var upRadioButton: RadioButton
    private lateinit var downRadioButton: RadioButton
    private lateinit var tenCheckBox: CheckBox
    private lateinit var countButton: Button

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        counterTextView = findViewById<TextView>(R.id.counter_textview)
        upRadioButton = findViewById<RadioButton>(R.id.up_radio_button)
        downRadioButton = findViewById<RadioButton>(R.id.down_radio_button)
        tenCheckBox = findViewById<CheckBox>(R.id.ten_checkbox)
        countButton = findViewById<Button>(R.id.count_button)

        initButtons()
        initUIs()
    }

    private fun initButtons() {
        countButton.setOnClickListener {
            if (upRadioButton.isChecked) {
                if (tenCheckBox.isChecked) {
                    count += 10
                } else {
                    count ++
                }
            } else if (downRadioButton.isChecked) {
                if (tenCheckBox.isChecked) {
                    count -= 10
                } else {
                    count--
                }
            } else {
                System.err.println("Something wrong with the program")
            }

            counterTextView.text = count.toString()
        }
    }

    private fun initUIs() {
        counterTextView.text = count.toString()
    }
}