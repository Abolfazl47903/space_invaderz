package com.example.myapplication

<<<<<<< HEAD
class missileCrabe : missile() {
    fun degats(){

    }
}
=======
class missileCrabe(private val Jeux: jeux) : missile() {


    fun degats(){
        Jeux.vie -= 2

        if (Jeux.vie <= 0) {
            Jeux.game_over()
        }
    }
}
>>>>>>> 72a4e3d469d58a2b13efe3435e9e1c34fe472213
