package com.example.myapplication

import android.graphics.RectF

class Joueur(
    var vaisseauLongueur: Float,
    var vaisseauHauteur: Float,
    var largeur: Float,
    val vue: AlienView,
    var joueurDistance: Float,
    var joueurDebut: Float,
    var joueurFin: Float,
    var width: Float,
    var state: UpdateState? = null
) {

    var joueur = RectF(
        joueurDistance,
        joueurDebut,
        joueurDistance + width,
        joueurFin
    )
}
