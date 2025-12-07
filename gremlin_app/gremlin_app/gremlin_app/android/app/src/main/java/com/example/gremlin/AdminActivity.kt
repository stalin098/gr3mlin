package com.example.gremlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val tvMetrics = findViewById<TextView>(R.id.tvMetrics)
        
        // In a real app, fetch from API
        tvMetrics.text = """
            Total Messages: 42
            Chaos Level: 85%
            User Sentiment: Confused
            
            Logs:
            [10:00] User: Hello -> Gremlin: bruh.
            [10:05] User: Help -> Gremlin: skill issue.
        """.trimIndent()
    }
}
