package com.example.myapplication

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Color
import android.widget.ImageView

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
    fun Missile(amplitude : Double, imageView: ImageView){ //Lancement du missile
        val imageLocation = IntArray(2)
        imageView.getLocationOnScreen(imageLocation)

        missile.x = imageLocation[0] + imageView.width / 2f
        missile.y = imageLocation[1] - 160f

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
    fun collision(interval : Double){if (missileOnScreen) {
        missile.x += (interval * missileVitesseX).toFloat()
        missile.y += (interval * missileVitesseY).toFloat()

        if (missile.x + missileTaille > vue.screenWidth
            || missile.x - missileTaille < 0) {
            missileOnScreen = false
        }
        else if (missile.y + missileTaille > vue.screenHeight
            || missile.y - missileTaille < 0) {
            missileOnScreen = false
        }
        else if (missile.x + missileTaille > alien.alien.left
            && missile.x + missileTaille > alien.alien.right
            && missile.y - missileTaille < alien.alien.bottom) {
            alien.detectchoc(this)
        }
    }
    }
    fun degat(){

    }
}