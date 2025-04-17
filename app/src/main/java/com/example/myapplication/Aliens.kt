package com.example.myapplication
import android.annotation.SuppressLint
import android.graphics.RectF

abstract class Aliens(
    var vitesseX: Float,
    var vitesseY: Float,
    var vitesseTir: Int,
    var taille: Int,
    var alienDistance: Float,
    var alienDebut: Float,
    var alienFin: Float,
    var alienVitesseInitiale: Float,
    var width: Float,
    var view: AlienView,
    var state: UpdateState? = null
){
    @SuppressLint("NotConstructor")// Juste pour dire que ceci n'est pas un constructeur
    var alien = RectF(alienDistance, alienDebut, alienDistance + width, alienFin)
    /*comme les attributs de Aliens sont privée et qu'on doit qu'a même les utilisé dans les sous classe crabes
    calmar, poulpe on doit faire des getter et setter
     */

    abstract fun shot()  // qui seront rédéfinit dans les sous classes crabes , poulpes et calmar

    abstract  fun donnerPoint(): Int
}
