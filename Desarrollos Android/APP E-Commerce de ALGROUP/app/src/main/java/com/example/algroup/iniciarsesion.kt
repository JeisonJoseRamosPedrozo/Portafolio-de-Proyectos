package com.example.algroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class iniciarsesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciarsesion)

        val contrasena = findViewById<EditText>(R.id.passwordInicioSesion)
        val telefono = findViewById<EditText>(R.id.phoneInicioSesion)
        val validacionPin = findViewById<Button>(R.id.generarPin)

        validacionPin.setOnClickListener{
            val password = contrasena.text.toString()
            val phone = telefono.text.toString()
            if (password.isEmpty() || phone.isEmpty()){
                Toast.makeText(this, "Por favor, rellene todos los datos.", Toast.LENGTH_SHORT).show()
            } else if (phone.length != 10) {
                Toast.makeText(this, "El número de teléfono debe ser exactamente de 10 dígitos.", Toast.LENGTH_SHORT).show()
            } else {
                // Realizar una consulta a la base de datos para verificar las credenciales
                val con = SQLite(this, "algroup", null, 1)
                val baseDatos = con.readableDatabase

                val consulta = "SELECT * FROM usuarios WHERE celular = ? AND contrasena = ?"
                val parametros = arrayOf(phone, password)

                val cursor = baseDatos.rawQuery(consulta, parametros)


                if (cursor.moveToFirst()) {
                    val nombreUsuario = cursor.getString(cursor.run { getColumnIndex("nombre") })
                    val sharedPreferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("nombre", nombreUsuario)
                    editor.apply()
                    // Las credenciales son válidas, iniciar la actividad de validación de pin
                    val validacionPinPass = Intent(this, validarpin::class.java)
                    startActivity(validacionPinPass)
                } else {
                    // Las credenciales no coinciden con ningún registro en la base de datos
                    Toast.makeText(this, "Credenciales incorrectas, vuelve a intentarlo.", Toast.LENGTH_SHORT).show()
                }

                cursor.close()
                baseDatos.close()
            }
        }
    }
}
