package com.example.myapplication


import android.graphics.PointF
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.Log

class MissileCollision(val missile: Missile) : UpdateState {
    // Cette méthode va maintenant gérer toute la logique de collision
    override fun update() {
        if (!missile.missileOnScreen) return

        // Vérifier d'abord si le missile sort de l'écran
        if (isOutOfBounds()) {
            resetMissile()
            return
        }

        // Vérifier les collisions avec les aliens
        if (checkAlienCollisions()) {
            // Si collision avec un alien, créer l'effet de collision
            createExplosionEffect()
            // Mettre à jour l'état du jeu (points, etc.)
            updateGameState()
            // Réinitialiser le missile
            resetMissile()
        }
    }
    // missile qui sort de l'écran
    private fun isOutOfBounds(): Boolean {
        val view = missile.alienView
        return missile.missile.x + missile.missileTaille > view.screenWidth
        missile.missile.x - missile.missileTaille < 0
        missile.missile.y + missile.missileTaille > view.screenHeight ||
                missile.missile.y - missile.missileTaille < 0
    }

    private fun checkAlienCollisions(): Boolean {
        val aliensList = missile.alien

        // Créer le rectangle du missile
        val missileRect = RectF(
            missile.missile.x - missile.missileTaille,
            missile.missile.y - missile.missileTaille,
            missile.missile.x + missile.missileTaille,
            missile.missile.y + missile.missileTaille
        )

        // Vérifier la collision avec l'alien
        if (RectF.intersects(missileRect, aliensList.alien)) {
            Log.d("MissileCollision", "Collision détectée avec un alien!")
            return true
        }

        return false
    }
    private fun createExplosionEffect() {
        // Créer l'explosion à la position du missile
        missile.explosionPosition = PointF(missile.missile.x, missile.missile.y)

        // Programmer la suppression de l'explosion après un délai
        Handler(Looper.getMainLooper()).postDelayed({
            missile.explosionPosition = null
        }, 500)
    }

    private fun updateGameState() {
    }

    private fun resetMissile() {
        missile.missileOnScreen = false
    }
}
