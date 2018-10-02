package com.commit451.teleprinter.sample

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.commit451.teleprinter.Teleprinter

class MainActivity : AppCompatActivity() {

    lateinit var root: ViewGroup
    lateinit var toolbar: Toolbar
    lateinit var editText: EditText
    lateinit var text: TextView

    lateinit var teleprinter: Teleprinter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        teleprinter = Teleprinter(this, true)

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
        toolbar = findViewById(R.id.toolbar)
        text = findViewById(R.id.text)
        editText = findViewById(R.id.edit_text)
        editText.append("Hello world")

        toolbar.title = "Teleprinter"
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        findViewById<View>(R.id.button_show).setOnClickListener { teleprinter.showKeyboard(editText) }
        findViewById<View>(R.id.button_hide).setOnClickListener { teleprinter.hideKeyboard() }
        findViewById<View>(R.id.button_is_keyboard_showing).setOnClickListener {
            Toast.makeText(this, if (teleprinter.isKeyboardShowing()) "Yes" else "No", Toast.LENGTH_SHORT)
                    .show()
        }

        teleprinter.showKeyboardWithDelay(editText)
    }

    override fun onDestroy() {
        teleprinter.hideKeyboard()
        super.onDestroy()
    }
}
