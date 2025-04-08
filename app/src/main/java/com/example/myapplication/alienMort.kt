package com.example.myapplication

class AlienMort (var aliens: Aliens) : UpdateState {
    override fun update(){
        aliens.alien.set(-100f, -100f, -100f, -100f)
    }
}


