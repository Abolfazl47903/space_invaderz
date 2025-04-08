package com.example.myapplication

import android.graphics.PointF
import android.os.Handler
import android.os.Looper

class MissileCollision(val missile: missile) : UpdateState {
    override fun update() {
        missile.explosionPosition = PointF(missile.missile.x, missile.missile.y)
        missile.missileOnScreen = false
        Handler(Looper.getMainLooper()).postDelayed({
            missile.explosionBitmap = null // Supprimer l'explosion après un délai
        }, 500)
    }
}

