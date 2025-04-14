package com.example.myapplication

import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Color
import android.graphics.RectF

class joueur(
    var vaisseauLongueur: Float,
    var vaisseauHauteur: Float,
    var largeur: Float,
    val vue: AlienView,
    var joueurDistance: Float,
    var joueurDebut: Float,
    var joueurFin: Float,
    var width: Float,
    var state: UpdateState? = null
) { //Pour l'instant la fusée est un rectangle
    val vaisseauPaint =Paint()
    var finVaisseau =PointF(vaisseauLongueur, vaisseauHauteur) //La fin de la fusée sert à où va sortir le tir
    private var vie : Int = 10
    private val vitesse : Int = 0

    var joueur = RectF(
        joueurDistance,
        joueurDebut,
        joueurDistance + width,
        joueurFin
    )

//    fun finvaisseau(hauteur: Float){ //Méthode servant à initialiser la fin de la fusée
//        finVaisseau.set(vaisseauLongueur, hauteur)
//    }
//    fun alignement(amplitude : Double){ //Sert à placer le rectangle sur l'écran
//        finVaisseau.x = (vaisseauLongueur*Math.sin(amplitude)).toFloat()
//        finVaisseau.y = (-vaisseauLongueur*Math.cos(amplitude) + vue.screenHeight/4).toFloat()
//    }
//    fun collision (missile: missile){
//        if(missile.collisionJoueur(0.016)){  //pour 60 fps jcrois
//            state = missileJoueur(this, vue)
//            state?.update()
//        }
 //  }


}
