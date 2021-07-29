package com.slimakoi.aminox

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class InformationActivity : AppCompatActivity() {
    @SuppressLint("HardwareIds", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.activity_information)

        val infoBoard = findViewById<TextView>(R.id.infoBoard)
        val infoBrand = findViewById<TextView>(R.id.infoBrand)
        val infoManufacturer = findViewById<TextView>(R.id.infoManufacturer)
        val infoModel = findViewById<TextView>(R.id.infoModel)
        val infoBootloader = findViewById<TextView>(R.id.infoBootloader)
        val infoDevice = findViewById<TextView>(R.id.infoDevice)
        val infoProduct = findViewById<TextView>(R.id.infoProduct)
        val infoDisplay = findViewById<TextView>(R.id.infoDisplay)
        val infoFingerprint = findViewById<TextView>(R.id.infoFingerprint)
        val infoHardware = findViewById<TextView>(R.id.infoHardware)
        val infoHost = findViewById<TextView>(R.id.infoHost)
        val infoId = findViewById<TextView>(R.id.infoId)
        val infoTags = findViewById<TextView>(R.id.infoTags)
        val infoTime = findViewById<TextView>(R.id.infoTime)
        val infoUser = findViewById<TextView>(R.id.infoUser)
        val infoOsVersion = findViewById<TextView>(R.id.infoOsVersion)
        val infoOsCodename = findViewById<TextView>(R.id.infoOsCodename)
        val infoOsIncremental = findViewById<TextView>(R.id.infoOsIncremental)
        val infoOsRelease = findViewById<TextView>(R.id.infoOsRelease)
        val infoSecurity = findViewById<TextView>(R.id.infoSecurity)
        val infoAndroidId = findViewById<TextView>(R.id.infoAndroidId)
        val infoSignatureId = findViewById<TextView>(R.id.infoSignatureId)
        val infoPremium = findViewById<TextView>(R.id.infoPremium)
        val infoDeviceId = findViewById<TextView>(R.id.infoDeviceId)

        val latestVersion = findViewById<TextView>(R.id.textLatestVersion)
        val appVersion = findViewById<TextView>(R.id.textCurrentVersion)
        val infoCreditsSlimakoi = findViewById<TextView>(R.id.infoCreditsSlimakoi)
        val infoCreditsAbodx = findViewById<TextView>(R.id.infoCreditsAbodx)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val current = this.packageManager.getPackageInfo(this.packageName, 0)
        appVersion.text = "Current Version: ${current.versionName}-${current.versionCode}"
        latestVersion.text = "Latest Version: $applicationLatestName-$applicationLatestCode"

        infoBoard.text = Build.BOARD
        infoBrand.text = Build.BRAND
        infoManufacturer.text = Build.MANUFACTURER
        infoModel.text = Build.MODEL
        infoBootloader.text = Build.BOOTLOADER
        infoDevice.text = Build.DEVICE
        infoProduct.text = Build.PRODUCT
        infoDisplay.text = Build.DISPLAY
        infoFingerprint.text = Build.FINGERPRINT
        infoHardware.text = Build.HARDWARE
        infoHost.text = Build.HOST
        infoId.text = Build.ID
        infoTags.text = Build.TAGS
        infoTime.text = Build.TIME.toString()
        infoUser.text = Build.USER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { infoOsVersion.text = Build.VERSION.BASE_OS } else { infoOsVersion.text = "Unavailable" }
        infoOsCodename.text = Build.VERSION.CODENAME
        infoOsIncremental.text = Build.VERSION.INCREMENTAL
        infoOsRelease.text = "${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { infoSecurity.text = Build.VERSION.SECURITY_PATCH } else { infoSecurity.text = "Unavailable" }
        infoAndroidId.text = AND_ID
        infoSignatureId.text = DEV_SIG
        infoPremium.text = WHITELISTED.toString()
        infoDeviceId.text = deviceId

        infoCreditsSlimakoi.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/Slimakoi"))) }
        infoCreditsAbodx.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://instabio.cc/21127nhPn3y"))) }

        infoBoard.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoBoard.text))
            Toast.makeText(this, "Copied 'BOARD' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoBrand.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoBrand.text))
            Toast.makeText(this, "Copied 'BRAND' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoManufacturer.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoManufacturer.text))
            Toast.makeText(this, "Copied 'MANUFACTURER' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoModel.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoModel.text))
            Toast.makeText(this, "Copied 'MODEL' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoBootloader.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoBootloader.text))
            Toast.makeText(this, "Copied 'BOOTLOADER' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoDevice.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoDevice.text))
            Toast.makeText(this, "Copied 'DEVICE' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoProduct.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoProduct.text))
            Toast.makeText(this, "Copied 'PRODUCT' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoDisplay.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoDisplay.text))
            Toast.makeText(this, "Copied 'DISPLAY' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoFingerprint.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoFingerprint.text))
            Toast.makeText(this, "Copied 'FINGERPRINT' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoHardware.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoHardware.text))
            Toast.makeText(this, "Copied 'HARDWARE' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoHost.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoHost.text))
            Toast.makeText(this, "Copied 'HOST' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoId.text))
            Toast.makeText(this, "Copied 'ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoTags.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoTags.text))
            Toast.makeText(this, "Copied 'TAGS' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoTime.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoTime.text))
            Toast.makeText(this, "Copied 'TIME' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoUser.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoUser.text))
            Toast.makeText(this, "Copied 'USER' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsVersion.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsVersion.text))
            Toast.makeText(this, "Copied 'BASE_OS' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsCodename.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsCodename.text))
            Toast.makeText(this, "Copied 'CODENAME' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsIncremental.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsIncremental.text))
            Toast.makeText(this, "Copied 'INCREMENTAL' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsRelease.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsRelease.text))
            Toast.makeText(this, "Copied 'RELEASE' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoSecurity.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoSecurity.text))
            Toast.makeText(this, "Copied 'SECURITY PATCH' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoAndroidId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoAndroidId.text))
            Toast.makeText(this, "Copied 'ANDROID ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoSignatureId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoSignatureId.text))
            Toast.makeText(this, "Copied 'SIGNATURE ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoPremium.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoPremium.text))
            Toast.makeText(this, "Copied 'PREMIUM' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoDeviceId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoDeviceId.text))
            Toast.makeText(this, "Copied 'DEVICE ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }
    }
}

/*
doAsync {
    while (isChroma) {
        for (color in listOf("#BB0000", "#BB1100", "#BB2200", "#BB3300", "#BB4400", "#BB5500", "#BB6600", "#BB7700", "#BB8800", "#BB9900", "#BBAA00", "#BBBB00", "#AABB00", "#99BB00", "#88BB00", "#77BB00", "#66BB00", "#55BB00", "#44BB00", "#33BB00", "#22BB00", "#11BB00", "#00BB00", "#00BB11", "#00BB22", "#00BB33", "#00BB44", "#00BB55", "#00BB66", "#00BB77", "#00BB88", "#00BB99", "#00BBAA", "#00BBBB", "#00AABB", "#0099BB", "#0088BB", "#0077BB", "#0066BB", "#0055BB", "#0044BB", "#0033BB", "#0022BB", "#0011BB", "#0000BB", "#1100BB", "#2200BB", "#3300BB", "#4400BB", "#5500BB", "#6600BB", "#7700BB", "#8800BB", "#9900BB", "#AA00BB", "#BB00BB", "#BB00AA", "#BB0099", "#BB0088", "#BB0077", "#BB0066", "#BB0055", "#BB0044", "#BB0033", "#BB0022", "#BB0011")) {
            this@InformationActivity.window.statusBarColor = Color.parseColor(color); Thread.sleep(50)
        }
    }
    this@InformationActivity.window.statusBarColor = Color.parseColor("#4400BB"); Thread.sleep(50)
}
*/