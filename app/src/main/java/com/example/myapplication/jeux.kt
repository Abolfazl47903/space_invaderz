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

class jeux @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable{

    // attributs
    private var score : Int = 0
    private var vie : Int = 3
    private var niveau_actuel : Int = 1

    //propriétés publiques
    var record : Int = 0
    var vie_supp : Int = 1
    var dessin = false
    var gameover = false
    val activite = context as FragmentActivity
    lateinit var thread :Thread

    //méthodes
    fun start_game(){
        dessin = false
        val monstres = mutableListOf<Aliens>() // créer une liste modifiable dans la classe Aliens pour y stocker les positions et type de monstres.
        val parcourir = activite.resources.getXml(R.xml.levels) // prends les données qui sont dans le fichier levels.xml et les stock dans parcourir.
        var x = 0  //j'initialise les positions en x et y et le type de monstre
        var y = 0
        var type = ""
        var etape = parcourir.eventType
        while (etape != XmlPullParser.END_DOCUMENT) {


            when (etape){
                XmlPullParser.START_TAG -> {
                    x = parcourir.getAttributeIntValue(null, "x",0)
                    y = parcourir.getAttributeIntValue(null, "y",0)
                    type = parcourir.getAttributeValue(null, "type")
                }
                XmlPullParser.END_TAG -> {
                    monstres.add(Aliens(x, y, type))
                }
            }
            if (monstres.size == 50) {
                break
            }
            etape = parcourir.next()
        }

    }
    fun verifier_fin_niveau(){

    }
    fun game_over(){
        dessin = false //Sert à montrer comment est l'écran. Si false alors on le voit pas si true on le voit
        prestation(R.string.win) //Méthode pour savoir si on a gagné ou perdu
        gameover = true //Si true alors on a perdu
    }
    fun prestation(messageId : Int) { //Méthode servant à voir les réultats de la partie
        class Resultat : DialogFragment() { //Les fragmetns servent à manier différentes interfaces et donc différents xml, voir l'avant dernier cours
            override fun onCreateDialog(bundle: Bundle?): Dialog { //Des trucs utilisant des builder
                val le_builder= AlertDialog.Builder(requireActivity()) //Des trucs d'import
                le_builder.setTitle(resources.getString(messageId))
                le_builder.setMessage("Nombre de vies : 0" + "GAME OVER") //C'est la petit truc qui s'affichera
                le_builder.setPositiveButton("Recommncer le jeu", //C'est le petit truc qui s'affichera et qui nous dira si on veut recommencer
                    DialogInterface.OnClickListener {_, _->restart_game()}) //Appelle la méthode restart_game au touché
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
        dessin = false
        thread.join() //Les thread servent quand on ouvre plusieurs application (voir cours)
    }
    fun reprendre() { //Si on veut reprendre le jeu
        dessin = true
        thread = Thread(this)
        thread.start()
    }


    fun restart_game(){

    }


}
