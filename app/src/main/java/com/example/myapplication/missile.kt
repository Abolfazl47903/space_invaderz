package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Color
import android.widget.ImageView

abstract class missile (var alienView: AlienView, val alien : Aliens, val joueur: joueur) {
    var explosionBitmap: Bitmap? = null
    var explosionPosition: PointF? = null
    var missile = PointF()
    var missileVitesse = 25f
    var missileVitesseX = 25f
    var missileVitesseY = 25f
    var missileOnScreen = true
    var missileTaille = 5f
    var missilePaint = Paint()
    var state: UpdateState? = null
    var explosionTimer = 0 // Compteur pour l'animation d'explosion

    init {
        missilePaint.color = Color.CYAN
        // S'assurer que l'image d'explosion est bien chargée
        explosionBitmap = BitmapFactory.decodeResource(alienView.resources, R.drawable.explosion)

        if (explosionBitmap == null) {
            android.util.Log.e("Missile", "Failed to load explosion bitmap")
        }
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

    fun collisionJoueur(interval: Double): Boolean {
        if (missileOnScreen) {
            // Déplacer le missile
            missile.x += (interval * missileVitesseX).toFloat()
            missile.y += (interval * missileVitesseY).toFloat()

            // Vérifier la collision avec le joueur
            if (missile.x + missileTaille > joueur.joueur.left
                && missile.x - missileTaille < joueur.joueur.right
                && missile.y + missileTaille > joueur.joueur.top
                && missile.y - missileTaille < joueur.joueur.bottom
            ) {
                // ARRÊTER COMPLÈTEMENT LE MISSILE
                missileVitesseX = 0f
                missileVitesseY = 0f
                missileOnScreen = false

                // Créer l'explosion à la position actuelle du missile
                createExplosion(missile.x, missile.y)

                // MARQUER LE MISSILE COMME ÉTANT EN EXPLOSION
                explosionTimer = 10

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
                // Dessiner l'explosion avec un facteur d'échelle pour la rendre plus visible
                val scale = 1.5f
                val matrix = android.graphics.Matrix()
                matrix.postScale(scale, scale)
                matrix.postTranslate(
                    pos.x - bmp.width * scale / 2,
                    pos.y - bmp.height * scale / 2
                )

                // Utiliser une peinture avec une opacité basée sur explosionTimer
                val paint = Paint().apply {
                    alpha = 255  // Complètement opaque au début
                }

                canvas.drawBitmap(bmp, matrix, paint)
            } ?: run {
                // Log d'erreur si l'image d'explosion est null
                android.util.Log.e("Missile", "Explosion bitmap is null")
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
