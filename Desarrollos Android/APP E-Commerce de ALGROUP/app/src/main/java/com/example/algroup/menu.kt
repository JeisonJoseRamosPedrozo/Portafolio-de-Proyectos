package com.example.algroup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val textViewLink = findViewById<TextView>(R.id.textViewLink)
        val imgcombustible = findViewById<ImageView>(R.id.imgcombustible)
        val imgaceite = findViewById<ImageView>(R.id.imgaceite)
        val imgaire = findViewById<ImageView>(R.id.imgaire)
        val imgcabina = findViewById<ImageView>(R.id.imgcabina)
        val carrito = findViewById<ImageView>(R.id.cart)
        val close = findViewById<ImageView>(R.id.cerrarsesion)

        textViewLink.setOnClickListener{
            val url = "https://algroup.com/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        imgcombustible.setOnClickListener {
            val filtrosCombustiblePass = Intent(this, filtrosCombustible::class.java)
            startActivity(filtrosCombustiblePass)
        }

        imgaceite.setOnClickListener {
            val filtrosAceitePass = Intent(this, filtrosAceite::class.java)
            startActivity(filtrosAceitePass)
        }

        imgaire.setOnClickListener {
            val filtrosAirePass = Intent(this, filtrosAire::class.java)
            startActivity(filtrosAirePass)
        }

        imgcabina.setOnClickListener {
            val filtrosCabinaPass = Intent(this, filtrosCabina::class.java)
            startActivity(filtrosCabinaPass)
        }

        carrito.setOnClickListener {
            val shoppingCartPass = Intent(this, carritoDeCompras::class.java)
            startActivity(shoppingCartPass)
        }

        close.setOnClickListener {
            Thread.sleep(1000)
            Toast.makeText(this, "Sesión Finalizada", Toast.LENGTH_LONG).show()
            val loginPass = Intent(this, login::class.java)
            startActivity(loginPass)
        }
    }

    override fun onBackPressed() {
    }
}
