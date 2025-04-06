package com.example.myapplication

import android.annotation.SuppressLint

abstract class Aliens(
    var vitesseX:Int,// qui sont les attributes privée
    var vitesseY:Int,
    var vitesseTir:Int,
    var taille:Int
){
    @SuppressLint("NotConstructor")// juste pour dire que ceci n'est pas un constructeur
    fun actionAliens() :String{
        return "tout les aliens se déplace de manière horizontale en x et verticalement en y "

    }
    /*comme les attributs de Aliens sont privée et qu'on doit qu'a même les utilisé dans les sous classe crabes
    calmar, poulpe on doit faire des getter et setter
     */
    protected fun getVitesseX()=vitesseX
    protected fun setVitesseX(value: Int){
        vitesseX=value
    }
    protected fun getVitesseY()=vitesseY
    protected fun setVitesseY(value: Int){
        vitesseY=value
    }
    protected fun afficherTaille():Int{ //cette méthode permet d'afficher la taille des aliens
        return taille
    }
    abstract fun shot()  // qui seront rédéfinit dans les sous classes crabes , poulpes et calmar
    abstract fun update()
    fun changeDirection() {

    }
    fun FinDujeu(){ // mais la function findujeux n'est pas obligé dêtre abstract

    }
    abstract  fun donnerPoint(): Int

}