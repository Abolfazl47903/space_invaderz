package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import org.xmlpull.v1.XmlPullParser
import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button


class jeux @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0, val left : Button, val right : Button, val up : Button, val down : Button, val fire : Button): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable{

    // attributs
    private var score : Int = 0
    private var vie : Int = 3
    private var niveau_actuel : Int = 1

    //propriétés publiques
    var record : Int = 0
    var vie_supp : Int = 1
    var interfacee = false
    var gameover = false
    val activite = context as FragmentActivity
    lateinit var thread :Thread
    var screenWidth = 0f
    var screenHeight = 0f



    //méthodes
    fun start_game(){
        left.visibility = View.VISIBLE
        right.visibility = View.VISIBLE
        up.visibility = View.VISIBLE
        down.visibility = View.VISIBLE
        val vaisseau = joueur()
        left.setOnClickListener {
            vaisseau.deplacement()
        }
        right.setOnClickListener {
            vaisseau.deplacement()
        }
        //faire appel a la fonction qui permet de faire bouger les aliens


    }
    fun verifier_fin_niveau(){
        if (score % 90 == 0){

        }
    }
    fun game_over(){
        interfacee = false //Sert à montrer comment est l'écran. Si false alors on le voit pas si true on le voit

        prestation(R.string.win) //Méthode pour savoir si on a gagné ou perdu
        gameover = true //Si true alors on a perdu
    }
    fun prestation(messageId : Int) { //Méthode servant à voir les réultats de la partie
        left.visibility = View.GONE
        right.visibility = View.GONE
        up.visibility = View.GONE
        down.visibility = View.GONE
        class Resultat : DialogFragment() { //Les fragmetns servent à manier différentes interfaces et donc différents xml, voir l'avant dernier cours
            override fun onCreateDialog(bundle: Bundle?): Dialog { //Des trucs utilisant des builder
                val le_builder= AlertDialog.Builder(requireActivity()) //Des trucs d'import
                le_builder.setTitle(resources.getString(messageId))
                le_builder.setMessage("Nombre de vies : 0" + "GAME OVER") //C'est la petit truc qui s'affichera
                le_builder.setPositiveButton("Recommncer le jeu", //C'est le petit truc qui s'affichera et qui nous dira si on veut recommencer
                    DialogInterface.OnClickListener {_, _->start_game()}) //Appelle la méthode restart_game au touché
                return le_builder.create()
            }
        }
        activite.runOnUiThread( //Des trucs avec le fragment, voir l'avant dernier cours sur l'UV
            Runnable {
                val feet = activite.supportFragmentManager.beginTransaction()
                val precedent =
                    activite.supportFragmentManager.findFragmentByTag("dialog")
                if (precedent != null) {
                    feet.remove(precedent)
                }
                feet.addToBackStack(null)
                val resultat = Resultat()
                resultat.setCancelable(false)
                resultat.show(feet,"dialog")
            }
        )   
    }
    fun pause() { //Si on veut faire une pause en quittant l'appli
        interfacee = false
        thread.join() //Les thread servent quand on ouvre plusieurs application (voir cours)
    }
    fun resume() { //Si on veut reprendre le jeu
        interfacee = true
        thread = Thread(this)
        thread.start()
    }



    override fun onSizeChanged(w:Int, h:Int, oldw: Int, oldh: Int){
        super.onSizeChanged(w,h,oldw,oldh)
        screenWidth =w.toFloat()
        screenHeight = h.toFloat()
    }
    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}