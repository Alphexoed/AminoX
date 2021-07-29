package com.slimakoi.aminox

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject

class CommunitySelectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.activity_community_selector)
        val buttonCommunity = findViewById<Button>(R.id.buttonCommunity)
        val profileImage = findViewById<WebView>(R.id.profileImage)
        val profileName = findViewById<TextView>(R.id.profileName)
        val profileCreation = findViewById<TextView>(R.id.profileCreation)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val buttonCopySid = findViewById<Button>(R.id.buttonCopySid)
        val credits = findViewById<TextView>(R.id.infoTxtCredits2)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        credits.setOnClickListener { val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/Slimakoi")); startActivity(i) }

        // Set Picture
        profileName.text = USER_NAME
        profileCreation.text = USER_CREATION
        profileName.setTypeface(null, Typeface.BOLD)
        profileImage.loadUrl(USER_ICON)
        profileImage.settings.loadWithOverviewMode = true
        profileImage.settings.useWideViewPort = true

        // Set Variables
        val communityObject = JSONObject(getCommunityList().toString())
        val jsonCom: JSONArray = communityObject.getJSONArray("communityList")
        var comTitleList: Array<String> = arrayOf()
        var comIdList: Array<String> = arrayOf()

        // List Communities the User is in
        for (i in 0 until jsonCom.length()) {
            val item = jsonCom.getJSONObject(i)
            val comObj = JSONObject(item.toString())
            val comTitleStr = comObj.getString("name")
            val comIdStr = comObj.getString("ndcId")

            comTitleList = append(comTitleList, comTitleStr)
            comIdList = append(comIdList, comIdStr)

            // Fill the Radio Group with the Communities Gathered
            val rb = RadioButton(this)
            rb.text = comTitleStr
            rb.setTextColor(Color.WHITE)
            radioGroup.addView(rb)
        }

        buttonCommunity.setOnClickListener {
            val radioId = radioGroup.checkedRadioButtonId
            try {
                COMMUNITY_NAME = comTitleList[radioId - 1]
                COMMUNITY_ID = comIdList[radioId - 1]
            } catch (e: ArrayIndexOutOfBoundsException) {
                Toast.makeText(this, "No Community Selected!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Selected Community : $COMMUNITY_NAME", Toast.LENGTH_LONG).show()

            startActivity(Intent(this, ActionSelectorActivity::class.java))
        }

        buttonCopySid.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, SID))
            Toast.makeText(this, "Copied 'SID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }
    }
}
