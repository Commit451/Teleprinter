package com.commit451.teleprinter.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.commit451.teleprinter.Teleprinter

class MainActivity : AppCompatActivity() {

    lateinit var root: ViewGroup
    lateinit var editText: EditText
    lateinit var text: TextView

    lateinit var teleprinter: Teleprinter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        teleprinter = Teleprinter(this)

        teleprinter.addOnKeyboardOpenedListener {
            Toast.makeText(this, "Keyboard opened", Toast.LENGTH_SHORT)
                    .show()
            text.text = "Open"
        }
        teleprinter.addOnKeyboardClosedListener {
            Toast.makeText(this, "Keyboard closed", Toast.LENGTH_SHORT)
                    .show()
            text.text = "Closed"
        }
        root = findViewById(R.id.root)
        text = findViewById(R.id.text)
        editText = findViewById(R.id.edit_text)

        findViewById<View>(R.id.button_show).setOnClickListener { teleprinter.showKeyboard(editText) }
        findViewById<View>(R.id.button_hide).setOnClickListener { teleprinter.hideKeyboard() }
        findViewById<View>(R.id.button_is_keyboard_showing).setOnClickListener {
            Toast.makeText(this, if (teleprinter.isKeyboardShowing()) "Yes" else "No", Toast.LENGTH_SHORT)
                    .show()
        }
        //seems to need this delay
        editText.postDelayed({
            teleprinter.toggleKeyboard()
        }, 200)
    }
}
