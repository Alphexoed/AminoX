package com.slimakoi.aminox

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import android.view.View
import android.webkit.WebView
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.AdapterView.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_action_selector.*
import kotlinx.android.synthetic.main.activity_information.view.*
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class ActionSelectorActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n", "InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContentView(R.layout.activity_action_selector)

        // Set Primary Variables
        val profileImageAction = findViewById<WebView>(R.id.profileImageAction)
        val profileNameAction = findViewById<TextView>(R.id.profileNameAction)
        val roleTitle = findViewById<Button>(R.id.roleTitle)
        val warningTitle = findViewById<Button>(R.id.warningTitle)
        val strikeTitle = findViewById<Button>(R.id.strikeTitle)
        val credits = findViewById<TextView>(R.id.infoTxtCredits5)
        val windowScale: Float = this.resources.displayMetrics.density

        // Set Button Variables
        val sendMessageButton = findViewById<Button>(R.id.buttonASendMessage)
        val editTitlesButton = findViewById<Button>(R.id.buttonAEditTitles)
        val blockerUsersButton = findViewById<Button>(R.id.buttonAGetBlocked)
        val bannedUsersButton = findViewById<Button>(R.id.buttonAGetBanned)
        val buttonHiddenBlogs = findViewById<Button>(R.id.buttonHiddenBlogs)
        val buttonKickUser = findViewById<Button>(R.id.buttonKickUser)
        val buttonCheckin = findViewById<Button>(R.id.buttonCheckin)
        val buttonLottery = findViewById<Button>(R.id.buttonLottery)
        val buttonIdFinder = findViewById<Button>(R.id.buttonIdFinder)
        val buttonStartChat = findViewById<Button>(R.id.buttonStartChat)
        val buttonCoinSender = findViewById<Button>(R.id.buttonCoinSender)
        val buttonUnfollow = findViewById<Button>(R.id.buttonUnfollow)
        val buttonChatStart = findViewById<Button>(R.id.buttonChatStart)
        val buttonQuizPlayer = findViewById<Button>(R.id.buttonQuizPlayer)
        val buttonFarmer = findViewById<Button>(R.id.buttonFarmer)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.ActionSelectorLayout)
        val constraintSet = ConstraintSet()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        credits.setOnClickListener { val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/Slimakoi")); startActivity(i) }

        /*
        if (WHITELISTED) {
            sendMessageButton.visibility = View.VISIBLE
            buttonStartChat.visibility = View.VISIBLE
        }
         */

        val aminoObject = JSONObject(getAminoProfile().toString())
        val json: JSONObject = aminoObject.getJSONObject("userProfile")

        val nickname = json.getString("nickname").toString()
        val icon = json.getString("icon").toString()
        val role = json.getString("role").toInt()
        try {
            val warns = JSONObject(json.getString("adminInfo")).getString("warningCount").toInt(); warningTitle.text = "$warns Warns"
        } catch (error: JSONException) {
            warningTitle.text = "? Warns"
        }
        try {
            val strikes = JSONObject(json.getString("adminInfo")).getString("strikeCount").toInt(); strikeTitle.text = "$strikes Strikes"
        } catch (error: JSONException) {
            strikeTitle.text = "? Strikes"
        }

        if (role != 0) {
            roleTitle.visibility = View.VISIBLE
            if (role == 102) {
                roleTitle.text = "Agent"
            }
            if (role == 101) {
                roleTitle.text = "Curator"
            }
            if (role == 100) {
                roleTitle.text = "Leader"
            }
        } else {
            roleTitle.visibility = View.GONE
            /*
            constraintSet.clone(constraintLayout)

            constraintSet.connect(
                R.id.warningTitle,
                ConstraintSet.START,
                R.id.profileImageAction,
                ConstraintSet.END,
                0
            )
            constraintSet.applyTo(constraintLayout)

             */
        }

        profileNameAction.text = nickname
        profileNameAction.setTypeface(null, Typeface.BOLD)
        profileImageAction.loadUrl(icon)
        profileImageAction.settings.loadWithOverviewMode = true
        profileImageAction.settings.useWideViewPort = true

        // Create Popup for Send Message Action
        val sendMessagePopup = PopupWindow(this)
        val sendMessageWindow = layoutInflater.inflate(R.layout.action_send_message, null)
        sendMessagePopup.contentView = sendMessageWindow

        // Set Message Action Variables
        val inputMessage = sendMessageWindow.findViewById<TextView>(R.id.inputSMMessage)
        val inputType = sendMessageWindow.findViewById<TextView>(R.id.inputSMType)
        val sendMessageSend = sendMessageWindow.findViewById<Button>(R.id.buttonASendMessageSend)
        val sendMessageClose = sendMessageWindow.findViewById<Button>(R.id.buttonASendMessageClose)
        val selectChats = sendMessageWindow.findViewById<Spinner>(R.id.spinnerChats)
        val actionLoading = findViewById<ProgressBar>(R.id.progress)

        // Send Message Action
        sendMessageButton.setOnClickListener {
            // Show Popup Menu
            sendMessagePopup.isFocusable = true
            sendMessagePopup.update()

            sendMessagePopup.showAtLocation(sendMessageWindow, Gravity.CENTER, 0, 0)
            sendMessageWindow.layoutParams.height = (400 * windowScale).toInt()
            sendMessageWindow.layoutParams.width = (320 * windowScale).toInt()

            //Set Message Type Default
            inputType.text = "0"

            // Select Chats Spinner
            val chatObject = JSONObject(getChatList().toString())
            val jsonChat: JSONArray = chatObject.getJSONArray("threadList")
            var itemsTitle: Array<String> = arrayOf()
            var itemsId: Array<String> = arrayOf()

            // List Chats the User is in
            for (i in 0 until jsonChat.length()) {
                val item = jsonChat.getJSONObject(i)
                val titleObj = JSONObject(item.toString())

                val chatTitle = titleObj.getString("title")
                val chatId = titleObj.getString("threadId")

                itemsTitle = if (chatTitle == "null") { append(itemsTitle, chatId) } else { append(itemsTitle, chatTitle) }
                itemsId = append(itemsId, chatId)
            }

            // Fill the Dropdown Menu with the Chats Gathered
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, itemsTitle)
            selectChats.adapter = adapter

            // Send Message
            sendMessageSend.setOnClickListener {
                val data = sendChatMessage(
                    message = inputMessage.text.toString(),
                    chatId = itemsId[selectChats.selectedItemPosition],
                    type = inputType.text.toString().toInt()
                )

                val jsonData = JSONObject(data.toString())
                Toast.makeText(this, jsonData.getString("api:message").toString(), Toast.LENGTH_LONG).show()
            }

            // Close Send Message Action Box
            sendMessageClose.setOnClickListener { sendMessagePopup.dismiss() }
        }

        // Create Popup for Get Blocked Users Action
        val blockerUsersPopup = PopupWindow(this)
        val blockerUsersWindow = layoutInflater.inflate(R.layout.action_get_blocker_users, null)
        blockerUsersPopup.contentView = blockerUsersWindow

        blockerUsersButton.setOnClickListener {
            actionLoading.visibility = View.VISIBLE
            Thread(MainActivity.ActivityBlockers(this, blockerUsersPopup, blockerUsersWindow, windowScale, actionLoading)).start()
        }

        // Create Popup for Get banned Users Action
        val bannedUsersPopup = PopupWindow(this)
        val bannedUsersWindow = layoutInflater.inflate(R.layout.action_get_banned_users, null)
        bannedUsersPopup.contentView = bannedUsersWindow

        bannedUsersButton.setOnClickListener {
            actionLoading.visibility = View.VISIBLE
            Thread(MainActivity.ActivityBanned(this, bannedUsersPopup, bannedUsersWindow, windowScale, actionLoading)).start()
        }

        editTitlesButton.setOnClickListener {
            try {
                titleNum = json.getJSONObject("extensions").getJSONArray("customTitles").length()
                startActivity(Intent(this, EditTitlesActivity()::class.java))
            } catch (e: JSONException) {
                Toast.makeText(this, "You do not have any titles!", Toast.LENGTH_LONG).show()
            }
        }

        // Create Popup for Kick User Action
        val kickUserPopup = PopupWindow(this)
        val kickUserWindow = layoutInflater.inflate(R.layout.action_kick_user, null)
        kickUserPopup.contentView = kickUserWindow

        // Set Kick User Action Variables
        val selectKickChats = kickUserWindow.findViewById<Spinner>(R.id.spinnerKickChats)
        val selectKickUsers = kickUserWindow.findViewById<Spinner>(R.id.spinnerKickUsers)
        val buttonKickUserSend = kickUserWindow.findViewById<Button>(R.id.buttonKickUserSend)
        val buttonKickUserClose = kickUserWindow.findViewById<Button>(R.id.buttonKickUserClose)
        val checkKickUserAllowRejoin = kickUserWindow.findViewById<CheckBox>(R.id.checkKickUserAllowRejoin)

        var itemsKickUserName: Array<String> = arrayOf()
        var itemsKickUserId: Array<String> = arrayOf()
        var itemsKickChatTitle: Array<String> = arrayOf()
        var itemsKickChatId: Array<String> = arrayOf()

        // Kick User Action
        buttonKickUser.setOnClickListener {
            // Show Popup Menu
            kickUserPopup.isFocusable = true
            kickUserPopup.update()

            kickUserPopup.showAtLocation(kickUserWindow, Gravity.CENTER, 0, 0)
            kickUserWindow.layoutParams.height = (300 * windowScale).toInt()
            kickUserWindow.layoutParams.width = (320 * windowScale).toInt()

            // Select Chats Spinner
            val chatObject = JSONObject(getChatList().toString())
            val jsonChat: JSONArray = chatObject.getJSONArray("threadList")

            itemsKickChatTitle = arrayOf()
            itemsKickChatId = arrayOf()

            // List Chats the User is in
            for (i in 0 until jsonChat.length()) {
                val item = jsonChat.getJSONObject(i)
                val titleObj = JSONObject(item.toString())

                val chatTitle = titleObj.getString("title")
                val chatId = titleObj.getString("threadId")

                itemsKickChatTitle = if (chatTitle == "null") {
                    append(itemsKickChatTitle, chatId)
                } else {
                    append(itemsKickChatTitle, chatTitle)
                }
                itemsKickChatId = append(itemsKickChatId, chatId)
            }

            // Fill the Dropdown Menu with the Chats Gathered
            val adapterKick = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, itemsKickChatTitle)
            selectKickChats.adapter = adapterKick

            selectKickChats.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    val selectedKickChat = itemsKickChatId[selectKickChats.selectedItemPosition]

                    // Clear List of User Names and IDs
                    itemsKickUserName = arrayOf()
                    itemsKickUserId = arrayOf()

                    // Select Chats Spinner
                    val userObject = JSONObject(getChatUsersList(chatId = selectedKickChat, start = 0).toString())
                    val jsonChatUsers: JSONArray = userObject.getJSONArray("memberList")

                    // List Users in the Chat
                    for (x in 0 until jsonChatUsers.length()) {
                        val itemKickUser = jsonChatUsers.getJSONObject(x)
                        val objKickUser = JSONObject(itemKickUser.toString())

                        val kickUserName = objKickUser.getString("nickname")
                        val kickUserId = objKickUser.getString("uid")

                        itemsKickUserName = append(itemsKickUserName, kickUserName)
                        itemsKickUserId = append(itemsKickUserId, kickUserId)
                    }

                    // Fill the Dropdown Menu with the Chats Gathered
                    val adapterKickUsers = ArrayAdapter(this@ActionSelectorActivity, android.R.layout.simple_spinner_dropdown_item, itemsKickUserName)
                    selectKickUsers.adapter = adapterKickUsers
                    return
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    return
                }
            }
        }

        // Kick User Action
        buttonKickUserSend.setOnClickListener {
            val selectedKickUserName = itemsKickUserName[selectKickUsers.selectedItemPosition]
            val selectedKickUserId = itemsKickUserId[selectKickUsers.selectedItemPosition]
            val selectedKickChatTitle = itemsKickChatTitle[selectKickChats.selectedItemPosition]
            val selectedKickChatId = itemsKickChatId[selectKickChats.selectedItemPosition]
            val allowRejoin = checkKickUserAllowRejoin.isChecked

            val kickUserCmd = kickUser(
                chatId = selectedKickChatId,
                userId = selectedKickUserId,
                allowRejoin = allowRejoin
            )

            val kickUserCmdJSON = JSONObject(kickUserCmd.toString())

            Toast.makeText(this, "Kicking '$selectedKickUserName' from '$selectedKickChatTitle' - ${kickUserCmdJSON.getString("api:message")}", Toast.LENGTH_LONG).show()
        }

        // Close Kick User Action Box
        buttonKickUserClose.setOnClickListener { kickUserPopup.dismiss() }

        // Checkin Action
        buttonCheckin.setOnClickListener {
            val checkInLoader = AlertDialog.Builder(this).create()
            checkInLoader.setTitle("Checking in on '$COMMUNITY_NAME'")
            checkInLoader.setMessage("Don't close the app while its checking-in!\n\nStatus: 0 / 287 (0.0%)")
            checkInLoader.setCancelable(false)

            var checkTimes = 0
            var checkOKTimes = 0

            doAsync {
                for (num in -1430..1430 step 10) {
                    val checkJSON = JSONObject(checkIn(tz = num).toString())
                    Log.println(Log.DEBUG, "ACTION-CHECKIN", "TZ: $num / JSON: $checkJSON")
                    if (checkJSON.getString("api:statuscode") == "0") {
                        checkTimes += 1; checkOKTimes += 1
                    } else {
                        checkTimes += 1
                    }

                    val percent = String.format("%.2f", (checkTimes.toFloat()/287)*100)
                    checkInLoader.setMessage("Don't close the app while its checking-in!\n\nStatus: $checkTimes / 287 ($percent%)")
                }

                runOnUiThread {
                    checkInLoader.setTitle("Completed Check-in!")
                    checkInLoader.setMessage("You can now close this tab.\n\nChecked-in $checkOKTimes out of 287 times")
                    checkInLoader.setCancelable(true)
                }
            }

            checkInLoader.show()
        }

        // Play Lottery Action
        buttonLottery.setOnClickListener {
            var lotteryTimes = 0
            var coinsWon = 0

            val lotteryLoader = AlertDialog.Builder(this).create()
            lotteryLoader.setTitle("Playing lottery on '$COMMUNITY_NAME'")
            lotteryLoader.setMessage("Don't close the app while its playing the lottery!\n\nStatus: 0 / 287 (0.0%)")
            lotteryLoader.setCancelable(false)

            doAsync {
                for (num in -1430..1430 step 10) {
                    val lotteryJSON = JSONObject(lottery(tz = num).toString())
                    Log.println(Log.DEBUG, "ACTION-LOTTERY", "TZ: $num / JSON: $lotteryJSON")
                    if (lotteryJSON.getString("api:statuscode") == "0") {
                        lotteryTimes += 1; coinsWon += lotteryJSON.getJSONObject("lotteryLog").getInt("awardValue")
                    } else {
                        lotteryTimes += 1
                    }
                    val percent = String.format("%.2f", (lotteryTimes.toFloat()/287)*100)
                    lotteryLoader.setMessage("Don't close the app while its playing the lottery!\n\nStatus: $lotteryTimes / 287 ($percent%)")
                }

                runOnUiThread {
                    lotteryLoader.setTitle("Completed playing the lottery!")
                    lotteryLoader.setMessage("You can now close this tab.\n\nCoins won $coinsWon out of $lotteryTimes plays")
                    lotteryLoader.setCancelable(true)
                }
            }

            lotteryLoader.show()
        }

        buttonUnfollow.setOnClickListener {
            val unfollowLoader = AlertDialog.Builder(this).create()
            unfollowLoader.setTitle("Unfollowing on '$COMMUNITY_NAME'")
            unfollowLoader.setMessage("Don't close the app while its unfollowing people!\n\nStatus: 0 / 100")
            unfollowLoader.setCancelable(false)

            val following = JSONObject(getUserFollowing().toString()).getJSONArray("userProfileList")

            doAsync {
                for (num in 0 until following.length()) {
                    val user = JSONObject(following[num].toString())

                    val unfollowUsername = user.getString("nickname")
                    val unfollowUid = user.getString("uid")

                    unfollowUser(unfollowUid)

                    unfollowLoader.setMessage("Don't close the app while its unfollowing people!\n\nUnfollowing: $unfollowUsername\nStatus: $num / ${following.length()}")

                    Log.println(Log.DEBUG, "ACTION-UNFOLLOW", "0/$num / JSON: $user")
                }

                runOnUiThread {
                    unfollowLoader.setTitle("Completed unfollowing!")
                    unfollowLoader.setMessage("You can now close this tab.")
                    unfollowLoader.setCancelable(true)
                }
            }

            unfollowLoader.show()
        }

        // Create Popup for ID Finder Action
        val idFinderPopup = PopupWindow(this)
        val idFinderWindow = layoutInflater.inflate(R.layout.action_id_finder, null)
        idFinderPopup.contentView = idFinderWindow

        // Set Id Finder Action Variables
        val idFinderInput = idFinderWindow.findViewById<EditText>(R.id.urlIdFinder)
        val idFinderSearch = idFinderWindow.findViewById<Button>(R.id.buttonIdFinderSearch)
        val idFinderClose = idFinderWindow.findViewById<Button>(R.id.buttonIdFinderClose)

        // Id Finder Boxes
        val infoIdNickname = idFinderWindow.findViewById<TextView>(R.id.infoIdNickname)
        val infoIdCommunity = idFinderWindow.findViewById<TextView>(R.id.infoIdCommunity)
        val infoIdObjectId = idFinderWindow.findViewById<TextView>(R.id.infoIdObjectId)

        // Id Finder Action
        buttonIdFinder.setOnClickListener {
            // Show Popup Menu
            idFinderPopup.isFocusable = true
            idFinderPopup.update()

            idFinderPopup.showAtLocation(idFinderWindow, Gravity.CENTER, 0, 0)
            idFinderWindow.layoutParams.height = (300 * windowScale).toInt()
            idFinderWindow.layoutParams.width = (320 * windowScale).toInt()

            idFinderWindow.isFocusable = true
        }

        // Id Finder Search for User
        idFinderSearch.setOnClickListener {
            val idFinderInputText = idFinderInput.text.toString()

            if (idFinderInputText != "") {
                var textSplit: Int
                var urlCode: String
                var urlType: String

                try {
                    textSplit = idFinderInput.text.split("/").lastIndex
                    urlCode = idFinderInput.text.split("/")[textSplit]
                    urlType = idFinderInput.text.split("/")[textSplit - 1]
                } catch (e: Exception) {
                    Toast.makeText(this, "Please enter an valid URL", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (urlType == "p") {
                    val findUrlCmd = findUrlCode(code = urlCode)
                    val findUrlJSON = JSONObject(findUrlCmd.toString())

                    val statusCode = findUrlJSON.getString("api:statuscode").toInt()
                    val statusMessage = findUrlJSON.getString("api:message").toString()

                    val info = findUrlJSON.getJSONObject("linkInfoV2").getJSONObject("extensions").getJSONObject("linkInfo")
                    val objectId = info.getString("objectId").toString()
                    val objectType = info.getString("objectType").toString()
                    val comId = info.getString("ndcId").toString()
                    val fullSplit = info.getString("fullPath").toString().split("/")
                    val userName = fullSplit.last()
                    val comName = fullSplit[0]

                    if (statusCode != 0) {
                        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show()
                    } else {
                        infoIdNickname.text = userName
                        infoIdCommunity.text = "$comName ($comId)"
                        infoIdObjectId.text = "$objectId ($objectType)"

                        val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        infoIdNickname.setOnClickListener {
                            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoIdNickname.text))
                            Toast.makeText(this, "Copied Nickname to Clipboard!", Toast.LENGTH_SHORT).show()
                        }

                        infoIdCommunity.setOnClickListener {
                            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoIdCommunity.text))
                            Toast.makeText(this, "Copied Community to Clipboard!", Toast.LENGTH_SHORT).show()
                        }

                        infoIdObjectId.setOnClickListener {
                            clipboard.setPrimaryClip(ClipData.newPlainText(null, objectId))
                            Toast.makeText(this, "Copied Object ID to Clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    val findUrlCmd = findUrlAminoId(id = urlCode)
                    val findUrlJSON = JSONObject(findUrlCmd.toString())

                    val statusCode = findUrlJSON.getString("api:statuscode").toInt()
                    val statusMessage = findUrlJSON.getString("api:message").toString()

                    val info = findUrlJSON.getJSONArray("resultList").getJSONObject(0)
                    val objectId = info.getString("objectId").toString()
                    val objectType = info.getString("objectType").toString()
                    val comId = info.getString("ndcId").toString()
                    val userName = info.getJSONObject("refObject").getString("nickname").toString()
                    val comName = "Global"

                    if (statusCode != 0) {
                        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show()
                    } else {
                        infoIdNickname.text = userName
                        infoIdCommunity.text = "$comName ($comId)"
                        infoIdObjectId.text = "$objectId ($objectType)"

                        val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        infoIdNickname.setOnClickListener {
                            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoIdNickname.text))
                            Toast.makeText(this, "Copied Nickname to Clipboard!", Toast.LENGTH_SHORT).show()
                        }

                        infoIdCommunity.setOnClickListener {
                            clipboard.setPrimaryClip(ClipData.newPlainText(null, infoIdCommunity.text))
                            Toast.makeText(this, "Copied Community to Clipboard!", Toast.LENGTH_SHORT).show()
                        }

                        infoIdObjectId.setOnClickListener {
                            clipboard.setPrimaryClip(ClipData.newPlainText(null, objectId))
                            Toast.makeText(this, "Copied Object ID to Clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter an URL", Toast.LENGTH_LONG).show()
            }
        }

        // Close Id Finder Action Box
        idFinderClose.setOnClickListener { idFinderPopup.dismiss() }

        // Create Popup for Start Chat Action
        val startChatPopup = PopupWindow(this)
        val startChatWindow = layoutInflater.inflate(R.layout.action_start_chat, null)
        startChatPopup.contentView = startChatWindow

        // Set Start Chat Action Variables
        val inputStartChat = startChatWindow.findViewById<EditText>(R.id.inputStartChat)
        val buttonStartChatSearch = startChatWindow.findViewById<Button>(R.id.buttonStartChatSearch)
        val buttonStartChatStart = startChatWindow.findViewById<Button>(R.id.buttonStartChatStart)
        val buttonStartChatClose = startChatWindow.findViewById<Button>(R.id.buttonStartChatClose)
        val checkStartChatBypass = startChatWindow.findViewById<CheckBox>(R.id.checkStartChatBypass)

        // Start Chat Boxes
        val startChatNickname = startChatWindow.findViewById<TextView>(R.id.infoIdNicknameStartChat)
        val startChatCommunity = startChatWindow.findViewById<TextView>(R.id.infoIdCommunityStartChat)
        val startChatObjectId = startChatWindow.findViewById<TextView>(R.id.infoIdObjectIdStartChat)

        // Start Chat Action
        buttonStartChat.setOnClickListener {
            // Show Popup Menu
            startChatPopup.isFocusable = true
            startChatPopup.update()

            startChatPopup.showAtLocation(startChatWindow, Gravity.CENTER, 0, 0)
            startChatWindow.layoutParams.height = (320 * windowScale).toInt()
            startChatWindow.layoutParams.width = (320 * windowScale).toInt()
            startChatWindow.isFocusable = true

            //if (WHITELISTED) { checkStartChatBypass.visibility = View.VISIBLE } else { }
        }

        var startChatObjectIdX = ""
        var startChatObjectTypeX = ""
        var startChatComIdX = ""

        buttonStartChatSearch.setOnClickListener {
            if (inputStartChat.text != null) {
                val textSplit = inputStartChat.text.split("/").lastIndex
                val urlCode = inputStartChat.text.split("/")[textSplit]
                val urlType = inputStartChat.text.split("/")[textSplit - 1]

                if (urlType == "p") {
                    val findUrlCmd = findUrlCode(code = urlCode)
                    val findUrlJSON = JSONObject(findUrlCmd.toString())

                    val statusCode = findUrlJSON.getString("api:statuscode").toInt()
                    val statusMessage = findUrlJSON.getString("api:message").toString()

                    val info: JSONObject

                    try {
                        info = findUrlJSON.getJSONObject("linkInfoV2").getJSONObject("extensions")
                            .getJSONObject("linkInfo")
                    } catch (e: JSONException) {
                        Toast.makeText(this, "User Code not Found!", Toast.LENGTH_LONG)
                            .show(); return@setOnClickListener
                    }

                    val objectId = info.getString("objectId").toString()
                    val objectType = info.getString("objectType").toString()
                    val comId = info.getString("ndcId").toString()
                    val fullSplit = info.getString("fullPath").toString().split("/")
                    val userName = fullSplit.last()
                    val comName = fullSplit[0]

                    if (statusCode != 0) {
                        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show()
                    } else {
                        startChatNickname.text = userName
                        startChatCommunity.text = "$comName ($comId)"
                        startChatObjectId.text = "$objectId ($objectType)"

                        startChatObjectIdX = objectId
                        startChatObjectTypeX = objectType
                        startChatComIdX = comId

                        val clipboard: ClipboardManager =
                            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        startChatNickname.setOnClickListener {
                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    null,
                                    startChatNickname.text
                                )
                            ); Toast.makeText(
                            this,
                            "Copied Nickname to Clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                        startChatCommunity.setOnClickListener {
                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    null,
                                    startChatCommunity.text
                                )
                            ); Toast.makeText(
                            this,
                            "Copied Community to Clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                        startChatObjectId.setOnClickListener {
                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    null,
                                    objectId
                                )
                            ); Toast.makeText(
                            this,
                            "Copied Object ID to Clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                    }

                } else {
                    val findUrlCmd = findUrlAminoId(id = urlCode)
                    val findUrlJSON = JSONObject(findUrlCmd.toString())

                    val statusCode = findUrlJSON.getString("api:statuscode").toInt()
                    val statusMessage = findUrlJSON.getString("api:message").toString()

                    val info: JSONObject

                    try {
                        info = findUrlJSON.getJSONArray("resultList").getJSONObject(0)
                    } catch (e: JSONException) {
                        Toast.makeText(this, "Amino ID not Found!", Toast.LENGTH_LONG)
                            .show(); return@setOnClickListener
                    }
                    val objectId = info.getString("objectId").toString()
                    val objectType = info.getString("objectType").toString()
                    val comId = info.getString("ndcId").toString()
                    val userName = info.getJSONObject("refObject").getString("nickname").toString()
                    val comName = "Global"

                    if (statusCode != 0) {
                        Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show()
                    } else {
                        startChatNickname.text = userName
                        startChatCommunity.text = "$comName ($comId)"
                        startChatObjectId.text = "$objectId ($objectType)"

                        startChatObjectIdX = objectId
                        startChatObjectTypeX = objectType
                        startChatComIdX = comId

                        val clipboard: ClipboardManager =
                            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        startChatNickname.setOnClickListener {
                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    null,
                                    startChatNickname.text
                                )
                            ); Toast.makeText(
                            this,
                            "Copied Nickname to Clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                        startChatCommunity.setOnClickListener {
                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    null,
                                    startChatCommunity.text
                                )
                            ); Toast.makeText(
                            this,
                            "Copied Community to Clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                        startChatObjectId.setOnClickListener {
                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    null,
                                    objectId
                                )
                            ); Toast.makeText(
                            this,
                            "Copied Object ID to Clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter an URL", Toast.LENGTH_LONG).show()
            }
        }

        buttonStartChatStart.setOnClickListener {
            if (startChatObjectTypeX.toInt() != 0) {
                Toast.makeText(this, "Please enter an valid User URL", Toast.LENGTH_LONG).show()
            } else {
                val startChatCmd = startChat(
                    userId = startChatObjectIdX,
                    comId = startChatComIdX,
                    bypass = checkStartChatBypass.isChecked
                )
                val startChatJSON = JSONObject(startChatCmd.toString())
                val statusMessage = startChatJSON.getString("api:message").toString()
                Toast.makeText(this, statusMessage, Toast.LENGTH_LONG).show()
            }
        }

        buttonStartChatClose.setOnClickListener { startChatPopup.dismiss() }

        buttonChatStart.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        // Create Popup for Send Coins Action
        val sendCoinsPopup = PopupWindow(this)
        val sendCoinsWindow = layoutInflater.inflate(R.layout.action_send_coins, null)
        sendCoinsPopup.contentView = sendCoinsWindow

        // Set Send Coins Action Variables
        val inputSendCoinsCoins = sendCoinsWindow.findViewById<EditText>(R.id.inputSendCoinsCoins)
        val sendCoinsSend = sendCoinsWindow.findViewById<Button>(R.id.buttonASendCoinsSend)
        val sendCoinsClose = sendCoinsWindow.findViewById<Button>(R.id.buttonASendCoinsClose)
        val selectChatsCoins = sendCoinsWindow.findViewById<Spinner>(R.id.spinnerChatsCoins)

        // Send Coins Action
        buttonCoinSender.setOnClickListener {
            // Show Popup Menu
            sendCoinsPopup.isFocusable = true
            sendCoinsPopup.update()

            sendCoinsPopup.showAtLocation(sendCoinsWindow, Gravity.CENTER, 0, 0)
            sendCoinsWindow.layoutParams.height = (250 * windowScale).toInt()
            sendCoinsWindow.layoutParams.width = (320 * windowScale).toInt()

            // Select Chats Spinner
            val chatObject = JSONObject(getChatList().toString())
            val jsonChat: JSONArray = chatObject.getJSONArray("threadList")
            var itemsTitle: Array<String> = arrayOf()
            var itemsId: Array<String> = arrayOf()

            // List Chats the User is in
            for (i in 0 until jsonChat.length()) {
                val item = jsonChat.getJSONObject(i)
                val titleObj = JSONObject(item.toString())

                val chatTitle = titleObj.getString("title")
                val chatId = titleObj.getString("threadId")

                itemsTitle = if (chatTitle == "null") { append(itemsTitle, chatId) } else { append(itemsTitle, chatTitle) }
                itemsId = append(itemsId, chatId)
            }

            // Fill the Dropdown Menu with the Chats Gathered
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, itemsTitle)
            selectChatsCoins.adapter = adapter

            // Send Message
            sendCoinsSend.setOnClickListener {
                val data = sendChatCoins(
                    coins = inputSendCoinsCoins.text.toString().toInt(),
                    chatId = itemsId[selectChatsCoins.selectedItemPosition]
                )

                val jsonData = JSONObject(data.toString())
                Toast.makeText(this, jsonData.getString("api:message").toString(), Toast.LENGTH_LONG).show()
            }

            // Close Send Message Action Box
            sendCoinsClose.setOnClickListener { sendCoinsPopup.dismiss() }
        }

        // Create Popup for Get Hidden Blogs Action
        val hiddenBlogsPopup = PopupWindow(this)
        val hiddenBlogsWindow = layoutInflater.inflate(R.layout.action_get_hidden_blogs, null)
        hiddenBlogsPopup.contentView = hiddenBlogsWindow

        buttonHiddenBlogs.setOnClickListener {
            actionLoading.visibility = View.VISIBLE

            val hiddenBlogsTitle: Array<String> = arrayOf()
            val hiddenBlogsID: Array<String> = arrayOf()

            Thread(MainActivity.ActivityHidden(this, hiddenBlogsPopup, hiddenBlogsWindow, windowScale, actionLoading, hiddenBlogsTitle, hiddenBlogsID)).start()
        }

        // Create Popup for Quiz Player Action
        val quizPlayerPopup = PopupWindow(this)
        val quizPlayerWindow = layoutInflater.inflate(R.layout.action_quiz_player, null)
        quizPlayerPopup.contentView = quizPlayerWindow

        // Set Quiz Player Action Variables
        val inputQuizPlayer = quizPlayerWindow.findViewById<EditText>(R.id.inputQuizPlayer)
        val buttonAQuizPlayerSend = quizPlayerWindow.findViewById<Button>(R.id.buttonAQuizPlayerSend)
        val buttonAQuizPlayerClose = quizPlayerWindow.findViewById<Button>(R.id.buttonAQuizPlayerClose)

        // Start Quiz Player Action
        buttonQuizPlayer.setOnClickListener {
            quizPlayerPopup.isFocusable = true
            quizPlayerPopup.update()

            quizPlayerPopup.showAtLocation(quizPlayerWindow, Gravity.CENTER, 0, 0)
            quizPlayerWindow.layoutParams.height = (250 * windowScale).toInt()
            quizPlayerWindow.layoutParams.width = (320 * windowScale).toInt()
            quizPlayerWindow.isFocusable = true
        }

        buttonAQuizPlayerSend.setOnClickListener {
            val inputQuizPlayerText = inputQuizPlayer.text.toString()

            if (inputQuizPlayerText == "") {
                Toast.makeText(this, "Please enter an valid URL", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            try {
                val target = JSONObject(findUrlCode(inputQuizPlayerText).toString()).getJSONObject("linkInfoV2").getJSONObject("extensions").getJSONObject("linkInfo").getString("objectId")
                val quiz = JSONObject(reviewQuizQuestions(target).toString()).getJSONObject("blog").getJSONArray("quizQuestionList")
                var final = JSONArray()

                for (q in 0 until quiz.length()) {
                    val item = quiz.getJSONObject(q)
                    val quizObj = JSONObject(item.toString())

                    val questionId = JSONObject(quizObj.toString()).getString("quizQuestionId")
                    val answers = JSONObject(quizObj.toString()).getJSONObject("extensions").getJSONArray("quizQuestionOptList")

                    for (answer in 0 until answers.length()) {
                        val answerItem = answers.getJSONObject(answer)
                        val answerObj = JSONObject(answerItem.toString())

                        val ans = JSONObject(answerObj.toString()).getString("optId")

                        var add = JSONObject()

                        var test = JSONArray()
                        test = test.put(ans)

                        add = add.put("optIdList", test)
                        add = add.put("quizQuestionId", questionId)
                        add = add.put("timeSpent", 0.0)

                        final = final.put(add)
                    }
                }

                val playQuiz = postQuiz(quizId = target, mode = 0, data = final)
                val playQuizHell = postQuiz(quizId = target, mode = 1, data = final)
                val playQuizJSON = JSONObject(playQuiz.toString())
                val playQuizHellJSON = JSONObject(playQuizHell.toString())
                val statusMessage = playQuizJSON.getString("api:message").toString()
                val statusMessageHell = playQuizHellJSON.getString("api:message").toString()
                Toast.makeText(this, "Normal: $statusMessage\nHell: $statusMessageHell", Toast.LENGTH_LONG).show()
            } catch (e: JSONException) {
                Toast.makeText(this, "Invalid Quiz URL ($e)", Toast.LENGTH_LONG).show()
            }
        }

        buttonAQuizPlayerClose.setOnClickListener { quizPlayerPopup.dismiss() }

        // Farmer Action
        buttonFarmer.setOnClickListener {
            var activityTimes = 0
            val farmerLoader = AlertDialog.Builder(this).create()
            farmerLoader.setTitle("Farming Coins on '$COMMUNITY_NAME'")
            farmerLoader.setMessage("Don't close the app while its farming coins!\n\nStatus: 0 / 70 (0.0%)")
            farmerLoader.setCancelable(false)

            doAsync {
                for (i in 0 until 50) {
                    val activityJSON = JSONObject(sendActivityObject(timestamp = System.currentTimeMillis()))
                    Log.println(Log.DEBUG, "ACTION-ACTIVITY", activityJSON.toString())
                    activityTimes += 1
                    val percent = String.format("%.2f", (activityTimes.toFloat()/70)*100)
                    farmerLoader.setMessage("Don't close the app while its farming coins!\n\nStatus: $activityTimes / 70 ($percent%)")
                }

                for (i in 0 until 20) {
                    val tapjoyJSON = sendTapJoy()
                    Log.println(Log.DEBUG, "ACTION-TAPJOY", tapjoyJSON.toString())
                    activityTimes += 1
                    val percent = String.format("%.2f", (activityTimes.toFloat()/70)*100)
                    farmerLoader.setMessage("Don't close the app while its farming coins!\n\nStatus: $activityTimes / 70 ($percent%)")
                }

                runOnUiThread {
                    farmerLoader.setTitle("Completed farming coins!")
                    farmerLoader.setMessage("You can now close this tab.\n\nYou may run this script multiple times per day\nSome of the coins may take a full day to receive!")
                    farmerLoader.setCancelable(true)
                }
            }
            farmerLoader.show()
        }
    }
}
