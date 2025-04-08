package com.example.myapplication

class alienMort : updateState {
    override fun update(collision: Boolean) {
        if (collision == true) {
            println("L'Alien est mort et disparaît.")

        }
    }
}
