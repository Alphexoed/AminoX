package com.slimakoi.aminox

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.runOnUiThread
import org.json.JSONObject

val client = OkHttpClient()

class ChatActivity : AppCompatActivity() {
    private class EchoWebSocketListener(cotx: Context, chatScroll: TextView): WebSocketListener() {
        val ctx = cotx
        val cScroll = chatScroll

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.println(Log.INFO, "WebSocket-onOpen", response.message)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.println(Log.INFO, "WebSocket-onMessage", text)

            var txt = JSONObject(text)

            if (txt.getInt("t") == 1000) {
                txt = txt.getJSONObject("o")

                val wsNdcId = txt.getInt("ndcId")
                val wsChat = txt.getJSONObject("chatMessage")

                val wsContent = wsChat.getString("content")
                val wsChatId = wsChat.getString("threadId")
                val wsAuthor = wsChat.getJSONObject("author").getString("nickname")

                val wsChatInfo = getChatInfo(comId = wsNdcId.toString(), chatId = wsChatId)
                val wsChatTitle = wsChatInfo.getJSONObject("thread").getString("title").toString()
                val wsChatName = if (wsChatTitle == "null") { wsChatId } else { wsChatTitle }

                val final = "[$wsNdcId][$wsChatName] $wsAuthor: $wsContent\n"

                ctx.runOnUiThread {
                    cScroll.append(final)
                }
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            webSocket.close(1000, null)
            Log.println(Log.INFO, "WebSocket-onClosing-$code", reason)
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR

        setContentView(R.layout.activity_chat)

        Toast.makeText(this, "This is a beta feature, it might have bugs!", Toast.LENGTH_LONG).show()

        val final = "$deviceId|${System.currentTimeMillis()}"

        val request: Request = Request.Builder()
            .url("wss://ws1.narvii.com?signbody=${final.replace("|", "%7C")}")
            .header("NDCDEVICEID", deviceId)
            .header("NDCAUTH", "sid=$SID")
            .header("NDC-MSG-SIG", ndcMsgSig(final))
            .build()
        val listener = EchoWebSocketListener(this, findViewById(R.id.chatScroll))
        var ws: WebSocket

        doAsync {
            while (true) {
                ws = client.newWebSocket(request, listener)
                println("DONE RESTARTING...")
                Thread.sleep(30000)
                println("RESTARTING...")
                ws.close(1000, null)
                println("RESTARTED!")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Exited Live Chat!", Toast.LENGTH_SHORT).show()

        finish()
    }
}