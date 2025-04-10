
package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(),GameListener {
    lateinit var start: Button
    lateinit var left: Button
    lateinit var right: Button

    lateinit var alienView: Aliens
    private lateinit var Jeux: jeux
    lateinit var score: TextView
    lateinit var AlienView: AlienView
    lateinit var JoueurView : JoueurView
    lateinit var Joueur: ImageView
    val maxTranslationX = 500f
    val minTranslationX = -500f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alien_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        alienView = findViewById(R.id.alien_view)
        score = findViewById(R.id.score)

        start = findViewById(R.id.button_start)
        left = findViewById(R.id.button_left)
        left.visibility =
            View.INVISIBLE // on désactive a chaque fois les 4 boutons de déplacement car on ne veut pas les voirent quand le jeu se lance, il faut les activer uniquement quand on clique sur le bouton start
        right = findViewById(R.id.button_right)
        right.visibility = View.INVISIBLE
        score.visibility = View.INVISIBLE
        Joueur = findViewById(R.id.imageView)
        right = findViewById(R.id.button_right)
        left = findViewById(R.id.button_left)

        val alienView = AlienView(this)
        val joueurView = JoueurView(this)
        start.setOnClickListener {
        }
        Jeux = jeux(
            context = this,
            attributes = null,
            defStyleAttr = 0,
            left = left,
            right = right,
            GameListener = this  ,
            mainactivity = this
        )

        fun updateScore() {
            score.text = "Score: $score"
        }

        start.setOnClickListener {
            score.visibility = View.VISIBLE
            setContentView(AlienView)
            setContentView(JoueurView)
            Jeux.start_game()
            Jeux.resume()
        }

        right.setOnClickListener {
            val newTranslationX = Joueur.translationX + 10f
            if (newTranslationX <= maxTranslationX){
                Joueur.translationX = newTranslationX
            }
        }
        left.setOnClickListener {
            val newTranslationX = (Joueur).translationX - 10f
            if (newTranslationX >= minTranslationX){
                Joueur.translationX = newTranslationX
            }
        }
    }
    override fun updateScore() {
        score.text = "Score: $score"
    }
    override fun NoAliens() {
        setContentView(AlienView)
    }

    override fun onPause() {
        super.onPause()
        Jeux.pause()
    }

    override fun onResume() {
        super.onResume()

        Jeux.resume()
    }

}

