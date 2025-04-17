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

class Jeux @JvmOverloads constructor (
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val left: Button,
    private val right: Button,
    private val gameListener: GameListener,
    val alienView: AlienView
) {
    // attributs du jeu
    var score: Int = 0
    var niveauActuel: Int = 1

    // propriétés publiques
    var vie: Int = 30
    var record: Int = 0
    var vieSupp: Int = 1
    var gameOver = false

    val activite = context as FragmentActivity
    var jeuEnPause = false


    init {
        alienView.setAlienListener2(object : AlienView.AlienListener {
            override fun onAliensReachedPlayer() {
                if (jeuEnPause || gameOver) return

                perdreVie()
                jeuEnPause = true
                alienView.jeuEnPause = true
                showAlienCollisionDialog()
            }

            override fun onMissileHitPlayer() {
                if (jeuEnPause || gameOver) return

                // Le missile a touché le joueur : on affiche un dialogue et met le jeu en pause
                jeuEnPause = true
                alienView.jeuEnPause = true
                showMissileHitDialog()
            }

            override fun updateScore(points: Int) {
                updateScore(points)
            }
        })
    }

    fun startGame() {
        // Réinitialiser les valeurs du jeu
        score = 0
        vie = 10
        niveauActuel = 1
        gameOver = false

        // Démarrer le mouvement des aliens via AlienView
        alienView.startMovement()
    }

    private fun showMissileHitDialog() {
        if (gameOver) return

        left.visibility = android.view.View.GONE
        right.visibility = android.view.View.GONE

        class MissileDialog : DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): android.app.Dialog {
                alienView.jeuEnPause = true
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("Touché !")
                builder.setMessage("Un missile vous a touché.\nVies restantes : $vie")

                builder.setPositiveButton("Continuer") { _, _ ->
                    if (vie > 0) {
                        alienView.resetAliens(niveauActuel)
                        jeuEnPause = false
                        alienView.jeuEnPause = false
                        left.visibility = android.view.View.VISIBLE
                        right.visibility = android.view.View.VISIBLE
                    }
                }

                return builder.create()
            }
        }

        activite.runOnUiThread {
            val ft = activite.supportFragmentManager.beginTransaction()
            val prev = activite.supportFragmentManager.findFragmentByTag("missile_dialog")
            if (prev != null) ft.remove(prev)
            ft.addToBackStack(null)

            val dialog = MissileDialog()
            dialog.setCancelable(false)
            dialog.show(ft, "missile_dialog")
        }
    }

    fun gameOver() {
        if (!gameOver) {
            gameOver = true
            showGameOverDialog(R.string.lose)
        }
    }

    fun showGameOverDialog(messageId: Int) { // Méthode servant à voir les résultats de la partie
        left.visibility = android.view.View.GONE
        right.visibility = android.view.View.GONE

        class Resultat : DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage("Score: $score\nNiveau: $niveauActuel\nVies restantes: $vie")
                builder.setPositiveButton("Recommencer le jeu",
                    DialogInterface.OnClickListener { _, _ -> startGame() })
                return builder.create()
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

    private fun showAlienCollisionDialog() {
        if (gameOver) return // Ne pas afficher si le jeu est déjà terminé

        left.visibility = android.view.View.GONE
        right.visibility = android.view.View.GONE

        class CollisionDialog : DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                alienView.jeuEnPause = true // quand tu ouvres un dialog, le jeu se met en pause
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle(R.string.alien_collision_title) // Ajouter cette ressource string
                builder.setMessage("Les aliens vous ont atteint! Vous perdez une vie.\nVies restantes: $vie")

                builder.setPositiveButton("Continuer") { _, _ ->
                    if (vie > 0) {
                        alienView.resetAliens(niveauActuel)
                        jeuEnPause = false
                        alienView.jeuEnPause = false
                        left.visibility = android.view.View.VISIBLE
                        right.visibility = android.view.View.VISIBLE
                    }
                }

                return builder.create()
            }
        }

        activite.runOnUiThread {
            val ft = activite.supportFragmentManager.beginTransaction()
            val prev = activite.supportFragmentManager.findFragmentByTag("collision_dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val dialog = CollisionDialog()
            dialog.setCancelable(false)
            dialog.show(ft, "collision_dialog")
        }
    }

    fun updateScore(points: Int) {
        score += points
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
        niveauActuel++

        // Réinitialiser les aliens avec la nouvelle difficulté
        alienView.resetAliens(niveauActuel)
    }

    fun perdreVie() {
        vie--
        if (vie <= 0) {
            gameOver()
        }
    }
}
