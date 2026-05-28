package com.nyz.geosos

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var textViewContactInfo: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textViewContactInfo = findViewById(R.id.textViewContactInfo)

        findViewById<ImageButton>(R.id.imageButtonSetting).setOnClickListener {
            openConfigActivity()
        }

        sharedPreferences = getSharedPreferences("socorro", MODE_PRIVATE)

        initSetup()
    }

    private fun displayContactInfo() {
        val contactName = sharedPreferences.getString("contactName", "")
        val contactPhone = sharedPreferences.getString("contactPhone", "")
        textViewContactInfo.setText("$contactName | $contactPhone")
    }

    private fun initSetup() {
        if(sharedPreferences.contains("contactPhone")) {
            displayContactInfo()
        } else {
            val alertConfigInit = AlertDialog.Builder(this)
            alertConfigInit.setTitle("Bem vindo(a) ao Geo SOS!")
            alertConfigInit.setMessage("Faça a sua configuração para melhor desenpenho")
            alertConfigInit.setPositiveButton("Configurar agora") {dialog, which ->
                openConfigActivity()
            }
            alertConfigInit.create()
            alertConfigInit.show()
        }
    }

    private fun openConfigActivity() {
        val intent = Intent(this, ConfigActivity::class.java)
        startActivity(intent)
    }
}