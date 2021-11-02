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
        val infoCreditsKapidev = findViewById<TextView>(R.id.infoCreditsKapidev)

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
        infoOsVersion.text = Build.VERSION.BASE_OS
        infoOsCodename.text = Build.VERSION.CODENAME
        infoOsIncremental.text = Build.VERSION.INCREMENTAL
        infoOsRelease.text = "${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})"
        infoSecurity.text = Build.VERSION.SECURITY_PATCH
        infoAndroidId.text = AND_ID
        infoSignatureId.text = DEV_SIG
        infoPremium.text = WHITELISTED.toString()
        infoDeviceId.text = deviceId

        infoCreditsSlimakoi.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/Slimakoi"))) }
        infoCreditsAbodx.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://instabio.cc/21127nhPn3y"))) }
        infoCreditsKapidev.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Kapideveloper"))) }

        infoBoard.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoBoard.text))
            Toast.makeText(this, "Copied 'BOARD' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoBoard.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoBrand.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoBrand.text))
            Toast.makeText(this, "Copied 'BRAND' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoBrand.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoManufacturer.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoManufacturer.text))
            Toast.makeText(this, "Copied 'MANUFACTURER' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoManufacturer.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoModel.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoModel.text))
            Toast.makeText(this, "Copied 'MODEL' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoModel.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoBootloader.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoBootloader.text))
            Toast.makeText(this, "Copied 'BOOTLOADER' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoBootloader.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoDevice.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoDevice.text))
            Toast.makeText(this, "Copied 'DEVICE' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoDevice.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoProduct.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoProduct.text))
            Toast.makeText(this, "Copied 'PRODUCT' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoProduct.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoDisplay.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoDisplay.text))
            Toast.makeText(this, "Copied 'DISPLAY' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoDisplay.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoFingerprint.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoFingerprint.text))
            Toast.makeText(this, "Copied 'FINGERPRINT' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoFingerprint.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoHardware.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoHardware.text))
            Toast.makeText(this, "Copied 'HARDWARE' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoHardware.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoHost.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoHost.text))
            Toast.makeText(this, "Copied 'HOST' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoHost.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoId.text))
            Toast.makeText(this, "Copied 'ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoId.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoTags.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoTags.text))
            Toast.makeText(this, "Copied 'TAGS' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoTags.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoTime.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoTime.text))
            Toast.makeText(this, "Copied 'TIME' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoTime.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoUser.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoUser.text))
            Toast.makeText(this, "Copied 'USER' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoUser.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoOsVersion.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsVersion.text))
            Toast.makeText(this, "Copied 'BASE_OS' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsVersion.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoOsCodename.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsCodename.text))
            Toast.makeText(this, "Copied 'CODENAME' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsCodename.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoOsIncremental.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsIncremental.text))
            Toast.makeText(this, "Copied 'INCREMENTAL' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsIncremental.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoOsRelease.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoOsRelease.text))
            Toast.makeText(this, "Copied 'RELEASE' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoOsRelease.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoSecurity.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoSecurity.text))
            Toast.makeText(this, "Copied 'SECURITY PATCH' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoSecurity.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoAndroidId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoAndroidId.text))
            Toast.makeText(this, "Copied 'ANDROID ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoAndroidId.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoSignatureId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoSignatureId.text))
            Toast.makeText(this, "Copied 'SIGNATURE ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoSignatureId.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoPremium.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoPremium.text))
            Toast.makeText(this, "Copied 'PREMIUM' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoPremium.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }

        infoDeviceId.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoDeviceId.text))
            Toast.makeText(this, "Copied 'DEVICE ID' to Clipboard!", Toast.LENGTH_SHORT).show()
        }

        infoDeviceId.setOnLongClickListener {
            this.startActivity(Intent(this, EasterEgg::class.java))
            true
        }
    }
}