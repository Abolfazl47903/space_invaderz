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
import android.view.View
import android.widget.Button


class jeux @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0, val left : Button, val right : Button, val up : Button , val down : Button, val fire : Button): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable{

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
        up.setOnClickListener {
            vaisseau.deplacement()
        }
        down.setOnClickListener {
            vaisseau.deplacement()
        }
        fire.setOnClickListener {
            vaisseau.shot()
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
    fun reprendre() { //Si on veut reprendre le jeu
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

/*
class CanonView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable{
    lateinit var canvas: Canvas
    val backgroundPaint = Paint()
    val textPaint = Paint()
    var screenWidth = 0f
    var screenHeight = 0f
    var drawing = false
    lateinit var thread: Thread
    val canon = Canon(0f, 0f, 0f, 0f, this)
    //val obstacle = Obstacle(0f, 0f, 0f, 0f, 0f, this)
    val cible = Cible(0f,0f,0f,0f,0f,this)
    val balle = BalleCanon(this, cible)
    var shotsFired = 0
    var timeLeft = 0.0
    init {
        backgroundPaint.color = Color.WHITE
        textPaint.textSize= screenWidth/20
        textPaint.color = Color.BLACK
        timeLeft = 3600.0
    }
    var gameOver = false
    val activity = context as FragmentActivity
    var totalElapsedTime = 0.0

    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        val currentTime = System.currentTimeMillis()
        var elapsedTimeMS:Double=(currentTime-previousFrameTime).toDouble()
        totalElapsedTime += elapsedTimeMS / 1000.0
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            var elapsedTimeMS = (currentTime-previousFrameTime).toDouble()
            updatePositions(elapsedTimeMS)
            draw()
            previousFrameTime = currentTime
        }
    }

    override fun onSizeChanged(w:Int, h:Int, oldw:Int, oldh:Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()

        canon.canonBaseRadius = (w / 18f)
        canon.canonLongueur = (w / 18f)
        canon.largeur = (w / 24f)
        canon.setFinCanon(h / 2f)

        balle.canonballRadius= (w / 36f)
        balle.canonballVitesse = (w*3 / 2f)

        /*
        obstacle.obstacleDistance = (w*5 / 8f)
        obstacle.obstacleDebut = (h / 8f)
        obstacle.obstacleFin = (h*3 / 8f)
        obstacle.width = (w / 24f)
        obstacle.initialObstacleVitesse= (0f)
        obstacle.setRect()
        obstacle.initialObstacleVitesse = (h / 2f)
         */

        cible.width = (w * 20/24f)
        cible.cibleDistance= (h*1/24f)
        cible.cibleDebut = (w *3/ 24f)
        cible.cibleFin = (w*12 / 24f)
        cible.cibleVitesseInitiale = (0f)
        cible.setRect()

        textPaint.setTextSize(w / 20f)
        textPaint.isAntiAlias = true
    }
    fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(),
                canvas.height.toFloat(), backgroundPaint)
            val formatted = String.format("%.2f", timeLeft)
            canvas.drawText("Il reste $formatted secondes. ",
                30f, 50f, textPaint)
            canon.draw(canvas)
            if (balle.canonballOnScreen)
                balle.draw(canvas)
            //obstacle.draw(canvas)
            cible.draw(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }
    fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0
        //obstacle.update(interval)
        cible.update(interval)
        balle.update(interval)
        timeLeft -= interval

        if (timeLeft <= 0.0) {
            timeLeft = 0.0
            gameOver = true
            drawing = false
            showGameOverDialog(R.string.lose)
        }
    }
    fun gameOver() {
        drawing = false
        showGameOverDialog(R.string.win)
        gameOver = true
    }
    fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage(
                    "Nombres de tirs : {$shotsFired} " +
                            "Temps écoulé : {$totalElapsedTime}"
                )
                builder.setPositiveButton("Redémarre le jeu",
                    DialogInterface.OnClickListener { _, _->newGame()}
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev =
                    activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.setCancelable(false)
                gameResult.show(ft,"dialog")
            }
        )
    }

    fun fireCanonball(event: MotionEvent) {
        if (! balle.canonballOnScreen) {
            val angle = alignCanon(event)
            balle.launch(angle)
            ++shotsFired
        }
    }

    fun alignCanon(event: MotionEvent): Double {
        val touchPoint = Point(event.x.toInt(), event.y.toInt())
        val deltaX = touchPoint.x - (screenWidth / 2)
        val deltaY = (screenHeight - touchPoint.y)  // Inversé car l'origine est en haut

        // Calcul de l'angle avec atan2 (gère tous les quadrants)
        var angle = Math.atan2(deltaX.toDouble(), deltaY.toDouble())

        // Ajustement pour avoir un angle entre 0 et π (vers le haut)
        angle = angle.coerceIn(-Math.PI/2, Math.PI/2)  // Limite à ±90° pour éviter de tirer vers le bas

        canon.align(angle)
        return -angle
    }
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val action = e.action
        if (action == MotionEvent.ACTION_DOWN
            || action == MotionEvent.ACTION_MOVE) {
            fireCanonball(e)
        }
        return true
    }
    fun newGame() {
        cible.resetCible()
        //obstacle.resetObstacle()
        timeLeft = 10.0
        balle.resetCanonBall()
        shotsFired = 0
        totalElapsedTime = 0.0
        drawing = true
        if (gameOver) {
            gameOver = false
            thread = Thread(this)
            thread.start()
        }
    }
    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}
 */