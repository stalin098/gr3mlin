package com.example.gremlin

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gremlin.api.ApiClient
import com.example.gremlin.api.ChatRequest
import com.example.gremlin.api.ChatResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var rvChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var fabSummon: FloatingActionButton
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvChat = findViewById(R.id.rvChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        fabSummon = findViewById(R.id.fabSummon)
        tvTitle = findViewById(R.id.tvTitle)

        chatAdapter = ChatAdapter(mutableListOf())
        rvChat.adapter = chatAdapter
        rvChat.layoutManager = LinearLayoutManager(this)

        btnSend.setOnClickListener {
            val msg = etMessage.text.toString()
            if (msg.isNotEmpty()) {
                sendMessage(msg)
                etMessage.text.clear()
            }
        }

        fabSummon.setOnClickListener {
            checkOverlayPermission()
        }

        // Secret Admin Gesture (Triple Tap Title)
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                // Using double tap + click logic or just count clicks manually
                return true
            }
        })
        
        var titleClicks = 0
        tvTitle.setOnClickListener {
            titleClicks++
            if (titleClicks >= 3) {
                startActivity(Intent(this, AdminActivity::class.java))
                titleClicks = 0
            }
        }

        // Start background service
        startService(Intent(this, GremlinService::class.java))
    }

    private fun sendMessage(text: String) {
        chatAdapter.addMessage(Message(text, true))
        rvChat.scrollToPosition(chatAdapter.itemCount - 1)

        ApiClient.instance.chat(ChatRequest(text, 0.5f)).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    val reply = response.body()?.response ?: "..."
                    runOnUiThread {
                        chatAdapter.addMessage(Message(reply, false))
                        rvChat.scrollToPosition(chatAdapter.itemCount - 1)
                    }
                } else {
                    showToast("Gremlin is ignoring you (Server Error)")
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                showToast("Gremlin is sleeping (Network Error)")
            }
        })
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                startActivityForResult(intent, 100)
            } else {
                startFloatingWidget()
            }
        } else {
            startFloatingWidget()
        }
    }

    private fun startFloatingWidget() {
        startService(Intent(this, FloatingWidgetService::class.java))
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
