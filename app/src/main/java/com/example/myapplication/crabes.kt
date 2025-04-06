package com.example.myapplication

class crabes(
    vitesseX:Int,
    vitesseY:Int,
    vitesseTir:Int,
    taille:Int,
    private val couleur :String):Aliens(vitesseX,vitesseY,vitesseTir,taille){
    override fun shot() {

    }

    override fun update() {
    }

    override fun donnerPoint(): Int{
        return 2  // qui est le nombre de point que la destruction d'un crabe accorde

    }

}