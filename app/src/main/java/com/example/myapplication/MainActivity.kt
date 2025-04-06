
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
    lateinit var up : Button
    lateinit var down : Button
    lateinit var fire : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Jeux = jeux()
        start = findViewById(R.id.button_start)
        left = findViewById(R.id.button_left)
        left.visibility = View.INVISIBLE // on désactive a chaque fois les 4 boutons de déplacement car on ne veut pas les voirent quand le jeu se lance, il faut les activer uniquement quand on clique sur le bouton start
        right = findViewById(R.id.button_right)
        right.visibility = View.INVISIBLE
        up = findViewById(R.id.button_up)
        up.visibility = View.INVISIBLE
        down = findViewById(R.id.button_down)
        up.visibility = View.INVISIBLE
        fire = findViewById(R.id.button_fire)
        val AlienView=AlienView(this)
        val vaisseau = joueur()



        start.setOnClickListener{
            left.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
            up.visibility = View.VISIBLE
            down.visibility = View.VISIBLE
            setContentView(AlienView)
            vaisseau.dessin(this)
            Jeux.start_game()
        }
    }
    /*override fun Pause() {
        super.Pause()
        Jeux.pause()
    }
    override fun Reprendre() {
        super.Reprendre()
        Jeux.reprendre()
    }

     */

}

//test pour le git pull
// test kevin
// test De Vinci 
