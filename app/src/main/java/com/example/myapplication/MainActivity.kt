
package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(),GameListener {
    lateinit var start : Button
    lateinit var left : Button
    lateinit var right : Button

    lateinit var alienView : Aliens
<<<<<<< HEAD
=======
    private lateinit var Jeux : jeux
    lateinit var AlienView : AlienView

>>>>>>> 449a49c36fd2996c9681b31853f2b40ab134ad98

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

        start = findViewById(R.id.button_start)
        left = findViewById(R.id.button_left)
        left.visibility = View.INVISIBLE // on désactive a chaque fois les 4 boutons de déplacement car on ne veut pas les voirent quand le jeu se lance, il faut les activer uniquement quand on clique sur le bouton start
        right = findViewById(R.id.button_right)
        right.visibility = View.INVISIBLE

<<<<<<< HEAD
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
=======
        val AlienView=AlienView(this)
        val JoueurView = JoueurView(this)
        Jeux = jeux(
            context = this,
            attributes = null,
            defStyleAttr = 0,
            left = left,
            right = right,
            GameListener = this
        )



        start.setOnClickListener{

            setContentView(AlienView)
            setContentView(JoueurView)
>>>>>>> 449a49c36fd2996c9681b31853f2b40ab134ad98
            Jeux.start_game()
            Jeux.resume()
        }
    }
<<<<<<< HEAD
    /*override fun Pause() {
        super.Pause()
        Jeux.pause()
=======

    override fun NoAliens(){
        setContentView(AlienView)
    }




>>>>>>> 449a49c36fd2996c9681b31853f2b40ab134ad98
    override fun onPause() {
        super.onPause()
        Jeux.pause()
    }
    override fun onResume() {
        super.onResume()
<<<<<<< HEAD
        alienView.resume()*/
=======
        Jeux.resume()
>>>>>>> 449a49c36fd2996c9681b31853f2b40ab134ad98
    }

