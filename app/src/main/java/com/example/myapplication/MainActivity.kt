package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), GameListener {
    lateinit var alienView: AlienView
    lateinit var jeux: Jeux
    lateinit var start: Button
    lateinit var left: Button
    lateinit var right: Button
    lateinit var joueurImageView: ImageView
    lateinit var alienSimple: AlienSimple
    lateinit var joueur: Joueur

    val maxTranslationX = 200f
    val minTranslationX = -200f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Récupérer les vues à partir du layout
        alienView = findViewById(R.id.alien_view)
        start = findViewById(R.id.button_start)
        left = findViewById(R.id.button_left)
        right = findViewById(R.id.button_right)
        joueurImageView = findViewById(R.id.imageView)

        // Garder les boutons invisibles au début
        left.visibility = View.INVISIBLE
        right.visibility = View.INVISIBLE

        // Initialisation de la classe jeux
        jeux = Jeux(
            context = this,
            left = left,
            right = right,
            gameListener = this,
            alienView = alienView
        )

        // Initialisation des classes de jeu
        alienSimple = AlienSimple(
            view = alienView,
            vitesseX = 10f,
            vitesseY = 10f,
            vitesseTir = 50,
            taille = 50,
            alienDistance = 10f,
            alienDebut = 10f,
            alienFin = 10f,
            alienVitesseInitiale = 10f,
            width = 10f,
            vaisseauHauteur = 10f,
            vaisseauLongueur = 10f,
            largeur = 10f,
            vue = jeux
        )

        joueur = Joueur(10f, 10f, 10f, alienView, 10f, 10f, 10f, 10f, null)

        // Configuration de AlienView pour le jeu
        alienView.setupGame(joueur, alienSimple, joueurImageView, jeux)

        ViewCompat.setOnApplyWindowInsetsListener(alienView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Déplacement du vaisseau à droite
        right.setOnClickListener {
            val newTranslationX = joueurImageView.translationX + 10f
            if (newTranslationX <= maxTranslationX) {
                joueurImageView.translationX = newTranslationX
            }
        }

        // Déplacement du vaisseau à gauche
        left.setOnClickListener {
            val newTranslationX = joueurImageView.translationX - 10f
            if (newTranslationX >= minTranslationX) {
                joueurImageView.translationX = newTranslationX
            }
        }

        // Configure le bouton Start
        start.setOnClickListener {
            // Quand le bouton Start est cliqué, démarrer le jeu
            jeux.startGame()  // Démarre le jeu (qui lance aussi le mouvement des aliens)

            // Rendre les boutons de déplacement visibles après Start
            left.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
            start.visibility = View.GONE
        }
    }

    override fun noAliens() {
        // Cette méthode est appelée quand tous les aliens sont détruits
        jeux.verifierNiveauTermine()
    }
}
