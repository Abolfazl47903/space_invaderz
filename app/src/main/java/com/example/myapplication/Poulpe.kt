package com.example.myapplication

class Poulpe(vitesseX:Int,
             vitesseY:Int,
             vitesseTir:Int,
             taille:Int,
             ):Aliens(vitesseX,vitesseY,vitesseTir,taille) {
    override fun shot() {

    }

    override fun update() {
    }

    override fun donnerPoint() :Int{
        return 3
    }
}