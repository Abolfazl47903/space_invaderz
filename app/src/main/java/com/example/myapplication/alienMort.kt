package com.example.myapplication

class AlienMort(
    private val x: Float,
    private val y: Float,
    private val ligne: Int,
    private val colonne: Int,
    private var timer: Int = 15 // Durée d'affichage de l'explosion
) : UpdateState {
    var isActive = true

    override fun update() {
        if (timer > 0) {
            timer--
        } else {
            isActive = false
        }
    }

    fun getPosition(): Pair<Float, Float> {
        return Pair(x, y)
    }

    fun isVisible(): Boolean {
        return timer > 0 && isActive
    }
}
