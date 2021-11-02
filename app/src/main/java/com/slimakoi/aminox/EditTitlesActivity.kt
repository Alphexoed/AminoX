package com.slimakoi.aminox

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import kotlinx.android.synthetic.main.activity_edit_titles.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception


var titleNum: Int = 0

class EditTitlesActivity : AppCompatActivity() {
    private var jOb: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.activity_edit_titles)

        val radioGroupTitles = findViewById<RadioGroup>(R.id.radioGroupTitles)
        val buttonSend = findViewById<Button>(R.id.buttonSend)

        var color = 0

        val credits = findViewById<TextView>(R.id.infoTxtCredits3)
        val aminoCreation = findViewById<TextView>(R.id.aminoCreation)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        credits.setOnClickListener { val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/Slimakoi")); startActivity(i) }

        // Show info popup
        val invisibleLoader = AlertDialog.Builder(this).create()
        invisibleLoader.setTitle("How to get invisible titles")
        invisibleLoader.setMessage("For you to get invisible titles, make the title pure black (#000000)\nIf you want a black title and not the invisible one, add a digit to the color (#010101 for example)")
        invisibleLoader.show()

        val aminoObject = JSONObject(getAminoProfile().toString())
        val json: JSONObject = aminoObject.getJSONObject("userProfile")
        aminoCreation.text = json.getString("createdTime")

        try {
            for (x in 0 until 100) {
                val titleObject = json.getJSONObject("extensions").getJSONArray("customTitles")[x]
                val titleName = JSONObject(titleObject.toString()).getString("title")
                var titleColor = JSONObject(titleObject.toString()).getString("color")

                if (titleColor == "#") {
                    titleColor = "#000000"
                }

                val rb = Button(this)
                rb.text = titleName
                rb.textAlignment = View.TEXT_ALIGNMENT_CENTER
                //rb.background.setTint(Color.parseColor(titleColor))
                try {
                    rb.setBackgroundColor(Color.parseColor(titleColor.toString()))
                } catch (e: Exception) {
                    rb.setBackgroundColor(Color.parseColor("#000000"))
                    Log.println(Log.ERROR, "SYSTEM-ERROR", "Error while loading colored title -- $e")
                }

                val cp = ColorPicker(this, color.red, color.green, color.blue)

                rb.setOnClickListener {
                    cp.show()
                    cp.findViewById<Button>(R.id.okColorButton).setOnClickListener {
                        //rb.background.setTint(cp.color)
                        rb.setBackgroundColor(cp.color)
                        color = cp.color
                    cp.dismiss()
                    }
                }

                radioGroupTitles.addView(rb)
            }
        } catch (e: JSONException) {
            Log.println(Log.ERROR, "SYSTEM-ERROR", "Error while loading titles -- $e")
        }

        aminoName.text = json.getString("nickname").toString()
        aminoName.setTypeface(null, Typeface.BOLD)
        aminoImage.loadUrl(json.getString("icon").toString())
        aminoImage.settings.loadWithOverviewMode = true
        aminoImage.settings.useWideViewPort = true

        buttonSend.setOnClickListener {
            val count: Int = radioGroupTitles.childCount
            val listOfButtons = ArrayList<Button>()
            val jsonTitles = JSONArray()
            val jsonTitleData = JSONObject()

            for (i in 0 until count) {
                val o: View = radioGroupTitles.getChildAt(i)
                if (o is Button) {
                    listOfButtons.add(o)

                    val colorTemp: ColorDrawable = o.background as ColorDrawable
                    var color = Integer.toHexString(colorTemp.color).substring(2)

                    if (color == "000000") {
                        color = "AA000000"
                    }

                    val temp = JSONObject()
                    temp.put("title", o.text)
                    temp.put("color", "#$color")
                    jsonTitles.put(temp)
                }
                jsonTitleData.put("customTitles", jsonTitles)
            }

            val sendJson = sendTitleEdit(jsonOb = jsonTitleData.toString())
            val titleSendJson = JSONObject(sendJson.toString())

            Toast.makeText(this, titleSendJson.getString("api:message"), Toast.LENGTH_SHORT).show()
        }
    }
}