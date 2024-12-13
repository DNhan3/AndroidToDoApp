package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.details_activity)

        val Bbutton = findViewById<Button>(R.id.backbutton)

        Bbutton.setOnClickListener {
            val intent = Intent(this@DetailsActivity, MainActivity::class.java)
            startActivity(intent)
        }

        val testView = findViewById<TextView>(R.id.textView)
        val text = intent.getStringExtra("des")
        testView.setText(text)

    }
}