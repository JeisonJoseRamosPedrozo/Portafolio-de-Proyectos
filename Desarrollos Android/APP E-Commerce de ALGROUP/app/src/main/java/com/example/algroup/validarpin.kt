package com.example.algroup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class validarpin : AppCompatActivity() {
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var contadorRegresivoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validarpin)

        val code = findViewById<EditText>(R.id.numAleatorio)
        val campoPin = findViewById<EditText>(R.id.campoPin)
        contadorRegresivoText = findViewById<TextView>(R.id.cuentaregresiva) // Declara la variable aquí
        val buttonValidar = findViewById<Button>(R.id.validar)

        val num1 = (Math.random() * 10).toInt().toString()
        val num2 = (Math.random() * 10).toInt().toString()
        val num3 = (Math.random() * 10).toInt().toString()
        val num4 = (Math.random() * 10).toInt().toString()
        val num5 = (Math.random() * 10).toInt().toString()
        val variables = num1 + num2 + num3 + num4 + num5

        val editableValue: Editable = Editable.Factory.getInstance().newEditable(variables)

        code.text = editableValue

        buttonValidar.setOnClickListener {
            val pin = campoPin.text.toString()
            val codigoGenerado = code.text.toString()
            if (pin.isEmpty()) {
                Toast.makeText(this, "Digita el código de validación (!).", Toast.LENGTH_SHORT).show()
            } else if (pin.toInt() != codigoGenerado.toInt()) {
                Toast.makeText(this, "Pin incorrecto, vuelve a intentarlo", Toast.LENGTH_SHORT).show()
            } else {
                countdownTimer.cancel() // Detener el contador regresivo
                val menuPass = Intent(this, menu::class.java)
                startActivity(menuPass)
            }
        }

        iniciarContadorRegresivo()
    }

    private fun iniciarContadorRegresivo() {
        countdownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                contadorRegresivoText.text = "El pin caducará en $secondsLeft segundos"
            }

            override fun onFinish() {
                Toast.makeText(applicationContext, "El pin ha caducado", Toast.LENGTH_SHORT).show()
                val loginReturn = Intent(applicationContext, login::class.java)
                startActivity(loginReturn)
            }
        }

        countdownTimer.start()
    }

    override fun onBackPressed() {
    }
}
