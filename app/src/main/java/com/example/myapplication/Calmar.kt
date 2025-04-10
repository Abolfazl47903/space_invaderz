package com.example.myapplication

<<<<<<< HEAD
class Calmar(vitesseX: Int, vitesseY: Int, vitesseTir: Int, taille: Int,
             alienDistance: Float, alienDebut: Float, alienFin: Float, alienVitesseInitiale: Float, width: Float, view: jeux)
    : Aliens(vitesseX, vitesseY, vitesseTir, taille, alienDistance, alienDebut, alienFin, alienVitesseInitiale, width, view) {



=======
class Calmar(
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
): Aliens(vitesseX,vitesseY,vitesseTir,taille, alienDistance, alienDebut, alienFin, alienVitesseInitiale, width, view){
    override fun shot() {
>>>>>>> 669cb90bc9e7bf8e33b71d5f52ab65353ddbbac8



    override fun donnerPoint() :Int{
        return 1
    }
}