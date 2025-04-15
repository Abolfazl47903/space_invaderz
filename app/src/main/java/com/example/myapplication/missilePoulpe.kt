package com.example.myapplication

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class missilePoulpe(
    private val jeux: jeux,
    alienView: AlienView,
    alien: Aliens,
    joueur: joueur
) : missile(alienView, alien, joueur) {

    init {
        // Couleur spécifique pour les missiles de poulpe
        missilePaint.color = Color.GREEN
        missileVitesse = -6f  // Missile rapide
    }

    override fun dessin(canvas: Canvas) {
        // Dessin spécifique pour le missile poulpe (en vert, forme triangulaire)
        val paint = Paint().apply {
            color = Color.GREEN
        }

        // Dessin d'un triangle dirigé vers le bas
        val path = android.graphics.Path()
        path.moveTo(missile.x, missile.y + 15f)  // Pointe vers le bas
        path.lineTo(missile.x - 8f, missile.y - 5f)  // Coin gauche
        path.lineTo(missile.x + 8f, missile.y - 5f)  // Coin droit
        path.close()  // Fermer le chemin pour former un triangle

        canvas.drawPath(path, paint)
        drawExplosion(canvas)
    }

    override fun degats() {
        if (collisionJoueur(1.2)) {
            // Le poulpe inflige 1 point de dégât
            jeux.vie -= 1

            // Créer l'effet d'explosion
            explosionPosition = missile.copy()

            // Réinitialiser le missile
            resetMissile()

            // Vérifier si le joueur est mort
            if (jeux.vie <= 0) {
                jeux.game_over()
            }
        }
    }
}
