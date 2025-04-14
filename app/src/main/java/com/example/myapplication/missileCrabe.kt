package com.example.myapplication

class missileCrabe(
    private val jeux: jeux,  // Garder cette référence
    alienView: AlienView,
    alien: Aliens,
    joueur: joueur
) : missile(alienView, alien, joueur) {  // Appel au constructeur parent modifié

    override fun degats() {
        if (collisionJoueur(1.2)) {
            jeux.vie -= 2

            if (jeux.vie <= 0) {
                jeux.game_over()
            }
        }
    }
}
