package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Color
import android.widget.ImageView

abstract class missile (var vue : jeux, val alien : Aliens, val joueur: joueur) {
    var explosionBitmap: Bitmap? = null
    var explosionPosition: PointF? = null
    var missile = PointF()
    var missileVitesse = 0f
    var missileVitesseX = 0f
    var missileVitesseY = 0f
    var missileOnScreen = true
    var missileTaille = 0f
    var missilePaint = Paint()
    var state: UpdateState? = null

    init {
        missilePaint.color = Color.CYAN
        explosionBitmap = BitmapFactory.decodeResource(vue.resources, R.drawable.explosion)
    }

    fun Missile(amplitude: Double, imageView: ImageView) { //Lancement du missile
        val imageLocation = IntArray(2)
        imageView.getLocationOnScreen(imageLocation)

        missile.x = imageLocation[0] + imageView.width / 2f
        missile.y = imageLocation[1] - 160f

        missileVitesseX = (-missileVitesse * Math.sin(amplitude)).toFloat()
        missileVitesseY = (-missileVitesse * Math.cos(amplitude)).toFloat()
        missileOnScreen = true
    }

    fun dessin(canvas: Canvas) { //Dessin du missile
        canvas.drawCircle(missile.x, missile.y, 10f, missilePaint)
        drawExplosion(canvas)
    }

    fun resetMissile() { //Fait disparître le missile pour faire réapparaître un autre
        missileOnScreen = false
    }

    fun collision(interval: Double) {
        if (missileOnScreen) {
            missile.x += (interval * missileVitesseX).toFloat()
            missile.y += (interval * missileVitesseY).toFloat()

            // Vérification des limites d'écran
            if (missile.x + missileTaille > vue.screenWidth
                || missile.x - missileTaille < 0
                || missile.y + missileTaille > vue.screenHeight
                || missile.y - missileTaille < 0
            ) {
                missileOnScreen = false
            } else if (missile.x + missileTaille > alien.alien.left
                && missile.x - missileTaille < alien.alien.right
                && missile.y + missileTaille > alien.alien.top
                && missile.y - missileTaille < alien.alien.bottom
            ) {
                // Si collision détectée
                alien.detectchoc(this)
                state = MissileCollision(this)
                state?.update()
            }
        }
    }

    fun collisionJoueur(interval: Double) {
        if (missileOnScreen) {
            missile.x += (interval * missileVitesseX).toFloat()
            missile.y += (interval * missileVitesseY).toFloat()

            // Vérification des limites d'écran
            if (missile.x + missileTaille > vue.screenWidth
                || missile.x - missileTaille < 0
                || missile.y + missileTaille > vue.screenHeight
                || missile.y - missileTaille < 0
            ) {
                missileOnScreen = false
            } else if (missile.x + missileTaille > joueur.joueur.left
                && missile.x - missileTaille < joueur.joueur.right
                && missile.y + missileTaille > joueur.joueur.top
                && missile.y - missileTaille < joueur.joueur.bottom
            ) {
                // Si collision détectée
                alien.detectchoc(this)
                state = MissileCollision(this)
                state?.update()
            }
        }
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

    fun degat() {}
}