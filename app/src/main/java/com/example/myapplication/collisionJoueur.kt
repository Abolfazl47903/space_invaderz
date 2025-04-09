package com.example.myapplication

class collisionJoueur (val joueur: joueur) : UpdateState {
    override fun update() {
        joueur.joueur.set(-100f, -100f, -100f, -100f)
    }
}