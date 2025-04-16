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
        // Si le missile est en train d'exploser, ne dessiner QUE l'explosion
        if (explosionTimer > 0) {
            drawExplosion(canvas)
            explosionTimer--
            if (explosionTimer <= 0) {
                // L'explosion est terminée, supprimer le missile
                missileOnScreen = false
            }
        } else {
            // Sinon, dessiner le missile normalement
            val paint = Paint().apply {
                color = Color.GREEN
            }

            val path = android.graphics.Path()
            path.moveTo(missile.x, missile.y + 13f)
            path.lineTo(missile.x - 11f, missile.y - 5f)
            path.lineTo(missile.x + 11f, missile.y - 5f)
            path.close()

            canvas.drawPath(path, paint)
        }
    }

    override fun degats() {
        if (collisionJoueur(1.2)) {
            jeux.vie -= 1

            if (jeux.vie <= 0) {
                jeux.game_over()
            } else {
                // Signaler que le missile a touché le joueur
                jeux.jeuEnPause = true
                jeux.alienView.jeuEnPause = true
                jeux.alienView.alienListener?.onMissileHitPlayer()
            }
        }
    }
}
