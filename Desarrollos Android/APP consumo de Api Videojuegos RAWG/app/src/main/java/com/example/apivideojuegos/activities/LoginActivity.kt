package com.example.apivideojuegos.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.apivideojuegos.MainActivity
import com.example.apivideojuegos.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setup()
    }

    private fun setup() {

        val tvNotAccount = findViewById<TextView>(R.id.tvDontHaveAccount)
        val btnLogin = findViewById<Button>(R.id.buttonLoginLogin)
        val etEmail = findViewById<EditText>(R.id.edEmailLogin)
        val etPassword = findViewById<EditText>(R.id.edPasswordLogin)

        tvNotAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{
            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome()
                        }else {
                            showAlert()
                        }
                    }


            }
        }
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al iniciar sesion.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
    }
}