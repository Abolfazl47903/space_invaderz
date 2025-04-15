package com.example.myapplication

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Button

class jeux @JvmOverloads constructor (
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val left: Button,
    private val right: Button,
    private val gameListener: GameListener,
    private val alienView: AlienView
) {
    // attributs du jeu
    var score: Int = 0
    var niveau_actuel: Int = 1

    // propriétés publiques
    var vie: Int = 10
    var record: Int = 0
    var vie_supp: Int = 1
    var gameOver = false
    var timeLeft = 60.0 // exemple: 60 secondes de jeu

    val activite = context as FragmentActivity

    init {
        // Initialisation supplémentaire si nécessaire
    }

    fun start_game() {
        // Réinitialiser les valeurs du jeu
        score = 0
        vie = 10
        niveau_actuel = 1
        gameOver = false
        timeLeft = 60.0

        // Démarrer le mouvement des aliens via AlienView
        alienView.startMovement()
    }

    fun game_over() {
        if (!gameOver) {
            gameOver = true
            prestation(R.string.lose)
        }
    }

    fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0

        // Mise à jour du temps restant
        timeLeft -= interval

        if (timeLeft <= 0.0) {
            timeLeft = 0.0
            game_over()
        }
    }

    fun prestation(messageId: Int) { // Méthode servant à voir les résultats de la partie
        left.visibility = android.view.View.GONE
        right.visibility = android.view.View.GONE

        class Resultat : DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val le_builder = AlertDialog.Builder(requireActivity())
                le_builder.setTitle(resources.getString(messageId))
                le_builder.setMessage("Score: $score\nNiveau: $niveau_actuel\nVies restantes: $vie")
                le_builder.setPositiveButton("Recommencer le jeu",
                    DialogInterface.OnClickListener { _, _ -> start_game() })
                return le_builder.create()
            }
        }

        activite.runOnUiThread(
            Runnable {
                val feet = activite.supportFragmentManager.beginTransaction()
                val precedent = activite.supportFragmentManager.findFragmentByTag("dialog")
                if (precedent != null) {
                    feet.remove(precedent)
                }
                feet.addToBackStack(null)
                val resultat = Resultat()
                resultat.setCancelable(false)
                resultat.show(feet, "dialog")
            }
        )
    }

    fun updateScore(points: Int) {
        score += points
        // Optionnellement, mettre à jour un affichage du score
    }

    fun alienTouche(typeAlien: Int): Int {
        // Logique pour déterminer les points en fonction du type d'alien
        val points = when (typeAlien) {
            0 -> 30 // Exemple: poulpe
            1 -> 20 // Exemple: crabe
            else -> 10 // Exemple: calmar
        }

        updateScore(points)
        return points
    }

    fun verifierNiveauTermine() {
        // Vérifier si tous les aliens sont détruits
        // Si oui, passer au niveau suivant
        niveau_actuel++

        // Augmenter la difficulté ici

        // Réinitialiser les aliens avec la nouvelle difficulté
        alienView.resetAliens(niveau_actuel)
    }

    fun perdreVie() {
        vie--
        if (vie <= 0) {
            game_over()
        }
    }
}
