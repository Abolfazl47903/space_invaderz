package com.example.myapplication

class missileCollision : updateState {
    override fun update(collision: Boolean) {
        if (collision == true) {
            println("Le missile entre en collision et explose.")

        }
    }
}
