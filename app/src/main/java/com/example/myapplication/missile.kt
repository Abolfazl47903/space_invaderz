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
        missilePaint.color = Color.CYAN
    }
    fun lancer(amplitude : Double){ //Lancement du missile
        missile.x = vue.screenWidth/2f
        missile.y = 1410f
        missileVitesseX =(-missileVitesse*Math.sin(amplitude)).toFloat()
        missileVitesseY =(-missileVitesse*Math.cos(amplitude)).toFloat()
        missileOnScreen = true
    }
    fun dessin(canvas : Canvas){ //Dessin du missile
        canvas.drawCircle(missile.x, missile.y, 10f, missilePaint)
    }
    fun resetMissile(){ //Fait disparître le missile pour faire réapparaître un autre
        missileOnScreen = false
    }
    fun MAJ(interval : Double){if (missileOnScreen) {
        missile.x += (interval * missileVitesseX).toFloat()
        missile.y += (interval * missileVitesseY).toFloat()

        if (missile.x + missileTaille > view.screenWidth
            || missile.x - missileTaille < 0) {
            missileOnScreen = false
        }
        else if (missile.y + missileTaille > view.screenHeight
            || missile.y - missileTaille < 0) {
            missileOnScreen = false
        }
        else if (missile.x + missileTaille > cible.cible.left
            && missile.y + missileTaille > cible.cible.top
            && missile.y - missileTaille < cible.cible.bottom) {
            cible.detectChoc(this)
        }
    }
    }
}