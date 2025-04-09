package com.example.myapplication

class crabes(
    vitesseX:Int,
    vitesseY:Int,
    vitesseTir:Int,
    taille:Int,
    alienDistance: Float,
    alienDebut: Float,
    alienFin: Float,
    alienVitesseInitiale: Float,
    width: Float,
    view: jeux
    ):Aliens(vitesseX,vitesseY,vitesseTir,taille, alienDistance, alienDebut, alienFin, alienVitesseInitiale, width, view){
    override fun shot() {

    }



    override fun donnerPoint(): Int{
        return 2  // qui est le nombre de point que la destruction d'un crabe accorde

    }

}