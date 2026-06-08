package com.nyz.geosos

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var textViewContactInfo: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializações
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferences = getSharedPreferences("socorro", MODE_PRIVATE)
        textViewContactInfo = findViewById(R.id.textViewContactInfo)

        // Botão Config
        findViewById<Button>(R.id.imageButtonSetting).setOnClickListener {
            openSettingsActivity()
        }

        // Botão SOS
        findViewById<Button>(R.id.buttonSos).setOnClickListener {

            if (checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {

                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {

                        val lat = location.latitude
                        val lng = location.longitude
                        Toast(this).setText(lat.toString())
                    }
                }
            }

            initSetup()
        }

        initSetup()
    }

    private fun displayContactInfo() {
        val contactName = sharedPreferences.getString("contactName", "")
        val contactPhone = sharedPreferences.getString("contactPhone", "")
        textViewContactInfo.text = "$contactName | $contactPhone"
    }

    private fun initSetup() {
        if (sharedPreferences.contains("contactPhone")) {
            displayContactInfo()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Bem vindo(a) ao Geo SOS!")
                .setMessage("Faça a sua configuração para melhor desempenho")
                .setPositiveButton("Configurar agora") { _, _ ->
                    openSettingsActivity()
                }
                .show()
        }
    }

    private fun openSettingsActivity() {
        val intent = Intent(this, ConfigActivity::class.java)
        startActivity(intent)
    }

    private fun checkAndRequestPermission(permission: String): Boolean {
        return if (ActivityCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            false
        } else {
            true
        }
    }
}