package com.example.myapplication

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Color

class missile (var vue : jeux, val alien : Aliens){
    var missile = PointF()
    var missileVitesse =0f
    var missileVitesseX =0f
    var missileVitesseY =0f
    var missileOnScreen =true
    var missileTaille =0f
    var missilePaint = Paint()

    init{
        missilePaint.color = Color.WHITE
    }
    fun lancer(amplitude : Double){ //Lancement du missile
        missile.x = vue.screenWidth/2f
        missile.y = missileTaille
        missileVitesseX =(missileVitesse*Math.sin(amplitude)).toFloat()
        missileVitesseY =(missileVitesse*Math.cos(amplitude)).toFloat()
        missileOnScreen = true
    }
    fun dessin(canvas : Canvas){ //Dessin du missile
        canvas.drawLine(missile.x, missile.y, missileTaille, missilePaint)
    }
    fun resetMissile(){ //Fait disparître le missile pour faire réapparaître un autre
        missileOnScreen = false
    }
}