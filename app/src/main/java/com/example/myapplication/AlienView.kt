package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.View

// ici va permetre de visualiser les différents aliens a l'écran
class AlienView(context: Context) : View(context) {
    private val crabeBitmap = BitmapFactory.decodeResource(resources, R.drawable.crabe)
    private val poulpeBitmap = BitmapFactory.decodeResource(resources, R.drawable.poulpe)
    private val calmarBitmap = BitmapFactory.decodeResource(resources, R.drawable.calmar)

    // Optimisation pour redimensionner une seule fois
    private val aliens = mutableListOf<Bitmap>()

    init {
        // Ajuster les tailles des aliens à la largeur de l'écran de l'android
        post {
            val largeurEcran = width.toFloat()
            val colonnesAliens = 10
            val espaceEntreAliens = largeurEcran * 0.02f
            val tailleAlien = ((largeurEcran - (colonnesAliens + 1) * espaceEntreAliens) / colonnesAliens).toInt()

            aliens.add(Bitmap.createScaledBitmap(crabeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(poulpeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(calmarBitmap, tailleAlien, tailleAlien, false))

            invalidate() // Redessiner les aliens  après redimensionnement
        }
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
                val x = colonne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens
                val y = ligne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens
                canvas.drawBitmap(alienBitmap, x, y, null)
            }
        }
    }
}


