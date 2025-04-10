package com.example.myapplication

class missilePoulpe(private val Jeux: jeux) : missile() {
    fun degats(){
        Jeux.vie -= 3

        if (Jeux.vie <= 0) {
            Jeux.game_over()
        }
    }
}