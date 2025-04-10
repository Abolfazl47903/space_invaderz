package com.example.myapplication

<<<<<<< HEAD
class missileCrabe(private val Jeux: jeux) : missile() {
    fun degats(){
        Jeux.vie -= 2
=======


>>>>>>> 669cb90bc9e7bf8e33b71d5f52ab65353ddbbac8

class missileCrabe(private val Jeux: jeux,
                   alien: Aliens,
                   joueur: joueur) : missile(Jeux, alien, joueur) {


    override fun degats(){

        if (collisionJoueur(1.2)){
            Jeux.vie -= 2

            if (Jeux.vie <= 0) {
                Jeux.game_over()
            }
        }
    }
}
<<<<<<< HEAD
=======

>>>>>>> 669cb90bc9e7bf8e33b71d5f52ab65353ddbbac8
