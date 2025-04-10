package com.example.myapplication





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

