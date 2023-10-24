package com.example.snake6

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.snake6.PermissionUtils.hasPermissions
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList


var filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
var Score = 0
lateinit var score_txtVeiw : TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


        if (!hasPermissions(this)) {
            PermissionUtils.requestPermissions(this, 0)
        }

        val btn_start_act = findViewById<Button>(R.id.btn_start_act)
        btn_start_act.setOnClickListener(this)
        val btn_about = findViewById<Button>(R.id.btn_about)
        btn_about.setOnClickListener(this)

        score_txtVeiw = findViewById(R.id.Score)
        val path = Paths.get("$filePath/Score.txt")
        if (Files.exists(path)) {
            var s = Files.lines(path, Charsets.UTF_8).toList()
            Score = s[0].toInt()
        }
        else{
            val file = File(filePath, "Score.txt")
            file.writeText("0")
        }
        score_txtVeiw.text = "Best score: " + Score
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1){
            score_txtVeiw.text = "Best score: " + Score
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.btn_start_act -> {
                val intent = Intent(this, GaameAct::class.java)
                this.startActivityForResult(intent, 1)
            }
            R.id.btn_about -> {
                val intent = Intent(this, AboutAct::class.java)
                this.startActivity(intent)
            }
        }
    }
}