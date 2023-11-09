package com.example.apivideojuegos

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import com.example.apivideojuegos.activities.LoginActivity

class splash : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ApiVideojuegos)
        setContentView(R.layout.pantalladecarga)

        progressBar = findViewById(R.id.progressBar2)

        // Configura la animación de la barra de carga
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animator.duration = 6000 // Duración de la animación en milisegundos (2 segundos en este ejemplo)
        animator.interpolator = LinearInterpolator() // Interpolator lineal para un movimiento uniforme
        animator.start()

        Thread.sleep(4000)

        startTimer()
    }

    private fun startTimer() {
        object : CountDownTimer(6000, 1000) {
            override fun onTick(p0: Long) {
                // Código a ejecutar en cada tick del temporizador (si es necesario)
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }.start()
    }

    override fun onBackPressed() {
    }
}
