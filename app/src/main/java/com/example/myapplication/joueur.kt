package com.example.myapplication

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF

class joueur (var fuseeLongueur : Float, var fuseeHauteur : Float, val vue : jeux){ //Pour l'instant la fusée est un rectangle
    val fuseePaint =Paint()
    var finFusee =PointF(fuseeLongueur, fuseeHauteur) //La fin de la fusée sert à où va sortir le tir

    fun dessin(canvas : Canavas) { //Dessin du rectangle
        fuseePaint.strokeWidth = largeur * 1.5f
        canvas.drawLine(0f,vue.screenHeight/4,finFusee.x,finFusee.y,fuseePaint)
    }
    fun finfusee(hauteur: Float){ //Méthode servant à initialiser la fin du canon
        finFusee.set(fuseeLongueur, hauteur)
    }
    fun alignement(amplitude : Double){
        finFusee.x = (fuseeLongueur*Math.sin(amplitude)).toFloat()
        finFusee.y = (-fuseeLongueur*Math.cos(amplitude) + vue.screenHeight/4).toFloat()
    }
}