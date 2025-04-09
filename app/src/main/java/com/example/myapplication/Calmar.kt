package com.example.myapplication

class Calmar(vitesseX:Int,
             vitesseY:Int,
             vitesseTir:Int,
             taille:Int,
<<<<<<< HEAD
             ):Aliens(vitesseX,vitesseY,vitesseTir,taille) {
=======
             alienDistance: Float,
             alienDebut: Float,
             alienFin: Float,
             alienVitesseInitiale: Float,
             width: Float,
             view: jeux
             ):Aliens(vitesseX,vitesseY,vitesseTir,taille, alienDistance, alienDebut, alienFin, alienVitesseInitiale, width, view) {
    override fun shot() {

    }

<<<<<<< HEAD
=======
    override fun update() {
    }

>>>>>>> 5c5f48f9abaccc01c6bfee014e3667258d1a517c
>>>>>>> 81ecf28520cebf1fb490a4c6b14bb98dc991b5f7
    override fun donnerPoint() :Int{
        return 1
    }
}