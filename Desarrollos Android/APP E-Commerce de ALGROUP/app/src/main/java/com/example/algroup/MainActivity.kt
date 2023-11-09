package com.example.algroup

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.ALGroup)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)

        // Configura la animación de la barra de carga
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animator.duration = 3000 // Duración de la animación en milisegundos (2 segundos en este ejemplo)
        animator.interpolator = LinearInterpolator() // Interpolator lineal para un movimiento uniforme
        animator.start()

        Thread.sleep(4000)

        startTimer()
    }

    private fun startTimer() {
        object : CountDownTimer(4000, 1000) {
            override fun onTick(p0: Long) {
                // Código a ejecutar en cada tick del temporizador (si es necesario)
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, login::class.java)
                startActivity(intent)
            }
        }.start()
    }

    override fun onBackPressed() {
    }
}
