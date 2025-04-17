package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class MissileCalmar(
    private val jeux: Jeux,
    alienView: AlienView,
    alien: Aliens,
    joueur: Joueur
) : Missile(alienView, alien, joueur) {

    init {
        // Couleur spécifique pour les missiles de calmar
        missilePaint.color = Color.MAGENTA
        missileVitesse = -8f  // Missile plus lent mais plus puissant
    }

    override fun dessin(canvas: Canvas) {
        // Dessin spécifique pour le missile calmar (en magenta, forme de losange)
        val paint = Paint().apply {
            color = Color.MAGENTA
        }

        // Dessin d'un losange
        val path = android.graphics.Path()
        path.moveTo(missile.x, missile.y + 15f)  // Pointe bas
        path.lineTo(missile.x - 10f, missile.y)    // Pointe gauche
        path.lineTo(missile.x, missile.y - 15f)  // Pointe haut
        path.lineTo(missile.x + 10f, missile.y)    // Pointe droite
        path.close()

        canvas.drawPath(path, paint)
        drawExplosion(canvas)
    }

    override fun degats() {
        if (collisionJoueur(1.2)) {
            // Le calmar inflige 3 points de dégât
            jeux.vie -= 3

            // Créer l'effet d'explosion
            explosionPosition = missile.copy()

            // Réinitialiser le missile
            resetMissile()

            // Vérifier si le joueur est mort
            if (jeux.vie <= 0) {
                jeux.gameOver()
            }
        }
    }
}
