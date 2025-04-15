package com.example.myapplication


class missileJoueur (
    val joueur: joueur,
    private val jeux: jeux) : UpdateState {
    override fun update() {
        if (jeux.vie == 0){joueur.joueur.set(-100f, -100f, -100f, -100f)}
    }
}
