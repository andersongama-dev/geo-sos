package com.nyz.geosos

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.telephony.SmsManager

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
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
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

            val phone = sharedPreferences.getString("contactPhone", "")

            if (phone.isNullOrBlank()) {
                initSetup()
                return@setOnClickListener
            }

            if (
                checkAndRequestPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkAndRequestPermission(Manifest.permission.SEND_SMS)
            ) {

                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

                    if (location != null) {

                        val lat = location.latitude
                        val lng = location.longitude

                        val customMsg = sharedPreferences.getString(
                            "contactMsg",
                            "SOS! Preciso de ajuda."
                        )

                        val message = """
                        $customMsg
                        
                        Minha localização:
                        https://maps.google.com/?q=$lat,$lng
                        """.trimIndent()

                        try {
                            val smsManager = getSystemService(SmsManager::class.java)

                            smsManager.sendTextMessage(
                                phone,
                                null,
                                message,
                                null,
                                null
                            )

                            Toast.makeText(
                                this,
                                "SMS enviado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()

                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                "Erro ao enviar SMS: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            this,
                            "Não foi possível obter a localização.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

            initSetup()
        }

    override fun onResume() {
        super.onResume()
        initSetup()
    }

    private fun displayContactInfo() {
        val contactName = sharedPreferences.getString("contactName", "")
        val contactPhone = sharedPreferences.getString("contactPhone", "")

        textViewContactInfo.text = "$contactName | $contactPhone"
    }

    private fun initSetup() {
        val phone = sharedPreferences.getString("contactPhone", "")

        if (!phone.isNullOrBlank()) {
            displayContactInfo()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Bem-vindo(a) ao Geo SOS!")
                .setMessage("Faça a configuração inicial para utilizar o aplicativo.")
                .setCancelable(false)
                .setPositiveButton("Configurar agora") { _, _ ->
                    openSettingsActivity()
                }
                .show()
        }
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, ConfigActivity::class.java))
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