package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class MissileCrabe(
    private val jeux: Jeux,
    alienView: AlienView,
    alien: Aliens,
    joueur: Joueur
) : Missile(alienView, alien, joueur) {

    init {
        // Couleur spécifique pour les missiles de crabe
        missilePaint.color = Color.YELLOW
        missileVitesse = -7f  // Missile de vitesse moyenne
    }

    override fun dessin(canvas: Canvas) {
        // Dessin spécifique pour le missile crabe (en jaune, forme rectangulaire)
        val paint = Paint().apply {
            color = Color.YELLOW
        }

        // Dessin d'un rectangle
        canvas.drawRect(
            missile.x - 10f,
            missile.y - 15f,
            missile.x + 10f,
            missile.y + 5f,
            paint
        )

        drawExplosion(canvas)
    }

    override fun degats() {
        if (collisionJoueur(1.2)) {
            // Le crabe inflige 2 points de dégât
            jeux.vie -= 2

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
