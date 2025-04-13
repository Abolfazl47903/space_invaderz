package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity(), GameListener {
    lateinit var alienView: AlienView
    private lateinit var jeux: jeux
    lateinit var start: Button
    lateinit var left: Button
    lateinit var right: Button
    lateinit var joueur: ImageView

    val maxTranslationX = 500f
    val minTranslationX = -500f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Récupérer les vues à partir du layout
        alienView = findViewById(R.id.alien_view)
        start = findViewById(R.id.button_start)
        left = findViewById(R.id.button_left)
        right = findViewById(R.id.button_right)
        joueur = findViewById(R.id.imageView)


        // Garder les boutons invisibles au début
        left.visibility = View.INVISIBLE
        right.visibility = View.INVISIBLE

        // Appliquer éventuellement des insets pour la vue alienView
        ViewCompat.setOnApplyWindowInsetsListener(alienView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialisation du jeu
        jeux = jeux(
            context = this,
            attributes = null,
            defStyleAttr = 0,
            left = left,
            right = right,
            GameListener = this
        )
        // Déplacement du vaisseau à gauche
        right.setOnClickListener {
            val newTranslationX = joueur.translationX + 10f
            if (newTranslationX <= maxTranslationX) {
                joueur.translationX = newTranslationX
                android.util.Log.d("MainActivity", "Déplacement à droite : ${joueur.translationX}")
            } else {
                android.util.Log.d("MainActivity", "Limite droite atteinte")
            }
        }

        // Déplacement du vaisseau à droite
        left.setOnClickListener {
            val newTranslationX = joueur.translationX - 10f
            if (newTranslationX >= minTranslationX) {
                joueur.translationX = newTranslationX
                android.util.Log.d("MainActivity", "Déplacement à gauche : ${joueur.translationX}")
            } else {
                android.util.Log.d("MainActivity", "Limite gauche atteinte")
            }
        }

        // Configure le bouton Start
        start.setOnClickListener {
            // Quand le bouton Start est cliqué, démarrer le jeu et les mouvements des aliens
            alienView.startMovement()  // Lancer le mouvement des aliens
            jeux.start_game()          // Démarre le jeu
            jeux.resume()              // Reprend le jeu si nécessaire

            // Rendre les boutons de déplacement visibles après Start
            left.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
            start.visibility = View.GONE

        }


    }

    override fun NoAliens() {
        // Aucun changement à apporter ici pour la visibilité des boutons
        alienView.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        jeux.pause()
    }

    override fun onResume() {
        super.onResume()
        jeux.resume()
    }
}





package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity(), GameListener {
    lateinit var alienView: AlienView
    private lateinit var jeux: jeux
    lateinit var start: Button
    lateinit var left: Button
    lateinit var right: Button
    lateinit var joueur: ImageView

    val maxTranslationX = 500f
    val minTranslationX = -500f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Récupérer les vues à partir du layout
        alienView = findViewById(R.id.alien_view)
        start = findViewById(R.id.button_start)
        left = findViewById(R.id.button_left)
        right = findViewById(R.id.button_right)
        joueur = findViewById(R.id.imageView)


        // Garder les boutons invisibles au début
        left.visibility = View.INVISIBLE
        right.visibility = View.INVISIBLE

        // Appliquer éventuellement des insets pour la vue alienView
        ViewCompat.setOnApplyWindowInsetsListener(alienView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialisation du jeu
        jeux = jeux(
            context = this,
            attributes = null,
            defStyleAttr = 0,
            left = left,
            right = right,
            GameListener = this
        )
        // Déplacement du vaisseau à gauche
        right.setOnClickListener {
            val newTranslationX = joueur.translationX + 10f
            if (newTranslationX <= maxTranslationX) {
                joueur.translationX = newTranslationX
                android.util.Log.d("MainActivity", "Déplacement à droite : ${joueur.translationX}")
            } else {
                android.util.Log.d("MainActivity", "Limite droite atteinte")
            }
        }

        // Déplacement du vaisseau à droite
        left.setOnClickListener {
            val newTranslationX = joueur.translationX - 10f
            if (newTranslationX >= minTranslationX) {
                joueur.translationX = newTranslationX
                android.util.Log.d("MainActivity", "Déplacement à gauche : ${joueur.translationX}")
            } else {
                android.util.Log.d("MainActivity", "Limite gauche atteinte")
            }
        }

        // Configure le bouton Start
        start.setOnClickListener {
            // Quand le bouton Start est cliqué, démarrer le jeu et les mouvements des aliens
            alienView.startMovement()  // Lancer le mouvement des aliens
            jeux.start_game()          // Démarre le jeu
            jeux.resume()              // Reprend le jeu si nécessaire

            // Rendre les boutons de déplacement visibles après Start
            left.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
            start.visibility = View.GONE

        }


    }

    override fun NoAliens() {
        // Aucun changement à apporter ici pour la visibilité des boutons
        alienView.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        jeux.pause()
    }

    override fun onResume() {
        super.onResume()
        jeux.resume()
    }
}

