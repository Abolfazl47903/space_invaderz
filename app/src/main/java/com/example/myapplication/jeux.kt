package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
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
        /* on crée une variable etape qui nous permettera de savoir ou est-ce qu'on se situe
        dans parcourir et SURTOUT IL NOUS PERMET DE CONNAITRE C'EST QUOI LE TYPE DE L'ENDROIT
        OU ON SE SITUE. par exemple etape peut nous dire qu'on est au début ou à la fin d'un document.
        Il peut aussi nous dire "ok la on ouvre une balise" ou bien "la on ferme une balise" , etc.
        var etape = parcourir.eventType  initialise etape à "on est au début du document".
         */
        while (etape != XmlPullParser.END_DOCUMENT) {// on fait un while jusqu'à la fin du document avec toute les positions


            when (etape){
                XmlPullParser.START_TAG -> { // quand etape nous indique qu'on ouvre une balise, d'où le START_TAG
                    //On stock toutes les informations qu'on a besoin
                    x = parcourir.getAttributeIntValue(null, "x",0)
                    y = parcourir.getAttributeIntValue(null, "y",0)
                    type = parcourir.getAttributeValue(null, "type")
                }
                XmlPullParser.END_TAG -> { // quand etape nous indique qu'on ferme une balise, d'où le END_TAG
                    monstres.add(Aliens(x, y, type))// on ajoute les infos dans la classe Aliens dans une liste monstres, qu'on avait créer un début de la fonction
                }
            }
            if (monstres.size == 50) {
                /* vu qu'on commence au niveau 1 on veut que cette fonction prennent en compte que les
                monstres du niveau 1, logique on va pas afficher les autres niveau aussi. Ducoup on fait
                un if avec le nombre de monstre == 50 car quand il y a 50 monstres stocké dans la liste
                alors on break le while (il y a 50 monstres au niveau 1)
                 */
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
                    DialogInterface.OnClickListener { _, _->restart_game()}) //Appelle la méthode restart_game au touché
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
