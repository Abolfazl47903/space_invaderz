package com.example.myapplication




class missileCrabe(private val Jeux: jeux) : missile() {


    fun degats(){
        Jeux.vie -= 2

        if (Jeux.vie <= 0) {
            Jeux.game_over()
        }
    }
}

