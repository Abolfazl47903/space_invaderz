package com.example.myapplication

class Poulpe(vitesseX:Int,
             vitesseY:Int,
             vitesseTir:Int,
             taille:Int,
             alienDistance: Float,
             alienDebut: Float,
             alienFin: Float,
             alienVitesseInitiale: Float,
             width: Float,
             view: jeux
             ): Aliens(vitesseX,
    vitesseY,
    vitesseTir,
    taille,
    alienDistance,
    alienDebut,
    alienFin,
    alienVitesseInitiale,
    width,
    view) {
    override fun shot() {

    }

    override fun donnerPoint() :Int{
        return 3
    }
}