package com.slimakoi.aminox

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EasterEgg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.easter_egg)

        val nekoImage = findViewById<ImageView>(R.id.nekoImage)

        nekoImage.setOnClickListener {
            Toast.makeText(this, "Nyah!", Toast.LENGTH_SHORT).show()
        }
    }
}