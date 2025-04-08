
package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var start : Button
    lateinit var left : Button
    lateinit var right : Button

    lateinit var alienView : Aliens

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
        val Jeux = jeux()
        start = findViewById(R.id.button_start)
        left = findViewById(R.id.button_left)
        left.visibility = View.INVISIBLE // on désactive a chaque fois les 4 boutons de déplacement car on ne veut pas les voirent quand le jeu se lance, il faut les activer uniquement quand on clique sur le bouton start
        right = findViewById(R.id.button_right)
        right.visibility = View.INVISIBLE

        val alienView=AlienView(this)
        val joueurView=JoueurView(this )
        start.setOnClickListener{

            setContentView(alienView)
            //ajoute joueurView au Layout principal (AlienView dans ce cas )
            alienView.addView(joueurView)
            //activer les boutons  de deplacement apres le clic  sur start
            left.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
            // configurer les boutons
            left.setOnClickListener { joueurView.deplacement("LEFT") }
            right.setOnClickListener { joueurView.deplacement("RIGHT") }
            Jeux.start_game()
        }
    }
    /*override fun Pause() {
        super.Pause()
        Jeux.pause()
    override fun onPause() {
        super.onPause()
        alienView.pause()
    }
    override fun onResume() {
        super.onResume()
        alienView.resume()*/
    }

