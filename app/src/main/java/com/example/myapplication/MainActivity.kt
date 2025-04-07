
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

    lateinit var AlienView : AlienView
    lateinit var vaisseau : joueur

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

        val AlienView=AlienView(this)
        val vaisseau = joueur()




        start.setOnClickListener{

            setContentView(AlienView)
            vaisseau.dessin(this)
            Jeux.start_game()
        }
    }
    override fun Pause() {
        super.Pause()
        Jeux.pause()
    override fun onPause() {
        super.onPause()
        alienView.pause()
    }
    override fun onResume() {
        super.onResume()
        alienView.resume()
    }
}
