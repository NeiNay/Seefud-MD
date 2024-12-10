package com.seefud.seefud.view.authentication.profil

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.seefud.seefud.R

class MyAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_account)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        val btnToProfile: ImageButton = findViewById(R.id.btnToProfile)
//        btnToProfile.setOnClickListener {
//            // Intent untuk navigasi ke ProfilActivity
//            val intent = Intent(this, ProfilActivity::class.java)
//            startActivity(intent)
//        }
    }
}