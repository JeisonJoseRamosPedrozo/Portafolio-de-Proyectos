package com.example.algroup

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class registrarse : AppCompatActivity() {

    var nombre: EditText? = null
    var contrasena: EditText? = null
    var telefono: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        nombre = findViewById(R.id.usernameRegistro)
        contrasena = findViewById(R.id.passwordRegistro)
        telefono = findViewById(R.id.phoneRegistro)
        val confirmarRegistro = findViewById<Button>(R.id.confirmar1)

        confirmarRegistro.setOnClickListener {
            val con = SQLite(this, "algroup", null, 1)
            val baseDatos = con.writableDatabase

            val name = nombre?.text.toString()
            val password = contrasena?.text.toString()
            val phone = telefono?.text.toString()

            if (name.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los datos.", Toast.LENGTH_SHORT).show()
            } else if (phone.length != 10) {
                Toast.makeText(this, "El número de teléfono debe ser exactamente de 10 dígitos.", Toast.LENGTH_SHORT).show()
            } else {
                // Verificar si el nombre ya existe en la tabla "usuarios"
                val consulta = "SELECT nombre FROM usuarios WHERE nombre = ?"
                val parametros = arrayOf(name)
                val cursor = baseDatos.rawQuery(consulta, parametros)

                if (cursor.count > 0) {
                    // El nombre ya existe en la base de datos, mostrar un mensaje de error
                    Toast.makeText(this, "El nombre ya está registrado. Por favor, elige otro nombre.", Toast.LENGTH_SHORT).show()
                } else {
                    // El nombre no existe en la base de datos, proceder con el registro
                    val registro = ContentValues()
                    registro.put("nombre", name)
                    registro.put("contrasena", password)
                    registro.put("celular", phone)
                    baseDatos.insert("usuarios", null, registro)

                    // Limpiar los campos de entrada
                    nombre?.setText("")
                    contrasena?.setText("")
                    telefono?.setText("")

                    val confirmarRegistroPass = Intent(this, iniciarsesion::class.java)
                    startActivity(confirmarRegistroPass)
                }

                cursor.close()
            }
        }

    }
}
