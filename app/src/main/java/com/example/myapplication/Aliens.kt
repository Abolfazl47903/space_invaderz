package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.RectF

abstract class Aliens(
    var vitesseX: Int, // attributs privés
    var vitesseY: Int,
    var vitesseTir: Int,
    var taille: Int,
    var alienDistance: Float, var alienDebut: Float, var alienFin: Float, var alienVitesseInitiale: Float, var width: Float, var view: jeux,
    var state: UpdateState? = null
) {
    @SuppressLint("NotConstructor") // juste pour dire que ceci n'est pas un constructeur
    fun actionAliens(): String {
        return "tout les aliens se déplace de manière horizontale en x et verticalement en y "
    }

    var alien = RectF(alienDistance, alienDebut, alienDistance + width, alienFin)

    // Utiliser des propriétés avec des getters et setters personnalisés
    var vitesseXValue: Int
        get() = vitesseX
        set(value) {
            vitesseX = value
        }

    var vitesseYValue: Int
        get() = vitesseY
        set(value) {
            vitesseY = value
        }

    protected fun afficherTaille(): Int { // cette méthode permet d'afficher la taille des aliens
        return taille
    }

    abstract fun shot() // qui seront redéfinies dans les sous-classes crabes, poulpes et calmar

    fun changeDirection() {
        // Votre code ici
    }

    fun finDuJeu() { // la fonction finDuJeu n'est pas obligée d'être abstract
        // Votre code ici
    }

    abstract fun donnerPoint(): Int

    fun detectChoc(missile: missile) {
        if (missile.collision(1.2)) {
            state = AlienMort(this)
            state?.update()
        }
    }

    fun donnerPoints() {
        // Votre code ici
    }
}
