package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.View

class JoueurView(context: Context) : View(context) {
    private val joueurBitmap = BitmapFactory.decodeResource(resources, R.drawable.joueur)
    private var joueurX = 0f
    private var joueurY = 0f

    init {
        post {
            joueurX = width / 2f - joueurBitmap.width / 2f
            joueurY = height - joueurBitmap.height * 2f
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(joueurBitmap, joueurX, joueurY, null)
    }

    fun deplacement(direction: String) {
        when (direction) {
            "LEFT" -> {
                joueurX -= 20 // Mouvement vers la gauche
                if (joueurX < 0) joueurX = 0F // Empêcher de sortir de l'écran
            }
            "RIGHT" -> {
                joueurX += 20 // Mouvement vers la droite
                if (joueurX > width - joueurBitmap.width) joueurX =
                    (width - joueurBitmap.width).toFloat() // Limite
            }
        }
        invalidate() // Met à jour l'affichage
    }
}