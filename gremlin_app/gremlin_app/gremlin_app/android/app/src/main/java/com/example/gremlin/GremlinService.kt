package com.example.gremlin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class GremlinService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val chaosRunnable = object : Runnable {
        override fun run() {
            // Every 1 minute (for demo purposes), do something chaotic
            // In real app: 4 hours
            if (Math.random() > 0.7) {
                Toast.makeText(applicationContext, "Gremlin is watching you.", Toast.LENGTH_SHORT).show()
            }
            handler.postDelayed(this, 60000)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(chaosRunnable)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(chaosRunnable)
    }
}
