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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setup()
    }

    private fun setup() {

        val tvHaveAccount = findViewById<TextView>(R.id.tvDoYouHaveAccount)
        val btnRegister = findViewById<Button>(R.id.buttonRegisterRegister)
        val etEmail = findViewById<EditText>(R.id.edEmailRegister)
        val etPassword = findViewById<EditText>(R.id.edPasswordRegister)

        tvHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener{
            if (etEmail.text.isNotEmpty() && etPassword.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString()).addOnCompleteListener {

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
        builder.setMessage("Error al registrar el usuario.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome() {

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}