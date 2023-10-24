package com.example.snake6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.snake6.R

class AboutAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val btn_back = findViewById<Button>(R.id.btn_back)
        btn_back.setOnClickListener{
            finish()
        }
    }
}
