package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import android.util.AttributeSet

class AlienView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val crabeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.calmar)
    private val poulpeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.poulpe)
    private val calmarBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.calmar)

    private val aliens = mutableListOf<Bitmap>()
    private var offsetX = 0f
    private var offsetY = 0f
    private var direction = 1 // 1 pour aller à droite, -1 pour aller à gauche
    private val descente = 30f // Décalage vertical à chaque changement de direction permet aussi d'ajuster la vitesse de descente
    private val handler = Handler(Looper.getMainLooper())

    init {
        setWillNotDraw(false)  // ca force le dessin
        post {
            if (crabeBitmap == null || poulpeBitmap == null || calmarBitmap == null) {
                throw RuntimeException("Les images nécessaires sont manquantes dans les ressources.")
            }

            val largeurEcran = width.toFloat()
            val colonnesAliens = 10
            val espaceEntreAliens = largeurEcran * 0.02f
            val tailleAlien =
                ((largeurEcran - (colonnesAliens + 1) * espaceEntreAliens) / colonnesAliens).toInt()

            aliens.add(Bitmap.createScaledBitmap(crabeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(poulpeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(calmarBitmap, tailleAlien, tailleAlien, false))
        }
    }

    // Ajout du mouvement horizontal et vertical
    fun startMovement() {
        val runnable = object : Runnable {
            override fun run() {
                val largeurEcran = width.toFloat()

                // Mettre à jour le décalage horizontal
                offsetX += direction * 10
                if (offsetX >= largeurEcran * 0.1 || offsetX <= -largeurEcran * 0.1) {
                    direction *= -1 // Inverser la direction
                    offsetY += descente // Descendre les aliens verticalement
                }

                invalidate() // Redessiner avec les nouvelles positions
                handler.postDelayed(this, 50) // Intervalle de 50ms
            }
        }
        handler.post(runnable) // Démarre le mouvement
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (aliens.isEmpty()) return // Attendre que les bitmaps soient prêts

        val lignesAliens = 5
        val colonnesAliens = 10
        val largeurEcran = width.toFloat()
        val espaceEntreAliens = largeurEcran * 0.02f
        val tailleAlien = aliens[0].width.toFloat() // Taille déjà redimensionnée

        for (ligne in 0 until lignesAliens) {
            val alienBitmap = aliens[ligne % aliens.size] // Choisir l'image en fonction de la ligne
            for (colonne in 0 until colonnesAliens) {
                val x = colonne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetX
                val y = ligne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetY
                canvas.drawBitmap(alienBitmap, x, y, null)
            }
        }
    }
}


