package com.nyz.geosos

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfigActivity : AppCompatActivity() {

    private lateinit var editTextContactName: EditText
    private lateinit var editTextContactPhone: EditText
    private lateinit var editTextContactMsg: EditText

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_config)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // SharedPreferences
        sharedPreferences = getSharedPreferences("socorro", MODE_PRIVATE)

        // EditTexts
        editTextContactName = findViewById(R.id.TextInputEditTextContactName)
        editTextContactPhone = findViewById(R.id.TextInputEditTextContactPhone)
        editTextContactMsg = findViewById(R.id.TextInputEditTextCustomMsg)

        loadPreferences()

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            println("è muito massa")
            savePreferences(
                editTextContactName.toString(),
                editTextContactPhone.toString(),
                editTextContactMsg.toString()
            )

        }
    }

    private fun savePreferences(name: String, phone: String, msg: String) {
        sharedPreferences.edit().putString("contactName", name)
            .putString("contactPhone", phone)
            .putString("contactMsg", msg)
            .apply()

        Toast.makeText(this, "Contato salvo com sucesso", Toast.LENGTH_SHORT).show()
    }

    private fun clearPreferences() {
        sharedPreferences.edit().clear().apply()

        Toast.makeText(this, "Contanto excluido com sucesso", Toast.LENGTH_SHORT).show()
        loadPreferences()
    }

    private fun loadPreferences() {
        editTextContactName.setText(sharedPreferences.getString("contactName", ""))
        editTextContactPhone.setText(sharedPreferences.getString("contactPhone", ""))
        editTextContactMsg.setText(sharedPreferences.getString("contactMsg", ""))
    }
}