package com.example.myapplication
// Fait disparaitre le joueur quand il n'a plus de vie
class MissileJoueur (
    val joueur: Joueur,
    private val jeux: Jeux) : UpdateState {
    override fun update() {
        if (jeux.vie == 0){joueur.joueur.set(-100f, -100f, -100f, -100f)}
    }
}
