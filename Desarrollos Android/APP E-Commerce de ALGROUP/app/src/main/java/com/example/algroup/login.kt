package com.example.algroup

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog

class login : AppCompatActivity() {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        imageView = findViewById(R.id.login)

        val scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 0.5f, 1.0f)
        val scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0.5f, 1.0f)

        scaleXAnimator.duration = 2000
        scaleYAnimator.duration = 2000

        scaleXAnimator.start()
        scaleYAnimator.start()

        val buttonIniciarSesion = findViewById<Button>(R.id.iniciarsesion)
        val buttonRegistrarse = findViewById<Button>(R.id.registrarse)

        buttonIniciarSesion.setOnClickListener{
            val iniciarSesionPass = Intent(this, iniciarsesion::class.java)
            startActivity(iniciarSesionPass)
        }


        buttonRegistrarse.setOnClickListener{
            val registrarsePass= Intent(this, registrarse::class.java)
            startActivity(registrarsePass)
        }
    }

    override fun onBackPressed() {
        preguntar()
    }

    private fun preguntar(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.cerrar, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = builder.create()
        dialog.show()

        val cerrar = dialogView.findViewById<Button>(R.id.si)
        val mantener = dialogView.findViewById<Button>(R.id.no)

        cerrar.setOnClickListener {
            Thread.sleep(500)
            finishAffinity()
        }

        mantener.setOnClickListener {
            dialog.dismiss()
        }
    }
}





