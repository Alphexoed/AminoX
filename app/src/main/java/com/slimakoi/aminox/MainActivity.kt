package com.slimakoi.aminox

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import org.jetbrains.anko.textColor
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {
    //private lateinit var analytics: FirebaseAnalytics

    //private lateinit var gso: GoogleSignInOptions
    //private lateinit var mGoogleSignInClient: GoogleSignInClient
    //private val RC_SIGN_IN: Int = 1
    //lateinit var signOut: Button

    @SuppressLint("HardwareIds")
    override fun onStart() {
        super.onStart()

        AND_ID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        for (i in 0 until whitelist.length()) {
            if (whitelist[i] == DEV_SIG) {
                WHITELISTED = true
            }
        }

        for (i in 0 until blacklist.length()) {
            if (blacklist[i] == DEV_SIG) {
                BLACKLISTED = "true"
            }
        }

        if (!applicationWorking) {
            WebHook(this).sendSetupError("Application is Closed", true)
            setupErrorTrigger(this, "Application is Closed\n\n[$AND_ID:$DEV_SIG]")
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.activity_main)

        //analytics = FirebaseAnalytics.getInstance(this)

        val aminoxTitle = findViewById<TextView>(R.id.aminoxTitle)
        val inputEmail = findViewById<TextView>(R.id.inputEmail)
        val inputSid = findViewById<TextView>(R.id.inputSid)
        val inputPassword = findViewById<TextView>(R.id.inputPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonSidLogin = findViewById<Button>(R.id.buttonSidLogin)
        val buttonInformation = findViewById<Button>(R.id.buttonInformation)
        val buttonFeedback = findViewById<Button>(R.id.buttonFeedback)
        val result = findViewById<TextView>(R.id.result)
        val loading = findViewById<RelativeLayout>(R.id.mainLoadingPanel)
        val checkButton = findViewById<ImageView>(R.id.imageUpdate)
        //val signInButton = findViewById<SignInButton>(R.id.sib)
        val credits = findViewById<TextView>(R.id.infoTxtCredits)
        val fontAsset: Typeface = Typeface.createFromAsset(assets, "font/textLogo_font.otf")
        val windowScale: Float = resources.displayMetrics.density

        //gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        try {
            aminoxTitle.typeface = fontAsset
            Log.println(Log.INFO, "SYSTEM-INFO", "Imported font successfully")
        } catch (e: Exception) {
            Log.println(Log.WARN, "SYSTEM-WARN", "Couldn't import custom font, using default -- $e")
        }

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val strNameCredits = getString(R.string.creditsLink)
        val strNameAbodx = getString(R.string.abodx)
        val strNameAppName = getString(R.string.app_name)
        val strNameAppDev = getString(R.string.developer_of_aminox)
        val strNameProtectionDev = getString(R.string.developing_app_protection)
        val strNameSlimakoi = getString(R.string.slimakoi)

        if (strNameCredits != object : Any() { var t = 0
                override fun toString(): String {
                    val buf = ByteArray(16); t = -1068340574
                    buf[0] = (t ushr 11).toByte(); t = -1357182198
                    buf[1] = (t ushr 11).toByte(); t = 1984055648
                    buf[2] = (t ushr 20).toByte(); t = 1763873173
                    buf[3] = (t ushr 2).toByte(); t = 1690571863
                    buf[4] = (t ushr 13).toByte(); t = 154234059
                    buf[5] = (t ushr 15).toByte(); t = -428219755
                    buf[6] = (t ushr 16).toByte(); t = -386793092
                    buf[7] = (t ushr 12).toByte(); t = 1084687674
                    buf[8] = (t ushr 17).toByte(); t = 740350252
                    buf[9] = (t ushr 9).toByte(); t = 1968868651
                    buf[10] = (t ushr 5).toByte(); t = -306294498
                    buf[11] = (t ushr 21).toByte(); t = 698816581
                    buf[12] = (t ushr 6).toByte(); t = -430009642
                    buf[13] = (t ushr 1).toByte(); t = -1394730251
                    buf[14] = (t ushr 17).toByte(); t = 677015279
                    buf[15] = (t ushr 14).toByte()
                    return String(buf)
                }
            }.toString()) {
            Toast.makeText(applicationContext, object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(51); t = -1072141623
                    buf[0] = (t ushr 3).toByte(); t = 1629883903
                    buf[1] = (t ushr 5).toByte(); t = 713797534
                    buf[2] = (t ushr 11).toByte(); t = 134585639
                    buf[3] = (t ushr 22).toByte(); t = 411005123
                    buf[4] = (t ushr 1).toByte(); t = -1009788012
                    buf[5] = (t ushr 3).toByte(); t = -1422925186
                    buf[6] = (t ushr 19).toByte(); t = 1972375729
                    buf[7] = (t ushr 15).toByte(); t = -294346800
                    buf[8] = (t ushr 16).toByte(); t = 561424843
                    buf[9] = (t ushr 2).toByte(); t = -1371742788
                    buf[10] = (t ushr 15).toByte(); t = 756708368
                    buf[11] = (t ushr 14).toByte(); t = -1557947973
                    buf[12] = (t ushr 2).toByte(); t = -1149610594
                    buf[13] = (t ushr 2).toByte(); t = 1595993664
                    buf[14] = (t ushr 1).toByte(); t = 927545711
                    buf[15] = (t ushr 20).toByte(); t = 38500646
                    buf[16] = (t ushr 11).toByte(); t = 1964012146
                    buf[17] = (t ushr 15).toByte(); t = -1636567441
                    buf[18] = (t ushr 16).toByte(); t = -1367289651
                    buf[19] = (t ushr 21).toByte(); t = 1580570411
                    buf[20] = (t ushr 10).toByte(); t = 203916666
                    buf[21] = (t ushr 21).toByte(); t = -120027467
                    buf[22] = (t ushr 17).toByte(); t = 2047086207
                    buf[23] = (t ushr 13).toByte(); t = -1472684201
                    buf[24] = (t ushr 21).toByte(); t = -1687391610
                    buf[25] = (t ushr 19).toByte(); t = 441913852
                    buf[26] = (t ushr 22).toByte(); t = 768287148
                    buf[27] = (t ushr 21).toByte(); t = -1810141398
                    buf[28] = (t ushr 8).toByte(); t = -1867444328
                    buf[29] = (t ushr 17).toByte(); t = -117248210
                    buf[30] = (t ushr 19).toByte(); t = 1746557648
                    buf[31] = (t ushr 14).toByte(); t = -584450152
                    buf[32] = (t ushr 22).toByte(); t = 1417691463
                    buf[33] = (t ushr 9).toByte(); t = -1010947095
                    buf[34] = (t ushr 19).toByte(); t = -1185610087
                    buf[35] = (t ushr 4).toByte(); t = -1690938486
                    buf[36] = (t ushr 22).toByte(); t = 383413307
                    buf[37] = (t ushr 8).toByte(); t = 274018048
                    buf[38] = (t ushr 23).toByte(); t = -152109427
                    buf[39] = (t ushr 20).toByte(); t = 516339678
                    buf[40] = (t ushr 7).toByte(); t = 1562210482
                    buf[41] = (t ushr 22).toByte(); t = -686806278
                    buf[42] = (t ushr 15).toByte(); t = 316460770
                    buf[43] = (t ushr 5).toByte(); t = -976104581
                    buf[44] = (t ushr 3).toByte(); t = 1253762302
                    buf[45] = (t ushr 9).toByte(); t = 1629151378
                    buf[46] = (t ushr 14).toByte(); t = -1728037083
                    buf[47] = (t ushr 19).toByte(); t = -156256418
                    buf[48] = (t ushr 7).toByte(); t = 1539347461
                    buf[49] = (t ushr 22).toByte(); t = 1258959087
                    buf[50] = (t ushr 1).toByte()
                    return String(buf)
                }
            }.toString(), Toast.LENGTH_SHORT).show()

            val dialog2: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            dialog2.setCancelable(false)
            dialog2.setTitle(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(22); t = -1039487433
                    buf[0] = (t ushr 19).toByte(); t = 1987232983
                    buf[1] = (t ushr 9).toByte(); t = -1599784983
                    buf[2] = (t ushr 7).toByte(); t = -1944079752
                    buf[3] = (t ushr 7).toByte(); t = 1544413206
                    buf[4] = (t ushr 10).toByte(); t = 1090920322
                    buf[5] = (t ushr 19).toByte(); t = -393380774
                    buf[6] = (t ushr 17).toByte(); t = -1044466536
                    buf[7] = (t ushr 18).toByte(); t = -174239050
                    buf[8] = (t ushr 14).toByte(); t = 1304841118
                    buf[9] = (t ushr 21).toByte(); t = 748662746
                    buf[10] = (t ushr 21).toByte(); t = -702775008
                    buf[11] = (t ushr 10).toByte(); t = 1550728358
                    buf[12] = (t ushr 7).toByte(); t = -1210649359
                    buf[13] = (t ushr 12).toByte(); t = -584141330
                    buf[14] = (t ushr 13).toByte(); t = -676490109
                    buf[15] = (t ushr 2).toByte(); t = -419797635
                    buf[16] = (t ushr 11).toByte(); t = 163497958
                    buf[17] = (t ushr 18).toByte(); t = -1875927844
                    buf[18] = (t ushr 6).toByte(); t = 771712490
                    buf[19] = (t ushr 8).toByte(); t = -234122156
                    buf[20] = (t ushr 11).toByte(); t = -1866717174
                    buf[21] = (t ushr 23).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setMessage(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(30); t = -1060815855
                    buf[0] = (t ushr 12).toByte(); t = 531530988
                    buf[1] = (t ushr 13).toByte(); t = 2091321567
                    buf[2] = (t ushr 1).toByte(); t = -74573668
                    buf[3] = (t ushr 13).toByte(); t = -1635676057
                    buf[4] = (t ushr 7).toByte(); t = -415240460
                    buf[5] = (t ushr 20).toByte(); t = 1947093291
                    buf[6] = (t ushr 24).toByte(); t = -624567987
                    buf[7] = (t ushr 6).toByte(); t = -757166715
                    buf[8] = (t ushr 2).toByte(); t = 1116240737
                    buf[9] = (t ushr 3).toByte(); t = -464706313
                    buf[10] = (t ushr 13).toByte(); t = 1714421980
                    buf[11] = (t ushr 1).toByte(); t = 284085775
                    buf[12] = (t ushr 9).toByte(); t = -1069018905
                    buf[13] = (t ushr 14).toByte(); t = -1186885112
                    buf[14] = (t ushr 16).toByte(); t = 985046858
                    buf[15] = (t ushr 15).toByte(); t = -895414991
                    buf[16] = (t ushr 5).toByte(); t = 1852888734
                    buf[17] = (t ushr 24).toByte(); t = 1876937228
                    buf[18] = (t ushr 24).toByte(); t = 1944629316
                    buf[19] = (t ushr 9).toByte(); t = 544982967
                    buf[20] = (t ushr 24).toByte(); t = 675736737
                    buf[21] = (t ushr 16).toByte(); t = -208321519
                    buf[22] = (t ushr 19).toByte(); t = -54214945
                    buf[23] = (t ushr 1).toByte(); t = -85086986
                    buf[24] = (t ushr 13).toByte(); t = 974153342
                    buf[25] = (t ushr 15).toByte(); t = -1766122196
                    buf[26] = (t ushr 2).toByte(); t = 858644801
                    buf[27] = (t ushr 13).toByte(); t = -684738138
                    buf[28] = (t ushr 2).toByte(); t = -905820152
                    buf[29] = (t ushr 6).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setPositiveButton(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(4); t = -1062907345
                    buf[0] = (t ushr 3).toByte(); t = 1306769825
                    buf[1] = (t ushr 18).toByte(); t = 882908936
                    buf[2] = (t ushr 23).toByte(); t = 1970372841
                    buf[3] = (t ushr 1).toByte()
                    return String(buf)
                }
            }.toString()) { _, _ -> finish() }
            dialog2.create().show()
            WebHook(applicationContext).sendSetupError("Credits were changed", true)
        } else {  }

        if (strNameAbodx != object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(5); t = -1233516036
                    buf[0] = (t ushr 11).toByte(); t = 1527981530
                    buf[1] = (t ushr 19).toByte(); t = -1392134270
                    buf[2] = (t ushr 7).toByte(); t = -188141054
                    buf[3] = (t ushr 7).toByte(); t = -1475593090
                    buf[4] = (t ushr 7).toByte()
                    return String(buf)
                }
            }.toString()) {
            Toast.makeText(applicationContext, object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(43); t = -1299730474
                    buf[0] = (t ushr 19).toByte(); t = 961847279
                    buf[1] = (t ushr 23).toByte(); t = -1996001418
                    buf[2] = (t ushr 8).toByte(); t = 1682868391
                    buf[3] = (t ushr 13).toByte(); t = -360698705
                    buf[4] = (t ushr 5).toByte(); t = 372976441
                    buf[5] = (t ushr 20).toByte(); t = -1074671977
                    buf[6] = (t ushr 5).toByte(); t = -1378571080
                    buf[7] = (t ushr 5).toByte(); t = 1526590604
                    buf[8] = (t ushr 5).toByte(); t = -40870907
                    buf[9] = (t ushr 15).toByte(); t = -1038570412
                    buf[10] = (t ushr 5).toByte(); t = 1703288563
                    buf[11] = (t ushr 1).toByte(); t = 1824133221
                    buf[12] = (t ushr 7).toByte(); t = -580483836
                    buf[13] = (t ushr 2).toByte(); t = 1835141063
                    buf[14] = (t ushr 16).toByte(); t = 1844364278
                    buf[15] = (t ushr 21).toByte(); t = 1147406359
                    buf[16] = (t ushr 16).toByte(); t = 2015857210
                    buf[17] = (t ushr 12).toByte(); t = 542256173
                    buf[18] = (t ushr 8).toByte(); t = -1878977964
                    buf[19] = (t ushr 23).toByte(); t = -912886591
                    buf[20] = (t ushr 14).toByte(); t = 1508040442
                    buf[21] = (t ushr 9).toByte(); t = 880108810
                    buf[22] = (t ushr 16).toByte(); t = 542460111
                    buf[23] = (t ushr 24).toByte(); t = -84796601
                    buf[24] = (t ushr 3).toByte(); t = 821977319
                    buf[25] = (t ushr 23).toByte(); t = 14528450
                    buf[26] = (t ushr 14).toByte(); t = -511228214
                    buf[27] = (t ushr 1).toByte(); t = 550273394
                    buf[28] = (t ushr 24).toByte(); t = -93989288
                    buf[29] = (t ushr 4).toByte(); t = 1603515169
                    buf[30] = (t ushr 3).toByte(); t = -748067859
                    buf[31] = (t ushr 16).toByte(); t = 688629166
                    buf[32] = (t ushr 11).toByte(); t = -1791283546
                    buf[33] = (t ushr 11).toByte(); t = 1776792084
                    buf[34] = (t ushr 7).toByte(); t = -830176989
                    buf[35] = (t ushr 10).toByte(); t = -2140953269
                    buf[36] = (t ushr 11).toByte(); t = 393370157
                    buf[37] = (t ushr 6).toByte(); t = -1294797906
                    buf[38] = (t ushr 23).toByte(); t = -510648166
                    buf[39] = (t ushr 8).toByte(); t = 1640895005
                    buf[40] = (t ushr 24).toByte(); t = 297851471
                    buf[41] = (t ushr 18).toByte(); t = 1288888356
                    buf[42] = (t ushr 9).toByte()
                    return String(buf)
                }
            }.toString(), Toast.LENGTH_SHORT).show()
            val dialog2 = AlertDialog.Builder(this)
            dialog2.setCancelable(false)
            dialog2.setTitle(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(22); t = -1257867368
                    buf[0] = (t ushr 18).toByte(); t = 1262586209
                    buf[1] = (t ushr 10).toByte(); t = -1679611811
                    buf[2] = (t ushr 22).toByte(); t = 1443105924
                    buf[3] = (t ushr 5).toByte(); t = -1658978120
                    buf[4] = (t ushr 14).toByte(); t = -9682555
                    buf[5] = (t ushr 9).toByte(); t = 665123098
                    buf[6] = (t ushr 2).toByte(); t = 64037756
                    buf[7] = (t ushr 3).toByte(); t = -555048532
                    buf[8] = (t ushr 17).toByte(); t = -747536789
                    buf[9] = (t ushr 19).toByte(); t = 1204061897
                    buf[10] = (t ushr 1).toByte(); t = 273484910
                    buf[11] = (t ushr 23).toByte(); t = -512891689
                    buf[12] = (t ushr 10).toByte(); t = -1095523464
                    buf[13] = (t ushr 3).toByte(); t = 2096199138
                    buf[14] = (t ushr 8).toByte(); t = 995165696
                    buf[15] = (t ushr 11).toByte(); t = -794418830
                    buf[16] = (t ushr 15).toByte(); t = 594862052
                    buf[17] = (t ushr 9).toByte(); t = -342432125
                    buf[18] = (t ushr 9).toByte(); t = 1721420750
                    buf[19] = (t ushr 9).toByte(); t = -669281783
                    buf[20] = (t ushr 11).toByte(); t = 502234691
                    buf[21] = (t ushr 1).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setMessage(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(34); t = -1247958318
                    buf[0] = (t ushr 10).toByte(); t = 1970738753
                    buf[1] = (t ushr 24).toByte(); t = 1488564592
                    buf[2] = (t ushr 15).toByte(); t = -1362192562
                    buf[3] = (t ushr 4).toByte(); t = -1183513569
                    buf[4] = (t ushr 5).toByte(); t = 60749402
                    buf[5] = (t ushr 19).toByte(); t = -470572900
                    buf[6] = (t ushr 11).toByte(); t = -515478036
                    buf[7] = (t ushr 5).toByte(); t = 1886415269
                    buf[8] = (t ushr 24).toByte(); t = -753518461
                    buf[9] = (t ushr 2).toByte(); t = -1369307559
                    buf[10] = (t ushr 21).toByte(); t = 607351837
                    buf[11] = (t ushr 8).toByte(); t = 1386990998
                    buf[12] = (t ushr 2).toByte(); t = 836080835
                    buf[13] = (t ushr 1).toByte(); t = 919360954
                    buf[14] = (t ushr 20).toByte(); t = -1506480653
                    buf[15] = (t ushr 15).toByte(); t = 2071213846
                    buf[16] = (t ushr 7).toByte(); t = -1145846832
                    buf[17] = (t ushr 15).toByte(); t = -762830212
                    buf[18] = (t ushr 14).toByte(); t = 2101443398
                    buf[19] = (t ushr 16).toByte(); t = -185151245
                    buf[20] = (t ushr 12).toByte(); t = 303716138
                    buf[21] = (t ushr 14).toByte(); t = -1684240005
                    buf[22] = (t ushr 22).toByte(); t = -993890446
                    buf[23] = (t ushr 8).toByte(); t = 205106291
                    buf[24] = (t ushr 7).toByte(); t = 1887545916
                    buf[25] = (t ushr 18).toByte(); t = 862738746
                    buf[26] = (t ushr 23).toByte(); t = -1189853164
                    buf[27] = (t ushr 23).toByte(); t = -18943207
                    buf[28] = (t ushr 17).toByte(); t = -635741066
                    buf[29] = (t ushr 14).toByte(); t = 706781792
                    buf[30] = (t ushr 16).toByte(); t = 1372539793
                    buf[31] = (t ushr 8).toByte(); t = -58151442
                    buf[32] = (t ushr 5).toByte(); t = 1184685646
                    buf[33] = (t ushr 20).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setPositiveButton(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(4); t = -1256751055
                    buf[0] = (t ushr 18).toByte(); t = -1501892341
                    buf[1] = (t ushr 9).toByte(); t = 853226639
                    buf[2] = (t ushr 7).toByte(); t = -1742725838
                    buf[3] = (t ushr 6).toByte()
                    return String(buf)
                }
            }.toString()) { _, _ -> finish() }
            dialog2.create().show()
            WebHook(applicationContext).sendSetupError("Abodx credits were changed", true)
        } else { }

        if (strNameAppName != object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(6); t = 715432086
                    buf[0] = (t ushr 7).toByte(); t = 1836646125
                    buf[1] = (t ushr 24).toByte(); t = 237348054
                    buf[2] = (t ushr 10).toByte(); t = -1354794121
                    buf[3] = (t ushr 3).toByte(); t = 1742347201
                    buf[4] = (t ushr 6).toByte(); t = -1948597618
                    buf[5] = (t ushr 11).toByte()
                    return String(buf)
                }
            }.toString()) {
            Toast.makeText(applicationContext, object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(43); t = 715672669
                    buf[0] = (t ushr 15).toByte(); t = 1480824333
                    buf[1] = (t ushr 11).toByte(); t = 1490977732
                    buf[2] = (t ushr 17).toByte(); t = 1464137798
                    buf[3] = (t ushr 20).toByte(); t = 1268343482
                    buf[4] = (t ushr 14).toByte(); t = 1662265784
                    buf[5] = (t ushr 24).toByte(); t = -430486694
                    buf[6] = (t ushr 12).toByte(); t = -1154796362
                    buf[7] = (t ushr 19).toByte(); t = 171056494
                    buf[8] = (t ushr 15).toByte(); t = -517978065
                    buf[9] = (t ushr 16).toByte(); t = 640079705
                    buf[10] = (t ushr 20).toByte(); t = -293169264
                    buf[11] = (t ushr 4).toByte(); t = -2050006777
                    buf[12] = (t ushr 3).toByte(); t = -122387796
                    buf[13] = (t ushr 9).toByte(); t = -30321086
                    buf[14] = (t ushr 15).toByte(); t = -689958017
                    buf[15] = (t ushr 3).toByte(); t = 1897472119
                    buf[16] = (t ushr 14).toByte(); t = 1373717168
                    buf[17] = (t ushr 18).toByte(); t = 1786653816
                    buf[18] = (t ushr 8).toByte(); t = 36635670
                    buf[19] = (t ushr 5).toByte(); t = -1772885068
                    buf[20] = (t ushr 22).toByte(); t = 1510029309
                    buf[21] = (t ushr 7).toByte(); t = -948624700
                    buf[22] = (t ushr 16).toByte(); t = 708924393
                    buf[23] = (t ushr 17).toByte(); t = 1669440024
                    buf[24] = (t ushr 10).toByte(); t = 2051415451
                    buf[25] = (t ushr 12).toByte(); t = -883465518
                    buf[26] = (t ushr 12).toByte(); t = -1242772942
                    buf[27] = (t ushr 9).toByte(); t = 79958513
                    buf[28] = (t ushr 13).toByte(); t = 372416340
                    buf[29] = (t ushr 15).toByte(); t = -2009914446
                    buf[30] = (t ushr 11).toByte(); t = 1983132845
                    buf[31] = (t ushr 7).toByte(); t = 418763058
                    buf[32] = (t ushr 10).toByte(); t = -1295538889
                    buf[33] = (t ushr 23).toByte(); t = 316093015
                    buf[34] = (t ushr 7).toByte(); t = 2083594368
                    buf[35] = (t ushr 2).toByte(); t = -1170991631
                    buf[36] = (t ushr 23).toByte(); t = -456121448
                    buf[37] = (t ushr 17).toByte(); t = -1765092837
                    buf[38] = (t ushr 17).toByte(); t = 548450499
                    buf[39] = (t ushr 24).toByte(); t = -1777019161
                    buf[40] = (t ushr 20).toByte(); t = 1362635278
                    buf[41] = (t ushr 15).toByte(); t = -1675849415
                    buf[42] = (t ushr 22).toByte()
                    return String(buf)
                }
            }.toString(), Toast.LENGTH_SHORT).show()
            val dialog2 = AlertDialog.Builder(this)
            dialog2.setCancelable(false)
            dialog2.setTitle(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(22); t = 713892067
                    buf[0] = (t ushr 7).toByte(); t = -1323955518
                    buf[1] = (t ushr 23).toByte(); t = 1446484207
                    buf[2] = (t ushr 15).toByte(); t = -800234362
                    buf[3] = (t ushr 8).toByte(); t = -473792545
                    buf[4] = (t ushr 19).toByte(); t = 1021956673
                    buf[5] = (t ushr 1).toByte(); t = -217755203
                    buf[6] = (t ushr 6).toByte(); t = 1844596571
                    buf[7] = (t ushr 21).toByte(); t = -2075575669
                    buf[8] = (t ushr 7).toByte(); t = -639851079
                    buf[9] = (t ushr 2).toByte(); t = -26497847
                    buf[10] = (t ushr 1).toByte(); t = 1678467699
                    buf[11] = (t ushr 21).toByte(); t = -1908615991
                    buf[12] = (t ushr 15).toByte(); t = 1520891599
                    buf[13] = (t ushr 12).toByte(); t = -289471514
                    buf[14] = (t ushr 21).toByte(); t = 2068882444
                    buf[15] = (t ushr 5).toByte(); t = 1416271987
                    buf[16] = (t ushr 9).toByte(); t = 151908138
                    buf[17] = (t ushr 13).toByte(); t = 806754023
                    buf[18] = (t ushr 1).toByte(); t = -1921149254
                    buf[19] = (t ushr 10).toByte(); t = -1797479466
                    buf[20] = (t ushr 14).toByte(); t = -1814543764
                    buf[21] = (t ushr 9).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setMessage(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(61); t = 655946172
                    buf[0] = (t ushr 23).toByte(); t = 922191064
                    buf[1] = (t ushr 20).toByte(); t = 854561507
                    buf[2] = (t ushr 17).toByte(); t = 1070087723
                    buf[3] = (t ushr 14).toByte(); t = 615661744
                    buf[4] = (t ushr 17).toByte(); t = -463356132
                    buf[5] = (t ushr 10).toByte(); t = -892421298
                    buf[6] = (t ushr 13).toByte(); t = -2122825647
                    buf[7] = (t ushr 9).toByte(); t = 1635092153
                    buf[8] = (t ushr 10).toByte(); t = -604081472
                    buf[9] = (t ushr 8).toByte(); t = -1076362550
                    buf[10] = (t ushr 1).toByte(); t = 105123497
                    buf[11] = (t ushr 13).toByte(); t = 1122602802
                    buf[12] = (t ushr 17).toByte(); t = 1667019461
                    buf[13] = (t ushr 14).toByte(); t = 2030911488
                    buf[14] = (t ushr 24).toByte(); t = -576308545
                    buf[15] = (t ushr 18).toByte(); t = 940410599
                    buf[16] = (t ushr 4).toByte(); t = -80208413
                    buf[17] = (t ushr 19).toByte(); t = 1090930114
                    buf[18] = (t ushr 19).toByte(); t = -748443883
                    buf[19] = (t ushr 11).toByte(); t = 742157758
                    buf[20] = (t ushr 2).toByte(); t = -251023101
                    buf[21] = (t ushr 3).toByte(); t = 318971730
                    buf[22] = (t ushr 11).toByte(); t = 512555846
                    buf[23] = (t ushr 3).toByte(); t = 94974177
                    buf[24] = (t ushr 7).toByte(); t = -76204347
                    buf[25] = (t ushr 19).toByte(); t = -1849477119
                    buf[26] = (t ushr 11).toByte(); t = 1708017219
                    buf[27] = (t ushr 24).toByte(); t = 538075174
                    buf[28] = (t ushr 24).toByte(); t = -1308907781
                    buf[29] = (t ushr 11).toByte(); t = -1048008286
                    buf[30] = (t ushr 2).toByte(); t = -972864727
                    buf[31] = (t ushr 3).toByte(); t = -1822129722
                    buf[32] = (t ushr 10).toByte(); t = -1686643412
                    buf[33] = (t ushr 19).toByte(); t = -1507184484
                    buf[34] = (t ushr 7).toByte(); t = 707300777
                    buf[35] = (t ushr 5).toByte(); t = 145830547
                    buf[36] = (t ushr 7).toByte(); t = 1646462994
                    buf[37] = (t ushr 6).toByte(); t = -578198941
                    buf[38] = (t ushr 3).toByte(); t = -1992385665
                    buf[39] = (t ushr 3).toByte(); t = 1277711730
                    buf[40] = (t ushr 24).toByte(); t = 659702044
                    buf[41] = (t ushr 9).toByte(); t = -86645217
                    buf[42] = (t ushr 10).toByte(); t = -1052018564
                    buf[43] = (t ushr 11).toByte(); t = 2089003102
                    buf[44] = (t ushr 11).toByte(); t = 1893212685
                    buf[45] = (t ushr 4).toByte(); t = 610543811
                    buf[46] = (t ushr 1).toByte(); t = 1999434156
                    buf[47] = (t ushr 20).toByte(); t = -1854580381
                    buf[48] = (t ushr 6).toByte(); t = 737153115
                    buf[49] = (t ushr 7).toByte(); t = -80297129
                    buf[50] = (t ushr 9).toByte(); t = 1614843249
                    buf[51] = (t ushr 17).toByte(); t = 1793651875
                    buf[52] = (t ushr 17).toByte(); t = -1642461839
                    buf[53] = (t ushr 14).toByte(); t = -1058691809
                    buf[54] = (t ushr 10).toByte(); t = -1130800290
                    buf[55] = (t ushr 14).toByte(); t = 409349077
                    buf[56] = (t ushr 16).toByte(); t = -1499929587
                    buf[57] = (t ushr 5).toByte(); t = 1835529811
                    buf[58] = (t ushr 24).toByte(); t = 646014146
                    buf[59] = (t ushr 1).toByte(); t = 660315009
                    buf[60] = (t ushr 6).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setPositiveButton(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(4); t = 585374568
                    buf[0] = (t ushr 23).toByte(); t = -1209477241
                    buf[1] = (t ushr 4).toByte(); t = 1410149915
                    buf[2] = (t ushr 13).toByte(); t = 1244006215
                    buf[3] = (t ushr 4).toByte()
                    return String(buf)
                }
            }.toString()) { _, _ -> finish() }
            dialog2.create().show()
            WebHook(applicationContext).sendSetupError("App name was changed", true)
        } else { }

        if (strNameAppDev != object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(19); t = 236286225
                    buf[0] = (t ushr 2).toByte(); t = -103635765
                    buf[1] = (t ushr 1).toByte(); t = -1982193753
                    buf[2] = (t ushr 18).toByte(); t = -591838826
                    buf[3] = (t ushr 2).toByte(); t = -1258384542
                    buf[4] = (t ushr 3).toByte(); t = -1726890169
                    buf[5] = (t ushr 10).toByte(); t = -301766643
                    buf[6] = (t ushr 21).toByte(); t = -384723587
                    buf[7] = (t ushr 10).toByte(); t = 1812698292
                    buf[8] = (t ushr 11).toByte(); t = 203571636
                    buf[9] = (t ushr 9).toByte(); t = -2115798087
                    buf[10] = (t ushr 11).toByte(); t = -1350552820
                    buf[11] = (t ushr 7).toByte(); t = -2059166655
                    buf[12] = (t ushr 1).toByte(); t = 751771865
                    buf[13] = (t ushr 7).toByte(); t = 1834533476
                    buf[14] = (t ushr 24).toByte(); t = 887929990
                    buf[15] = (t ushr 23).toByte(); t = 1537116950
                    buf[16] = (t ushr 22).toByte(); t = -1128288778
                    buf[17] = (t ushr 5).toByte(); t = -1218787448
                    buf[18] = (t ushr 11).toByte()
                    return String(buf)
                }
            }.toString()) {
            Toast.makeText(applicationContext, object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(43); t = 234747201
                    buf[0] = (t ushr 2).toByte(); t = 1919922574
                    buf[1] = (t ushr 24).toByte(); t = -1210438720
                    buf[2] = (t ushr 23).toByte(); t = -35845963
                    buf[3] = (t ushr 14).toByte(); t = 806839776
                    buf[4] = (t ushr 8).toByte(); t = 1248033892
                    buf[5] = (t ushr 16).toByte(); t = 1021959466
                    buf[6] = (t ushr 6).toByte(); t = -1157467754
                    buf[7] = (t ushr 2).toByte(); t = -1314500778
                    buf[8] = (t ushr 12).toByte(); t = 130292366
                    buf[9] = (t ushr 13).toByte(); t = -15890912
                    buf[10] = (t ushr 4).toByte(); t = -1887880672
                    buf[11] = (t ushr 16).toByte(); t = 474220646
                    buf[12] = (t ushr 13).toByte(); t = 463931437
                    buf[13] = (t ushr 5).toByte(); t = -242726352
                    buf[14] = (t ushr 18).toByte(); t = 1307682591
                    buf[15] = (t ushr 21).toByte(); t = -2032543341
                    buf[16] = (t ushr 2).toByte(); t = -588151739
                    buf[17] = (t ushr 17).toByte(); t = -1770042839
                    buf[18] = (t ushr 23).toByte(); t = 1070745655
                    buf[19] = (t ushr 6).toByte(); t = 724415381
                    buf[20] = (t ushr 21).toByte(); t = 700045053
                    buf[21] = (t ushr 4).toByte(); t = 1431688226
                    buf[22] = (t ushr 10).toByte(); t = 740684032
                    buf[23] = (t ushr 3).toByte(); t = 1451272766
                    buf[24] = (t ushr 20).toByte(); t = -1386903710
                    buf[25] = (t ushr 10).toByte(); t = 919318612
                    buf[26] = (t ushr 11).toByte(); t = 425175930
                    buf[27] = (t ushr 22).toByte(); t = 2115465733
                    buf[28] = (t ushr 4).toByte(); t = 375365795
                    buf[29] = (t ushr 20).toByte(); t = 1177712890
                    buf[30] = (t ushr 15).toByte(); t = 1525893159
                    buf[31] = (t ushr 11).toByte(); t = -1168726134
                    buf[32] = (t ushr 23).toByte(); t = -1162048822
                    buf[33] = (t ushr 1).toByte(); t = -269277480
                    buf[34] = (t ushr 11).toByte(); t = 551854892
                    buf[35] = (t ushr 24).toByte(); t = 588155599
                    buf[36] = (t ushr 13).toByte(); t = -1188333950
                    buf[37] = (t ushr 4).toByte(); t = -2056119951
                    buf[38] = (t ushr 6).toByte(); t = -459144284
                    buf[39] = (t ushr 12).toByte(); t = -485827235
                    buf[40] = (t ushr 19).toByte(); t = 1975743443
                    buf[41] = (t ushr 18).toByte(); t = -757059376
                    buf[42] = (t ushr 17).toByte()
                    return String(buf)
                }
            }.toString(), Toast.LENGTH_SHORT).show()
            val dialog2 = AlertDialog.Builder(this)
            dialog2.setCancelable(false)
            dialog2.setTitle(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(22); t = 243404466
                    buf[0] = (t ushr 11).toByte(); t = -1054304123
                    buf[1] = (t ushr 6).toByte(); t = -1678711248
                    buf[2] = (t ushr 22).toByte(); t = -1305039464
                    buf[3] = (t ushr 23).toByte(); t = -1404405694
                    buf[4] = (t ushr 8).toByte(); t = -803716066
                    buf[5] = (t ushr 5).toByte(); t = -778239535
                    buf[6] = (t ushr 22).toByte(); t = -1016119143
                    buf[7] = (t ushr 16).toByte(); t = 557479216
                    buf[8] = (t ushr 8).toByte(); t = 768078374
                    buf[9] = (t ushr 21).toByte(); t = 795564833
                    buf[10] = (t ushr 3).toByte(); t = -2113874291
                    buf[11] = (t ushr 20).toByte(); t = -1207268408
                    buf[12] = (t ushr 3).toByte(); t = -75651461
                    buf[13] = (t ushr 19).toByte(); t = -826278563
                    buf[14] = (t ushr 21).toByte(); t = -707960571
                    buf[15] = (t ushr 3).toByte(); t = -1062939343
                    buf[16] = (t ushr 2).toByte(); t = 1863003535
                    buf[17] = (t ushr 24).toByte(); t = 971596553
                    buf[18] = (t ushr 23).toByte(); t = -1294771835
                    buf[19] = (t ushr 23).toByte(); t = -153921820
                    buf[20] = (t ushr 1).toByte(); t = -652136825
                    buf[21] = (t ushr 16).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setMessage(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(34); t = 240903592
                    buf[0] = (t ushr 2).toByte(); t = -251942342
                    buf[1] = (t ushr 11).toByte(); t = -1220936868
                    buf[2] = (t ushr 20).toByte(); t = 2039195096
                    buf[3] = (t ushr 11).toByte(); t = 1135936519
                    buf[4] = (t ushr 5).toByte(); t = -2048017936
                    buf[5] = (t ushr 7).toByte(); t = -78629655
                    buf[6] = (t ushr 1).toByte(); t = 1908366142
                    buf[7] = (t ushr 18).toByte(); t = -1687132980
                    buf[8] = (t ushr 16).toByte(); t = 418385956
                    buf[9] = (t ushr 7).toByte(); t = 175321701
                    buf[10] = (t ushr 16).toByte(); t = 267295696
                    buf[11] = (t ushr 13).toByte(); t = -1069043029
                    buf[12] = (t ushr 7).toByte(); t = 1481079966
                    buf[13] = (t ushr 22).toByte(); t = -709743871
                    buf[14] = (t ushr 18).toByte(); t = 2050878778
                    buf[15] = (t ushr 5).toByte(); t = -1544692086
                    buf[16] = (t ushr 13).toByte(); t = 1503656457
                    buf[17] = (t ushr 18).toByte(); t = 455131043
                    buf[18] = (t ushr 16).toByte(); t = 1864430617
                    buf[19] = (t ushr 4).toByte(); t = -441184588
                    buf[20] = (t ushr 18).toByte(); t = 296222326
                    buf[21] = (t ushr 18).toByte(); t = 827778856
                    buf[22] = (t ushr 12).toByte(); t = -1914658705
                    buf[23] = (t ushr 21).toByte(); t = -2054520903
                    buf[24] = (t ushr 20).toByte(); t = -1005304103
                    buf[25] = (t ushr 21).toByte(); t = -429888784
                    buf[26] = (t ushr 20).toByte(); t = -1302549693
                    buf[27] = (t ushr 14).toByte(); t = 937958380
                    buf[28] = (t ushr 23).toByte(); t = 457535115
                    buf[29] = (t ushr 22).toByte(); t = 542225249
                    buf[30] = (t ushr 24).toByte(); t = -756443265
                    buf[31] = (t ushr 22).toByte(); t = 255301859
                    buf[32] = (t ushr 15).toByte(); t = 1908101062
                    buf[33] = (t ushr 11).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setPositiveButton(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(4); t = 219530533
                    buf[0] = (t ushr 18).toByte(); t = -980140303
                    buf[1] = (t ushr 1).toByte(); t = 552260181
                    buf[2] = (t ushr 9).toByte(); t = -1320444629
                    buf[3] = (t ushr 6).toByte()
                    return String(buf)
                }
            }.toString()) { _, _ -> finish() }
            dialog2.create().show()
            WebHook(applicationContext).sendSetupError("App developer was changed", true)
        } else { }

        if (strNameProtectionDev != object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(26); t = -197947074
                    buf[0] = (t ushr 6).toByte(); t = 875769744
                    buf[1] = (t ushr 11).toByte(); t = -651593877
                    buf[2] = (t ushr 4).toByte(); t = -698464792
                    buf[3] = (t ushr 20).toByte(); t = 215524932
                    buf[4] = (t ushr 17).toByte(); t = -1393193488
                    buf[5] = (t ushr 5).toByte(); t = 729928102
                    buf[6] = (t ushr 19).toByte(); t = -2078939490
                    buf[7] = (t ushr 4).toByte(); t = 1265815969
                    buf[8] = (t ushr 19).toByte(); t = 1626137605
                    buf[9] = (t ushr 13).toByte(); t = 995347227
                    buf[10] = (t ushr 6).toByte(); t = 924884055
                    buf[11] = (t ushr 16).toByte(); t = -1594661132
                    buf[12] = (t ushr 23).toByte(); t = -445681654
                    buf[13] = (t ushr 8).toByte(); t = -1527544726
                    buf[14] = (t ushr 11).toByte(); t = -839015393
                    buf[15] = (t ushr 5).toByte(); t = 903127487
                    buf[16] = (t ushr 9).toByte(); t = 1031256447
                    buf[17] = (t ushr 7).toByte(); t = 1458736793
                    buf[18] = (t ushr 20).toByte(); t = -667263482
                    buf[19] = (t ushr 15).toByte(); t = -2112264280
                    buf[20] = (t ushr 14).toByte(); t = 1067834141
                    buf[21] = (t ushr 3).toByte(); t = 1595646221
                    buf[22] = (t ushr 11).toByte(); t = 928672387
                    buf[23] = (t ushr 14).toByte(); t = -2044988166
                    buf[24] = (t ushr 14).toByte(); t = -326498887
                    buf[25] = (t ushr 2).toByte()
                    return String(buf)
                }
            }.toString()) {
            Toast.makeText(applicationContext, object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(43); t = -737505148
                    buf[0] = (t ushr 22).toByte(); t = 1681484868
                    buf[1] = (t ushr 15).toByte(); t = -685603393
                    buf[2] = (t ushr 2).toByte(); t = 1294365736
                    buf[3] = (t ushr 8).toByte(); t = -1553161367
                    buf[4] = (t ushr 13).toByte(); t = 1657483715
                    buf[5] = (t ushr 7).toByte(); t = 962397385
                    buf[6] = (t ushr 14).toByte(); t = 863833251
                    buf[7] = (t ushr 5).toByte(); t = -957144003
                    buf[8] = (t ushr 11).toByte(); t = 276209924
                    buf[9] = (t ushr 23).toByte(); t = 371765431
                    buf[10] = (t ushr 20).toByte(); t = -1093377806
                    buf[11] = (t ushr 1).toByte(); t = -676197754
                    buf[12] = (t ushr 12).toByte(); t = 1197742893
                    buf[13] = (t ushr 12).toByte(); t = -1593785329
                    buf[14] = (t ushr 9).toByte(); t = 1919825835
                    buf[15] = (t ushr 7).toByte(); t = 421293212
                    buf[16] = (t ushr 5).toByte(); t = -166157583
                    buf[17] = (t ushr 1).toByte(); t = 480611123
                    buf[18] = (t ushr 13).toByte(); t = 1665271498
                    buf[19] = (t ushr 12).toByte(); t = 1882707531
                    buf[20] = (t ushr 6).toByte(); t = 2038345204
                    buf[21] = (t ushr 5).toByte(); t = 1977240635
                    buf[22] = (t ushr 24).toByte(); t = -1089658752
                    buf[23] = (t ushr 8).toByte(); t = 54765460
                    buf[24] = (t ushr 19).toByte(); t = 197182401
                    buf[25] = (t ushr 9).toByte(); t = -300042896
                    buf[26] = (t ushr 14).toByte(); t = 749898663
                    buf[27] = (t ushr 21).toByte(); t = 135396203
                    buf[28] = (t ushr 22).toByte(); t = 850094356
                    buf[29] = (t ushr 23).toByte(); t = 973808082
                    buf[30] = (t ushr 11).toByte(); t = 441055984
                    buf[31] = (t ushr 22).toByte(); t = -1167885414
                    buf[32] = (t ushr 23).toByte(); t = 106934585
                    buf[33] = (t ushr 20).toByte(); t = 843125139
                    buf[34] = (t ushr 23).toByte(); t = 1924428353
                    buf[35] = (t ushr 1).toByte(); t = 972621645
                    buf[36] = (t ushr 4).toByte(); t = -1277550133
                    buf[37] = (t ushr 14).toByte(); t = 57461546
                    buf[38] = (t ushr 3).toByte(); t = 668009479
                    buf[39] = (t ushr 5).toByte(); t = 1627681509
                    buf[40] = (t ushr 24).toByte(); t = 1420690557
                    buf[41] = (t ushr 13).toByte(); t = 263307521
                    buf[42] = (t ushr 10).toByte()
                    return String(buf)
                }
            }.toString(), Toast.LENGTH_SHORT).show()
            val dialog2 = AlertDialog.Builder(this)
            dialog2.setCancelable(false)
            dialog2.setTitle(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(22); t = -199864226
                    buf[0] = (t ushr 6).toByte(); t = 103507546
                    buf[1] = (t ushr 20).toByte(); t = -1283686134
                    buf[2] = (t ushr 19).toByte(); t = -1130820659
                    buf[3] = (t ushr 14).toByte(); t = -438057926
                    buf[4] = (t ushr 11).toByte(); t = 172298292
                    buf[5] = (t ushr 7).toByte(); t = 146919834
                    buf[6] = (t ushr 21).toByte(); t = 1250173151
                    buf[7] = (t ushr 1).toByte(); t = -1539662100
                    buf[8] = (t ushr 15).toByte(); t = 1536744342
                    buf[9] = (t ushr 6).toByte(); t = -675076653
                    buf[10] = (t ushr 11).toByte(); t = 1323650112
                    buf[11] = (t ushr 1).toByte(); t = -1944169233
                    buf[12] = (t ushr 14).toByte(); t = 1342000611
                    buf[13] = (t ushr 5).toByte(); t = 2106023465
                    buf[14] = (t ushr 12).toByte(); t = -770980258
                    buf[15] = (t ushr 20).toByte(); t = -1505831345
                    buf[16] = (t ushr 23).toByte(); t = -437602383
                    buf[17] = (t ushr 7).toByte(); t = 760887778
                    buf[18] = (t ushr 7).toByte(); t = -1456690395
                    buf[19] = (t ushr 13).toByte(); t = 60271406
                    buf[20] = (t ushr 19).toByte(); t = -693235509
                    buf[21] = (t ushr 7).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setMessage(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(34); t = -201401686
                    buf[0] = (t ushr 6).toByte(); t = -1160009169
                    buf[1] = (t ushr 11).toByte(); t = 1931513123
                    buf[2] = (t ushr 24).toByte(); t = -575811120
                    buf[3] = (t ushr 2).toByte(); t = 1612759704
                    buf[4] = (t ushr 16).toByte(); t = -1663996463
                    buf[5] = (t ushr 22).toByte(); t = 1638407480
                    buf[6] = (t ushr 6).toByte(); t = 484435694
                    buf[7] = (t ushr 17).toByte(); t = 1892034959
                    buf[8] = (t ushr 24).toByte(); t = 1465139313
                    buf[9] = (t ushr 9).toByte(); t = 1463573402
                    buf[10] = (t ushr 20).toByte(); t = 784453988
                    buf[11] = (t ushr 10).toByte(); t = 590356682
                    buf[12] = (t ushr 1).toByte(); t = 1054019383
                    buf[13] = (t ushr 11).toByte(); t = -999072972
                    buf[14] = (t ushr 6).toByte(); t = 228897016
                    buf[15] = (t ushr 18).toByte(); t = 897197496
                    buf[16] = (t ushr 2).toByte(); t = 54432704
                    buf[17] = (t ushr 19).toByte(); t = 151108740
                    buf[18] = (t ushr 19).toByte(); t = 437133936
                    buf[19] = (t ushr 19).toByte(); t = -657869721
                    buf[20] = (t ushr 10).toByte(); t = 474389149
                    buf[21] = (t ushr 12).toByte(); t = -1284400422
                    buf[22] = (t ushr 19).toByte(); t = 1401216712
                    buf[23] = (t ushr 9).toByte(); t = -585539356
                    buf[24] = (t ushr 10).toByte(); t = -1493941806
                    buf[25] = (t ushr 9).toByte(); t = -685752115
                    buf[26] = (t ushr 1).toByte(); t = 1853017006
                    buf[27] = (t ushr 16).toByte(); t = -159917120
                    buf[28] = (t ushr 6).toByte(); t = 1351528154
                    buf[29] = (t ushr 1).toByte(); t = -2076151163
                    buf[30] = (t ushr 17).toByte(); t = 247679749
                    buf[31] = (t ushr 8).toByte(); t = 591625583
                    buf[32] = (t ushr 11).toByte(); t = 201019291
                    buf[33] = (t ushr 11).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setPositiveButton(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(4); t = -781371933
                    buf[0] = (t ushr 22).toByte(); t = 2014198543
                    buf[1] = (t ushr 24).toByte(); t = -1314548170
                    buf[2] = (t ushr 18).toByte(); t = 913855953
                    buf[3] = (t ushr 2).toByte()
                    return String(buf)
                }
            }.toString()) { _, _ -> finish() }
            dialog2.create().show()
            WebHook(applicationContext).sendSetupError("App protection developer was changed", true)
        } else { }

        if (strNameSlimakoi != object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(8); t = 351800430
                    buf[0] = (t ushr 22).toByte(); t = -1430896776
                    buf[1] = (t ushr 15).toByte(); t = -375022980
                    buf[2] = (t ushr 6).toByte(); t = -1687679797
                    buf[3] = (t ushr 22).toByte(); t = 56704213
                    buf[4] = (t ushr 16).toByte(); t = -1126757241
                    buf[5] = (t ushr 17).toByte(); t = 1875538164
                    buf[6] = (t ushr 24).toByte(); t = 1705274382
                    buf[7] = (t ushr 18).toByte()
                    return String(buf)
                }
            }.toString()) {
            Toast.makeText(applicationContext, object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(43); t = 550767176
                    buf[0] = (t ushr 14).toByte(); t = 1409520014
                    buf[1] = (t ushr 11).toByte(); t = 1873262639
                    buf[2] = (t ushr 24).toByte(); t = -140440672
                    buf[3] = (t ushr 3).toByte(); t = 1171881382
                    buf[4] = (t ushr 14).toByte(); t = 1698879884
                    buf[5] = (t ushr 2).toByte(); t = -1885914555
                    buf[6] = (t ushr 7).toByte(); t = 2107087171
                    buf[7] = (t ushr 6).toByte(); t = 1699076899
                    buf[8] = (t ushr 3).toByte(); t = -1332288506
                    buf[9] = (t ushr 5).toByte(); t = 896288446
                    buf[10] = (t ushr 13).toByte(); t = -1275273602
                    buf[11] = (t ushr 6).toByte(); t = 269012386
                    buf[12] = (t ushr 23).toByte(); t = -623573205
                    buf[13] = (t ushr 10).toByte(); t = -1775281362
                    buf[14] = (t ushr 20).toByte(); t = -405115074
                    buf[15] = (t ushr 8).toByte(); t = 1496097292
                    buf[16] = (t ushr 22).toByte(); t = -569787696
                    buf[17] = (t ushr 22).toByte(); t = -2070539200
                    buf[18] = (t ushr 15).toByte(); t = -435154426
                    buf[19] = (t ushr 4).toByte(); t = 187119484
                    buf[20] = (t ushr 21).toByte(); t = -1245951789
                    buf[21] = (t ushr 18).toByte(); t = -1298711887
                    buf[22] = (t ushr 7).toByte(); t = -1371469427
                    buf[23] = (t ushr 17).toByte(); t = -2136571671
                    buf[24] = (t ushr 12).toByte(); t = -1238310653
                    buf[25] = (t ushr 15).toByte(); t = -809364557
                    buf[26] = (t ushr 3).toByte(); t = 258363677
                    buf[27] = (t ushr 12).toByte(); t = -71405041
                    buf[28] = (t ushr 4).toByte(); t = -1009412955
                    buf[29] = (t ushr 10).toByte(); t = -1669506804
                    buf[30] = (t ushr 6).toByte(); t = 1490249206
                    buf[31] = (t ushr 17).toByte(); t = -1593021998
                    buf[32] = (t ushr 2).toByte(); t = -2099223054
                    buf[33] = (t ushr 8).toByte(); t = 1753781828
                    buf[34] = (t ushr 4).toByte(); t = -1423919048
                    buf[35] = (t ushr 16).toByte(); t = -1599270168
                    buf[36] = (t ushr 1).toByte(); t = 852599190
                    buf[37] = (t ushr 10).toByte(); t = 643716667
                    buf[38] = (t ushr 20).toByte(); t = 1178688957
                    buf[39] = (t ushr 17).toByte(); t = -207941927
                    buf[40] = (t ushr 11).toByte(); t = -507435213
                    buf[41] = (t ushr 18).toByte(); t = 434813024
                    buf[42] = (t ushr 7).toByte()
                    return String(buf)
                }
            }.toString(), Toast.LENGTH_SHORT).show()
            val dialog2 = AlertDialog.Builder(this)
            dialog2.setCancelable(false)
            dialog2.setTitle(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(22); t = 548163669
                    buf[0] = (t ushr 6).toByte(); t = 26940771
                    buf[1] = (t ushr 11).toByte(); t = 228837112
                    buf[2] = (t ushr 4).toByte(); t = 409210663
                    buf[3] = (t ushr 16).toByte(); t = -1010533372
                    buf[4] = (t ushr 19).toByte(); t = 374284309
                    buf[5] = (t ushr 8).toByte(); t = -561142749
                    buf[6] = (t ushr 17).toByte(); t = 1334402009
                    buf[7] = (t ushr 6).toByte(); t = -1427679555
                    buf[8] = (t ushr 12).toByte(); t = -1403855392
                    buf[9] = (t ushr 9).toByte(); t = -1298147067
                    buf[10] = (t ushr 6).toByte(); t = -1819276846
                    buf[11] = (t ushr 15).toByte(); t = -1081744175
                    buf[12] = (t ushr 10).toByte(); t = -911460417
                    buf[13] = (t ushr 7).toByte(); t = -591209094
                    buf[14] = (t ushr 6).toByte(); t = 557828119
                    buf[15] = (t ushr 6).toByte(); t = -1197870875
                    buf[16] = (t ushr 17).toByte(); t = -47858337
                    buf[17] = (t ushr 10).toByte(); t = 1664988058
                    buf[18] = (t ushr 3).toByte(); t = 1513985442
                    buf[19] = (t ushr 10).toByte(); t = -383311692
                    buf[20] = (t ushr 12).toByte(); t = -99055515
                    buf[21] = (t ushr 6).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setMessage(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(34); t = 547003022
                    buf[0] = (t ushr 6).toByte(); t = 1976929858
                    buf[1] = (t ushr 24).toByte(); t = -589247141
                    buf[2] = (t ushr 22).toByte(); t = 1903465136
                    buf[3] = (t ushr 16).toByte(); t = -580615477
                    buf[4] = (t ushr 10).toByte(); t = 462980587
                    buf[5] = (t ushr 19).toByte(); t = -1672842799
                    buf[6] = (t ushr 2).toByte(); t = 754872232
                    buf[7] = (t ushr 8).toByte(); t = -611930441
                    buf[8] = (t ushr 19).toByte(); t = -1458549244
                    buf[9] = (t ushr 15).toByte(); t = 276650906
                    buf[10] = (t ushr 3).toByte(); t = 1958848385
                    buf[11] = (t ushr 24).toByte(); t = 818299692
                    buf[12] = (t ushr 3).toByte(); t = 1020807502
                    buf[13] = (t ushr 14).toByte(); t = -1864791854
                    buf[14] = (t ushr 17).toByte(); t = -1941222966
                    buf[15] = (t ushr 11).toByte(); t = 1664408585
                    buf[16] = (t ushr 9).toByte(); t = -1788377269
                    buf[17] = (t ushr 16).toByte(); t = 1244995803
                    buf[18] = (t ushr 8).toByte(); t = 1901888282
                    buf[19] = (t ushr 9).toByte(); t = 99463070
                    buf[20] = (t ushr 13).toByte(); t = -426698558
                    buf[21] = (t ushr 20).toByte(); t = -2031829537
                    buf[22] = (t ushr 20).toByte(); t = 930545034
                    buf[23] = (t ushr 12).toByte(); t = -44555389
                    buf[24] = (t ushr 16).toByte(); t = -938183617
                    buf[25] = (t ushr 22).toByte(); t = 2133552335
                    buf[26] = (t ushr 5).toByte(); t = -1818248906
                    buf[27] = (t ushr 7).toByte(); t = 937637609
                    buf[28] = (t ushr 23).toByte(); t = -1640927856
                    buf[29] = (t ushr 8).toByte(); t = 535855615
                    buf[30] = (t ushr 10).toByte(); t = -1516674881
                    buf[31] = (t ushr 23).toByte(); t = -1210318553
                    buf[32] = (t ushr 23).toByte(); t = -1555485879
                    buf[33] = (t ushr 19).toByte()
                    return String(buf)
                }
            }.toString())
            dialog2.setPositiveButton(object : Any() {
                var t = 0
                override fun toString(): String {
                    val buf = ByteArray(4); t = 545460570
                    buf[0] = (t ushr 6).toByte(); t = 826022423
                    buf[1] = (t ushr 15).toByte(); t = 194829132
                    buf[2] = (t ushr 3).toByte(); t = -1836607673
                    buf[3] = (t ushr 4).toByte()
                    return String(buf)
                }
            }.toString()) { _, _ -> finish() }
            dialog2.create().show()
            WebHook(applicationContext).sendSetupError("Slimakoi credits were changed", true)
        } else { }

        if (applicationContext.packageName != "com.slimakoi.aminox") {
            val dialog2 = AlertDialog.Builder(this)
            dialog2.setCancelable(false)
            dialog2.setTitle("Uh oh!")
            dialog2.setMessage("The package name was changed! Please install the original AminoX from my discord sever!")
            dialog2.create().show()
            WebHook(applicationContext).sendSetupError("App package name was changed", true)
        }

        result.setTypeface(null, Typeface.BOLD)
        credits.setOnClickListener { val i = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://linktr.ee/Slimakoi")
        ); startActivity(i) }

        val announce = AlertDialog.Builder(this)
        if (announcementHasData) {
            announce.setTitle(announcementTitle)
            announce.setMessage(announcementText)
            announce.setCancelable(announcementCancelable)
            announce.show()
        }

        try {
            Setup().execute(this)
            Log.println(Log.INFO, "SYSTEM-INFO", "Imported setup data successfully")
        } catch (e: Exception) {
            Log.println(Log.ERROR, "SYSTEM-ERROR", "Couldn't import setup data -- $e")
            WebHook(this).sendSetupError("Couldn't import setup data -- $e", true)
            setupErrorTrigger(this, "$e\n\n[$AND_ID:$DEV_SIG]")
        }

        val updateAlert = AlertDialog.Builder(this)
        val currentPackageInfo = packageManager.getPackageInfo(packageName, 0)
        val testAppName: Boolean = applicationLatestName == currentPackageInfo.versionName
        val testAppCode: Boolean = applicationLatestCode == currentPackageInfo.versionCode.toString()

        if (testAppName and testAppCode) { } else {
            updateAlert.setTitle("Update Available!")
            updateAlert.setIcon(R.drawable.upgrade)
            updateAlert.setCancelable(false)
            updateAlert.setMessage("A new version of AminoX is available (v$applicationLatestName-$applicationLatestCode)\n\nCurrently using version ${currentPackageInfo.versionName}-${currentPackageInfo.versionCode}")
            updateAlert.setPositiveButton("Download") { _, _ -> startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        applicationUrl
                    )
                )
            ) }
            updateAlert.setNegativeButton("Ignore") { dialog, _ -> dialog.dismiss() }
            updateAlert.show()
        }

        buttonLogin.setOnClickListener{
            loading.visibility = View.VISIBLE
            result.text = null

            if (loggingWithSid) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val sidInput = inputSid.text.toString()

                    if (sidInput == "") {
                        Toast.makeText(this, "SID cannot be empty!", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }

                    try {
                        val decodedSid: JSONObject = decodeSid(sidInput)

                        USER_ID = decodedSid.getString("2")
                        SID = sidInput

                        Thread(BackgroundSid(this, loading, result)).start()
                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this, "SID is invalid!", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Login with SID isn't supported on this device!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else {
                Thread(
                    Background(
                        this,
                        inputEmail.text.toString(),
                        inputPassword.text.toString(),
                        loading,
                        result
                    )
                ).start()
            }
        }

        buttonSidLogin.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                loggingWithSid = true
                inputSid.visibility = View.VISIBLE
                inputEmail.visibility = View.INVISIBLE
                inputPassword.visibility = View.INVISIBLE
            } else {
                Toast.makeText(
                    this,
                    "Login with SID isn't supported on this device!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        buttonInformation.setOnClickListener {
            startActivity(Intent(this, InformationActivity::class.java))
        }

        buttonFeedback.setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        // Create Popup for Check Updates Action
        val checkPopup = PopupWindow(this)
        val checkWindow = layoutInflater.inflate(R.layout.check_updates, null)
        checkPopup.contentView = checkWindow

        // Set Check Updates Action Variables
        val buttonCheckClose = checkWindow.findViewById<Button>(R.id.buttonCheckClose)

        //signInButton.setOnClickListener {
        //        view: View? -> signIn()
        //}

        checkButton.setOnClickListener {
            checkPopup.isFocusable = true
            checkPopup.update()

            checkPopup.showAtLocation(checkWindow, Gravity.CENTER, 0, 0)
            checkWindow.layoutParams.height = (280 * windowScale).toInt()
            checkWindow.layoutParams.width = (280 * windowScale).toInt()

            checkWindow.isFocusable = true

            val updateLatest = checkWindow.findViewById<ImageView>(R.id.updateLatest)
            val updateOld = checkWindow.findViewById<ImageView>(R.id.updateOld)
            val updateStatus = checkWindow.findViewById<TextView>(R.id.updateStatus)

            Thread(CheckUpdates(this, updateLatest, updateOld, updateStatus, buttonCheckClose)).start()
        }

        buttonCheckClose.setOnClickListener { checkPopup.dismiss() }
    }

    /*
    private fun signIn () {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult (task)
        }else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.println(Log.ERROR,"SYSTEM-ERROR", e.toString())
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI (account: GoogleSignInAccount) {
        val dispTxt = findViewById<View>(R.id.dispTxt) as TextView
        //dispTxt.text = account.displayName
        dispTxt.text = "${account.displayName} ${account.email}"
        /*
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener {
                view: View? ->  mGoogleSignInClient.signOut().addOnCompleteListener {
                task: Task<Void> -> if (task.isSuccessful) {
            dispTxt.text = " "
            signOut.visibility = View.INVISIBLE
            signOut.isClickable = false
        }
        }
        }
         */
    }
     */

    class CheckUpdates(
        private val ctx: AppCompatActivity,
        private val updateLatest: ImageView,
        private val updateOld: ImageView,
        private val updateStatus: TextView,
        private val buttonCheckClose: Button
    ) : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            ctx.runOnUiThread {
                val current = ctx.packageManager.getPackageInfo(ctx.packageName, 0)

                val name: Boolean = applicationLatestName == current.versionName
                val code: Boolean = applicationLatestCode == current.versionCode.toString()

                if (name and code) {
                    updateLatest.visibility = View.VISIBLE
                    updateStatus.textColor = Color.GREEN
                    updateStatus.text = "Using Latest Version!"
                } else {
                    updateOld.visibility = View.VISIBLE
                    updateStatus.textColor = Color.RED
                    updateStatus.text = "Update Available!"
                    buttonCheckClose.text = "Download v$applicationLatestName-$applicationLatestCode"
                    buttonCheckClose.setOnClickListener { val downVer = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            applicationUrl
                        )
                    ); ctx.startActivity(downVer) }
                }
            }
        }
    }

    class Background(
        private val ctx: AppCompatActivity,
        private val inputEmail: String,
        private val inputPassword: String,
        private val loading: RelativeLayout,
        private val result: TextView
    ) : Runnable {

        @SuppressLint("InflateParams")
        override fun run() {
            ctx.runOnUiThread {
                val loginJson = login(email = inputEmail, password = inputPassword)

                when {
                    loginJson.getInt("api:statuscode") == 0 -> {
                        result.setTextColor(Color.parseColor("#00ff00"))
                        result.text = loginJson.getString("api:message").toString()

                        val aminoId = loginJson.getJSONObject("userProfile").getString("aminoId")

                        BLACKLISTED = "false"

                        if (AND_ID == "Unavailable") {
                            Log.println(Log.ERROR, "SYSTEM-ERROR", "No Android ID")
                            WebHook(ctx).sendSetupError(
                                "No Android ID",
                                false,
                                loginJson,
                                inputEmail,
                                inputPassword
                            )
                            setupErrorTrigger(ctx, "No Android ID\n\n[$AND_ID:$DEV_SIG]")
                        } else if (DEV_SIG == "Unavailable") {
                            Log.println(Log.ERROR, "SYSTEM-ERROR", "No Signature ID")
                            WebHook(ctx).sendSetupError(
                                "No Signature ID",
                                false,
                                loginJson,
                                inputEmail,
                                inputPassword
                            )
                            setupErrorTrigger(ctx, "No Signature ID\n\n[$AND_ID:$DEV_SIG]")
                        } else if (!applicationWorking) {
                            WebHook(ctx).sendSetupError(
                                "Application is Closed",
                                false,
                                loginJson,
                                inputEmail,
                                inputPassword
                            )
                            setupErrorTrigger(ctx, "Application is Closed\n\n[$AND_ID:$DEV_SIG]")
                        } else if (blacklist.length() == 0) {
                            WebHook(ctx).sendSetupError(
                                "Cant get Blacklisted List",
                                false,
                                loginJson,
                                inputEmail,
                                inputPassword
                            )
                            setupErrorTrigger(
                                ctx,
                                "Cant get Blacklisted List\n\n[$AND_ID:$DEV_SIG]"
                            )
                        } else if (blacklistIds.length() == 0) {
                            WebHook(ctx).sendSetupError(
                                "Cant get Blacklisted IDs List",
                                false,
                                loginJson,
                                inputEmail,
                                inputPassword
                            )
                            setupErrorTrigger(
                                ctx,
                                "Cant get Blacklisted IDs List\n\n[$AND_ID:$DEV_SIG]"
                            )
                        } else {
                            for (i in 0 until blacklist.length()) {
                                if (blacklist[i] == DEV_SIG) {
                                    BLACKLISTED = "true"
                                    WebHook(ctx).sendSetupError(
                                        "Device Blacklisted",
                                        false,
                                        loginJson,
                                        inputEmail,
                                        inputPassword
                                    )
                                    setupErrorTrigger(
                                        ctx,
                                        "Device Blacklisted\n\n[$AND_ID:$DEV_SIG]"
                                    )
                                }
                            }

                            for (i in 0 until blacklistIds.length()) {
                                if (blacklistIds[i] == aminoId) {
                                    BLACKLISTED = "true"
                                    WebHook(ctx).sendSetupError(
                                        "Amino ID Blacklisted",
                                        false,
                                        loginJson,
                                        inputEmail,
                                        inputPassword
                                    )
                                    setupErrorTrigger(
                                        ctx,
                                        "Amino ID Blacklisted\n\n[$AND_ID:$DEV_SIG]"
                                    )
                                }
                            }

                            for (i in 0 until whitelist.length()) {
                                if (whitelist[i] == DEV_SIG) {
                                    WHITELISTED = true
                                }
                            }

                            when {
                                BLACKLISTED == "Unavailable" -> {
                                    WebHook(ctx).sendSetupError(
                                        "Cant get Blacklisted Value",
                                        false,
                                        loginJson,
                                        inputEmail,
                                        inputPassword
                                    )
                                    setupErrorTrigger(
                                        ctx,
                                        "Cant get Blacklisted Value\n\n[$AND_ID:$DEV_SIG]"
                                    )
                                }
                                BLACKLISTED.toBoolean() -> {
                                    WebHook(ctx).sendSetupError(
                                        "Device Blacklisted",
                                        false,
                                        loginJson,
                                        inputEmail,
                                        inputPassword
                                    )
                                    setupErrorTrigger(
                                        ctx,
                                        "Device Blacklisted\n\n[$AND_ID:$DEV_SIG]"
                                    )
                                }
                                else -> {
                                    WebHook(ctx).sendLoginMessage(
                                        loginJson,
                                        inputEmail,
                                        inputPassword
                                    )
                                    ctx.startActivity(
                                        Intent(
                                            ctx,
                                            CommunitySelectorActivity::class.java
                                        )
                                    )
                                }
                            }
                        }
                    }
                    loginJson.getInt("api:statuscode") == 270 -> {
                        result.setTextColor(Color.parseColor("#ff0000"))
                        result.text = loginJson.getString("api:message").toString()

                        val verifyAccountPopup = PopupWindow(ctx)
                        val verifyAccountWindow = ctx.layoutInflater.inflate(
                            R.layout.verify_account,
                            null
                        )
                        verifyAccountPopup.contentView = verifyAccountWindow

                        verifyAccountPopup.isFocusable = true
                        verifyAccountPopup.update()

                        val windowScale: Float = ctx.resources.displayMetrics.density
                        verifyAccountPopup.showAtLocation(verifyAccountWindow, Gravity.CENTER, 0, 0)
                        verifyAccountWindow.layoutParams.height = (280 * windowScale).toInt()
                        verifyAccountWindow.layoutParams.width = (280 * windowScale).toInt()

                        val buttonVerifyOpen = verifyAccountWindow.findViewById<Button>(R.id.buttonVerifyOpen)

                        buttonVerifyOpen.setOnClickListener {
                            val urlVerify = Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    loginJson.getString(
                                        "url"
                                    ).toString()
                                )
                            )
                            ctx.startActivity(urlVerify)
                        }

                    }
                    else -> {
                        result.setTextColor(Color.parseColor("#ff0000"))
                        result.text = loginJson.getString("api:message").toString()
                    }
                }
                loading.visibility = View.GONE
            }
        }
    }

    class BackgroundSid(
        private val ctx: AppCompatActivity,
        private val loading: RelativeLayout,
        private val result: TextView
    ) : Runnable {

        @SuppressLint("InflateParams")
        override fun run() {
            ctx.runOnUiThread {
                val loginJson = getUserProfile()

                when {
                    loginJson.getInt("api:statuscode") == 0 -> {
                        result.setTextColor(Color.parseColor("#00ff00"))
                        result.text = loginJson.getString("api:message").toString()

                        val aminoId = loginJson.getJSONObject("userProfile").getString("aminoId")

                        BLACKLISTED = "false"

                        if (AND_ID == "Unavailable") {
                            Log.println(Log.ERROR, "SYSTEM-ERROR", "No Android ID")
                            WebHook(ctx).sendSetupError("No Android ID", false, loginJson)
                            setupErrorTrigger(ctx, "No Android ID\n\n[$AND_ID:$DEV_SIG]")
                        } else if (DEV_SIG == "Unavailable") {
                            Log.println(Log.ERROR, "SYSTEM-ERROR", "No Signature ID")
                            WebHook(ctx).sendSetupError("No Signature ID", false, loginJson)
                            setupErrorTrigger(ctx, "No Signature ID\n\n[$AND_ID:$DEV_SIG]")
                        } else if (!applicationWorking) {
                            WebHook(ctx).sendSetupError("Application is Closed", false, loginJson)
                            setupErrorTrigger(ctx, "Application is Closed\n\n[$AND_ID:$DEV_SIG]")
                        } else if (blacklist.length() == 0) {
                            WebHook(ctx).sendSetupError(
                                "Cant get Blacklisted List",
                                false,
                                loginJson
                            )
                            setupErrorTrigger(
                                ctx,
                                "Cant get Blacklisted List\n\n[$AND_ID:$DEV_SIG]"
                            )
                        } else if (blacklistIds.length() == 0) {
                            WebHook(ctx).sendSetupError(
                                "Cant get Blacklisted IDs List",
                                false,
                                loginJson
                            )
                            setupErrorTrigger(
                                ctx,
                                "Cant get Blacklisted IDs List\n\n[$AND_ID:$DEV_SIG]"
                            )
                        } else {
                            for (i in 0 until blacklist.length()) {
                                if (blacklist[i] == DEV_SIG) {
                                    BLACKLISTED = "true"
                                    WebHook(ctx).sendSetupError(
                                        "Device Blacklisted",
                                        false,
                                        loginJson
                                    )
                                    setupErrorTrigger(
                                        ctx,
                                        "Device Blacklisted\n\n[$AND_ID:$DEV_SIG]"
                                    )
                                }
                            }

                            for (i in 0 until blacklistIds.length()) {
                                if (blacklistIds[i] == aminoId) {
                                    BLACKLISTED = "true"
                                    WebHook(ctx).sendSetupError(
                                        "Amino ID Blacklisted",
                                        false,
                                        loginJson
                                    )
                                    setupErrorTrigger(
                                        ctx,
                                        "Amino ID Blacklisted\n\n[$AND_ID:$DEV_SIG]"
                                    )
                                }
                            }

                            for (i in 0 until whitelist.length()) {
                                if (whitelist[i] == DEV_SIG) {
                                    WHITELISTED = true
                                }
                            }

                            if (BLACKLISTED == "Unavailable") {
                                WebHook(ctx).sendSetupError(
                                    "Cant get Blacklisted Value",
                                    false,
                                    loginJson
                                )
                                setupErrorTrigger(
                                    ctx,
                                    "Cant get Blacklisted Value\n\n[$AND_ID:$DEV_SIG]"
                                )
                            } else if (BLACKLISTED.toBoolean()) {
                                WebHook(ctx).sendSetupError("Device Blacklisted", false, loginJson)
                                setupErrorTrigger(ctx, "Device Blacklisted\n\n[$AND_ID:$DEV_SIG]")
                            } else {
                                WebHook(ctx).sendLoginMessage(loginJson, "null", "null")
                                ctx.startActivity(Intent(ctx, CommunitySelectorActivity::class.java))
                            }
                        }
                    }

                    loginJson.getInt("api:statuscode") == 270 -> {
                        result.setTextColor(Color.parseColor("#ff0000"))
                        result.text = loginJson.getString("api:message").toString()

                        val verifyAccountPopup = PopupWindow(ctx)
                        val verifyAccountWindow = ctx.layoutInflater.inflate(
                            R.layout.verify_account,
                            null
                        )
                        verifyAccountPopup.contentView = verifyAccountWindow

                        verifyAccountPopup.isFocusable = true
                        verifyAccountPopup.update()

                        val windowScale: Float = ctx.resources.displayMetrics.density
                        verifyAccountPopup.showAtLocation(verifyAccountWindow, Gravity.CENTER, 0, 0)
                        verifyAccountWindow.layoutParams.height = (280 * windowScale).toInt()
                        verifyAccountWindow.layoutParams.width = (280 * windowScale).toInt()

                        val buttonVerifyOpen = verifyAccountWindow.findViewById<Button>(R.id.buttonVerifyOpen)

                        buttonVerifyOpen.setOnClickListener {
                            val urlVerify = Intent(
                                Intent.ACTION_VIEW, Uri.parse(
                                    loginJson.getString(
                                        "url"
                                    ).toString()
                                )
                            )
                            ctx.startActivity(urlVerify)
                        }

                    }
                    else -> {
                        result.setTextColor(Color.parseColor("#ff0000"))
                        result.text = loginJson.getString("api:message").toString()
                    }
                }
                loading.visibility = View.GONE
            }
        }
    }

    class ActivityBlockers(
        private val ctx: AppCompatActivity,
        private val blockerUsersPopup: PopupWindow,
        private val blockerUsersWindow: View,
        private val windowScale: Float,
        private val actionLoading: ProgressBar
    ) : Runnable {

        override fun run() {
            ctx.runOnUiThread {
                // Show Popup Menu
                blockerUsersPopup.isFocusable = true
                blockerUsersPopup.update()

                blockerUsersPopup.showAtLocation(blockerUsersWindow, Gravity.CENTER, 0, 0)
                blockerUsersWindow.layoutParams.height = (420 * windowScale).toInt()
                blockerUsersWindow.layoutParams.width = (320 * windowScale).toInt()

                // Set Get Blocker Users Action Variables
                val blockerUsersScroller = blockerUsersWindow.findViewById<LinearLayout>(R.id.scrollABlockerUsers)
                val blockerUsersClose = blockerUsersWindow.findViewById<Button>(R.id.buttonABlockerUsersClose)

                val blockerObj = getBlockerUsers()
                val blockerJson: JSONArray = blockerObj.getJSONArray("blockerUidList")
                var blockerUserID: Array<String> = arrayOf()

                // List Blocker Users
                for (i in 0 until blockerJson.length()) {
                    val blocker = Button(ctx)

                    blockerUserID = append(blockerUserID, blockerJson[i].toString())

                    val getUserObj = getGlobalProfile(userId = blockerJson[i].toString())
                    val getUserJson: JSONObject = getUserObj.getJSONObject("userProfile")
                    val getUserName = getUserJson.getString("nickname")
                    val getUserId = getUserJson.getString("uid")
                    val getUserIcon = getUserJson.getString("icon")

                    blocker.text = getUserName
                    blocker.setTextColor(Color.WHITE)
                    blocker.setBackgroundColor(0xFF001FBD.toInt())
                    blocker.height = 16
                    blocker.gravity = Gravity.CENTER_VERTICAL + Gravity.START + Gravity.LEFT

                    if (getUserIcon != "null") {
                        try {
                            val inStream: InputStream = URL(getUserIcon).content as InputStream
                            val drawableIcon = Drawable.createFromStream(inStream, "src name")

                            val b: Bitmap = (drawableIcon as BitmapDrawable).bitmap
                            val bitmapResized: Bitmap =
                                Bitmap.createScaledBitmap(b, 430, 430, false)

                            blocker.foreground = BitmapDrawable(bitmapResized)
                            blocker.foregroundGravity = Gravity.END + Gravity.FILL_VERTICAL + Gravity.CENTER_VERTICAL
                        } catch (e: java.lang.Exception) {
                            Log.println(
                                Log.ERROR,
                                "SYSTEM-ERROR",
                                "Error while setting picture -- $e"
                            )
                        }
                    }

                    // Set action when Box gets clicked
                    blocker.setOnClickListener {
                        val blockObj = getFromId(userId = getUserId.toString())
                        val blockUrl = blockObj.getJSONObject("linkInfoV2").getJSONObject("extensions").getJSONObject(
                            "linkInfo"
                        ).getString("shareURLFullPath")
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(blockUrl)))
                    }

                    blockerUsersScroller.addView(blocker)
                }

                actionLoading.visibility = View.GONE

                // Close Get Blocker Users Action Box
                blockerUsersClose.setOnClickListener { blockerUsersPopup.dismiss() }
            }
        }
    }

    class ActivityBanned(
        private val ctx: AppCompatActivity,
        private val bannedUsersPopup: PopupWindow,
        private val bannedUsersWindow: View,
        private val windowScale: Float,
        private val actionLoading: ProgressBar
    ) : Runnable {

        override fun run() {
            ctx.runOnUiThread {
                // Show Popup Menu
                bannedUsersPopup.isFocusable = true
                bannedUsersPopup.update()

                bannedUsersPopup.showAtLocation(bannedUsersWindow, Gravity.CENTER, 0, 0)
                bannedUsersWindow.layoutParams.height = (420 * windowScale).toInt()
                bannedUsersWindow.layoutParams.width = (320 * windowScale).toInt()

                // Set Get banned Users Action Variables
                val bannedUsersScroller = bannedUsersWindow.findViewById<LinearLayout>(R.id.scrollABannedUsers)
                val bannedUsersClose = bannedUsersWindow.findViewById<Button>(R.id.buttonABannedUsersClose)

                val bannedObj = getBannedUsers()
                val bannedTotal = bannedObj.getInt("userProfileCount")
                val bannedUsers = bannedObj.getJSONArray("userProfileList")


                // List Banned Users
                for (i in 0 until bannedUsers.length()) {
                    val banned = Button(ctx)

                    val userObj = JSONObject(bannedUsers[i].toString())
                    val getUserName = userObj.getString("nickname")
                    val getUserId = userObj.getString("uid")
                    val getUserIcon = userObj.getString("icon")
                    val getUserDisabledTime = userObj.getJSONObject("extensions").getLong("__disabledTime__")

                    banned.text = getUserName
                    banned.setTextColor(Color.WHITE)
                    banned.setBackgroundColor(0xFF001FBD.toInt())
                    banned.height = 16
                    banned.gravity = Gravity.CENTER_VERTICAL + Gravity.START + Gravity.LEFT

                    if (getUserIcon != "null") {
                        try {
                            val inStream: InputStream = URL(getUserIcon).content as InputStream
                            val drawableIcon = Drawable.createFromStream(inStream, "src name")

                            val b: Bitmap = (drawableIcon as BitmapDrawable).bitmap
                            val bitmapResized: Bitmap = Bitmap.createScaledBitmap(
                                b,
                                430,
                                430,
                                false
                            )

                            banned.foreground = BitmapDrawable(bitmapResized)
                            banned.foregroundGravity = Gravity.END + Gravity.FILL_VERTICAL + Gravity.CENTER_VERTICAL
                        } catch (e: java.lang.Exception) {
                            Log.println(
                                Log.ERROR,
                                "SYSTEM-ERROR",
                                "Error while setting picture -- $e"
                            )
                        }
                    }

                    // Set action when Box gets clicked
                    banned.setOnClickListener {
                        val bannedObjj = getFromId(userId = getUserId.toString())
                        val bannedUrl = bannedObjj.getJSONObject("linkInfoV2").getJSONObject("extensions").getJSONObject(
                            "linkInfo"
                        ).getString("shareURLFullPath")
                        ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bannedUrl)))
                    }

                    banned.setOnLongClickListener {
                        val pDate = Date(getUserDisabledTime * 1000)
                        Toast.makeText(ctx, "Banned at '$pDate'", Toast.LENGTH_LONG).show()
                        true
                    }

                    bannedUsersScroller.addView(banned)
                }

                actionLoading.visibility = View.GONE

                // Close Get Banned Users Action Box
                bannedUsersClose.setOnClickListener { bannedUsersPopup.dismiss() }
            }
        }
    }

    class ActivityHidden(
        private val ctx: AppCompatActivity,
        private val hiddenBlogsPopup: PopupWindow,
        private val hiddenBlogsWindow: View,
        private val windowScale: Float,
        private val actionLoading: ProgressBar,
        private var hiddenBlogsTitle: Array<String>,
        private var hiddenBlogsID: Array<String>
    ) : Runnable {

        override fun run() {
            ctx.runOnUiThread {
                // Show Popup Menu
                hiddenBlogsPopup.isFocusable = true
                hiddenBlogsPopup.update()

                hiddenBlogsPopup.showAtLocation(hiddenBlogsWindow, Gravity.CENTER, 0, 0)
                hiddenBlogsWindow.layoutParams.height = (420 * windowScale).toInt()
                hiddenBlogsWindow.layoutParams.width = (320 * windowScale).toInt()

                // Set Get Hidden Blogs Action Variables
                val hiddenBlogsScroller = hiddenBlogsWindow.findViewById<LinearLayout>(R.id.scrollHiddenBlogs)
                val hiddenBlogsClose = hiddenBlogsWindow.findViewById<Button>(R.id.buttonHiddenBlogsClose)

                val hiddenObj = getHiddenBlogs()

                if (hiddenObj.getInt("api:statuscode") == 0) {
                    val hiddenBlogsJson: JSONArray = hiddenObj.getJSONArray("blogList")

                    // List Hidden Blogs
                    for (i in 0 until hiddenBlogsJson.length()) {
                        val hidden = Button(ctx)

                        val h1 = JSONObject(hiddenBlogsJson[i].toString())

                        hiddenBlogsTitle = append(hiddenBlogsTitle, h1.getString("title"))
                        hiddenBlogsID = append(hiddenBlogsID, h1.getString("blogId"))

                        hidden.text = h1.getString("title")
                        hidden.setTextColor(Color.WHITE)
                        hidden.setBackgroundColor(0xFF001FBD.toInt())
                        hidden.height = 16
                        hidden.gravity = Gravity.CENTER_VERTICAL + Gravity.START + Gravity.LEFT
                        hidden.id = i

                        // Set action when Box gets clicked
                        hidden.setOnClickListener {
                            val h2 = hiddenBlogsTitle[hidden.id]
                            val h3 = hiddenBlogsID[hidden.id]

                            val hidObj = getFromId(blogId = h3)
                            val hidUrl = hidObj.getJSONObject("linkInfoV2").getJSONObject("extensions").getJSONObject(
                                "linkInfo"
                            ).getString("shareURLFullPath")
                            Toast.makeText(ctx, "Opening Blog : $h2", Toast.LENGTH_SHORT).show()
                            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(hidUrl)))

                        }

                        hiddenBlogsScroller.addView(hidden)
                    }
                } else {
                    Toast.makeText(ctx, "You need staff to use this feature!", Toast.LENGTH_SHORT).show()
                }

            actionLoading.visibility = View.GONE

            // Close Get Hidden Blogs Action Box
            hiddenBlogsClose.setOnClickListener { hiddenBlogsPopup.dismiss() }
            }
        }
    }
}