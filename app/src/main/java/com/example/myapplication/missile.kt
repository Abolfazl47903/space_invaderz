package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Color
import android.widget.ImageView
import android.os.Handler
import android.os.Looper

abstract class missile (var alienView: AlienView, val alien : Aliens, val joueur: joueur) {
    var explosionBitmap: Bitmap? = null
    var explosionPosition: PointF? = null
    var missile = PointF()
    var missileVitesse = 15f  // Augmenté de 5f à 15f pour un déplacement plus rapide
    var missileVitesseX = 15f // Augmenté également
    var missileVitesseY = 15f // Augmenté également
    var missileOnScreen = true
    var missileTaille = 5f
    var missilePaint = Paint()
    var state: UpdateState? = null
    var explosionTimer = 0 // Compteur pour l'animation d'explosion

    init {
        missilePaint.color = Color.CYAN
        // Utiliser les ressources d'AlienView au lieu de jeux
        explosionBitmap = BitmapFactory.decodeResource(alienView.resources, R.drawable.explosion)
    }

    fun Missile(angle: Double, joueurImageView: ImageView) {
        val location = IntArray(2)
        joueurImageView.getLocationOnScreen(location)

        // Position du centre horizontal et en haut du joueur
        val joueurX = location[0] + joueurImageView.width / 2f
        val joueurY = location[1].toFloat()

        missile.x = joueurX
        missile.y = joueurY

        // Vitesse selon l’angle
        val vitesse = 500f
        missileVitesseX = (Math.sin(angle) * vitesse).toFloat()
        missileVitesseY = -(Math.cos(angle) * vitesse).toFloat() // vers le haut
        missileOnScreen = true
    }

    open fun dessin(canvas: Canvas) { //Dessin du missile
        canvas.drawCircle(missile.x, missile.y, 10f, missilePaint)
        drawExplosion(canvas)
    }

    fun resetMissile() { //Fait disparître le missile pour faire réapparaître un autre
        missileOnScreen = false
        explosionPosition = null
    }

    fun update(interval: Double): Boolean {
        if (missileOnScreen) {
            missile.x += (interval * missileVitesseX).toFloat()
            missile.y += (interval * missileVitesseY).toFloat()

            // Mise à jour du timer d'explosion si nécessaire
            if (explosionPosition != null) {
                explosionTimer--
                if (explosionTimer <= 0) {
                    explosionPosition = null
                }
            }

            // Vérifier si le missile est sorti de l'écran
            if (missile.x + missileTaille > alienView.screenWidth
                || missile.x - missileTaille < 0
                || missile.y + missileTaille > alienView.screenHeight
                || missile.y - missileTaille < 0
            ) {
                missileOnScreen = false
                return false
            }
        }
        return missileOnScreen
    }

    fun collision(interval: Double): Boolean {
        if (missileOnScreen) {
            missile.x += (interval * missileVitesseX).toFloat()
            missile.y += (interval * missileVitesseY).toFloat()

            // Utiliser les propriétés de l'AlienView pour les limites d'écran
            if (missile.x + missileTaille > alienView.screenWidth
                || missile.x - missileTaille < 0
                || missile.y + missileTaille > alienView.screenHeight
                || missile.y - missileTaille < 0
            ) {
                missileOnScreen = false
                return false
            } else if (missile.x + missileTaille > alien.alien.left
                && missile.x - missileTaille < alien.alien.right
                && missile.y + missileTaille > alien.alien.top
                && missile.y - missileTaille < alien.alien.bottom
            ) {
                // Si collision détectée
                createExplosion(missile.x, missile.y)
                state = MissileCollision(this)
                state?.update()
                return true
            }
        }
        return false
    }

    fun collisionJoueur(interval: Double): Boolean {
        if (missileOnScreen) {
            missile.x += (interval * missileVitesseX).toFloat()
            missile.y += (interval * missileVitesseY).toFloat()

            // Utiliser les propriétés de l'AlienView pour les limites d'écran
            if (missile.x + missileTaille > alienView.screenWidth
                || missile.x - missileTaille < 0
                || missile.y + missileTaille > alienView.screenHeight
                || missile.y - missileTaille < 0
            ) {
                missileOnScreen = false
                return false
            } else if (missile.x + missileTaille > joueur.joueur.left
                && missile.x - missileTaille < joueur.joueur.right
                && missile.y + missileTaille > joueur.joueur.top
                && missile.y - missileTaille < joueur.joueur.bottom
            ) {
                // Si collision détectée avec le joueur
                createExplosion(missile.x, missile.y)
                state = MissileCollision(this)
                state?.update()
                return true
            }
        }
        return false
    }

    // Création de l'explosion à une position donnée
    fun createExplosion(x: Float, y: Float) {
        explosionPosition = PointF(x, y)
        explosionTimer = 15 // Durée de l'animation d'explosion
    }

    // Dessin de l'explosion
    fun drawExplosion(canvas: Canvas) {
        explosionPosition?.let { pos ->
            explosionBitmap?.let { bmp ->
                canvas.drawBitmap(
                    bmp,
                    pos.x - bmp.width / 2,
                    pos.y - bmp.height / 2,
                    null
                )
            }
        }
    }

    open fun degats() {
        // Fonction à redéfinir dans les sous-classes
    }

    fun PointF.copy(): PointF {
        return PointF(this.x, this.y)
    }
}
