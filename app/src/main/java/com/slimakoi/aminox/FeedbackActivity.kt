package com.slimakoi.aminox

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.feedback)

        val feedbackInputName = findViewById<EditText>(R.id.feedbackInputName)
        val feedbackInputMessage = findViewById<EditText>(R.id.feedbackInputMessage)
        val feedbackSend = findViewById<Button>(R.id.feedbackSend)

        val feedbackAlert = AlertDialog.Builder(this)

        feedbackAlert.setTitle("Read before sending feedback!")
        feedbackAlert.setMessage("Here you can message Slimakoi about AminoX, you can send new feature ideas for the app or report bugs, please be specific on your message!\n\nAlso please don't spam this, thanks!")
        feedbackAlert.setNegativeButton("Understood") { dialog, _ -> dialog.dismiss() }
        feedbackAlert.setCancelable(false)
        feedbackAlert.show()

        feedbackSend.setOnClickListener {
            if (BLACKLISTED == "true") {
                Toast.makeText(this, "You are blocked from sending feedback!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (SENT_FEEDBACK) {
                Toast.makeText(this, "Already sent feedback today!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val feedName = feedbackInputName.text.toString()
            val feedMessage = feedbackInputMessage.text.toString()

            if (feedName == "") {
                Toast.makeText(this, "Feedback name should be filled!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (feedMessage == "") {
                Toast.makeText(this, "Feedback message should be filled!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (feedMessage.length > 1001) {
                Toast.makeText(this, "Message is above 1000 characters!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            WebHook(this).sendFeedback(feedName, feedMessage)
            Toast.makeText(this, "Sent feedback! Thanks", Toast.LENGTH_LONG).show()
            SENT_FEEDBACK = true
        }
    }
}