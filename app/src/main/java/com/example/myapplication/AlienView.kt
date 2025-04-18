package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView

class AlienView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), AlienObserver {

    // Interface pour les écouteurs d'événements aliens
    interface AlienListener {
        fun onAliensReachedPlayer()
        fun updateScore(points: Int)
        fun onMissileHitPlayer()
    }

    // Bitmaps et ressources graphiques
    private val crabeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.crabe)
    private val poulpeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.poulpe)
    private val calmarBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.calmar)
    private val collisionImage: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.explosion)

    // Liste des images d'aliens
    val aliens = mutableListOf<Bitmap>()

    // Handler pour la gestion des animations
    private val handler = Handler(Looper.getMainLooper())

    // Composants du jeu
    lateinit var joueur: Joueur
    lateinit var aliensList: Aliens
    var screenWidth = 0f
    var screenHeight = 0f
    lateinit var joueurImageView: ImageView
    lateinit var jeux: Jeux

    // Gestionnaires
    private lateinit var alienManager: AlienManager
    private lateinit var missileManager: MissileManager
    private lateinit var collisionManager: CollisionManager

    // Variables d'état
    var alienListener: AlienListener? = null
    var jeuEnPause = false

    // Pour l'affichage du score
    private var scorePaint = Paint()

    init {
        setWillNotDraw(false)  // Force le dessin
        isClickable = true  // Rend la vue cliquable pour les événements tactiles

        // Configuration de la peinture pour le score
        scorePaint.color = Color.WHITE
        scorePaint.textSize = 40f
        scorePaint.textAlign = Paint.Align.LEFT

        post {
            if (crabeBitmap == null || poulpeBitmap == null || calmarBitmap == null) {
                throw RuntimeException("Les images nécessaires sont manquantes dans les ressources.")
            }

            val largeurEcran = width.toFloat()
            val colonnesAliens = 10
            val espaceEntreAliens = largeurEcran * 0.01f
            val tailleAlien =
                ((largeurEcran - (colonnesAliens + 1) * espaceEntreAliens) / colonnesAliens).toInt()

            aliens.add(Bitmap.createScaledBitmap(crabeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(poulpeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(calmarBitmap, tailleAlien, tailleAlien, false))
        }
    }

    fun setAlienListener2(listener: AlienListener) {
        alienListener = listener
    }

    fun setupGame(joueur: Joueur, alien: Aliens, joueurView: ImageView, gameInstance: Jeux) {
        this.joueur = joueur
        this.aliensList = alien
        this.joueurImageView = joueurView
        this.jeux = gameInstance

        // Initialiser les gestionnaires
        alienManager = AlienManager(screenWidth, screenHeight, aliens, joueurImageView)
        missileManager = MissileManager(this, joueur, alien, joueurImageView, jeux)
        collisionManager = CollisionManager(this, alienManager, jeux, collisionImage)

        // S'inscrire comme observateur des événements aliens
        alienManager.addObserver(this)
    }

    // Observer pattern : implémenter les callbacks de l'interface AlienObserver
    override fun onAlienDestroyed(ligne: Int, colonne: Int, points: Int) {
        alienListener?.updateScore(points)
    }

    override fun onLevelCompleted() {
        jeux.verifierNiveauTermine()
    }

    override fun onAliensReachedPlayer() {
        alienListener?.onAliensReachedPlayer()
    }

    // Mise à jour de l'affichage du score
    fun updateScoreDisplay() {
        // Utiliser le singleton pour mettre à jour l'affichage
        ScoreManager.refreshScore()
    }

    // Démarrer le mouvement et les tirs des aliens
    fun startMovement(vitesse: Float = 1f) {
        val safeHandler = handler ?: Handler(Looper.getMainLooper()).also {
            Log.w("AlienView", "Handler was null when starting movement")
        }

        val runnable = object : Runnable {
            override fun run() {
                if (jeuEnPause) {
                    safeHandler.postDelayed(this, 100)
                    return
                }

                // Mettre à jour le mouvement des aliens
                alienManager.updateMovement(vitesse)

                // Faire tirer les aliens aléatoirement
                val alienToShoot = alienManager.selectAlienForShot()
                missileManager.createAlienMissile(alienToShoot)

                // Mise à jour de tous les missiles
                val playerHit = missileManager.updateAllMissiles()
                if (playerHit) {
                    jeux.perdreVie()
                    alienListener?.onMissileHitPlayer()
                }

                // Vérifier les collisions
                val alienDestroyed = collisionManager.checkCollisions(missileManager.allPlayerMissiles as MutableList<MissileDuJoueur>)

                // Mise à jour des effets de collision
                collisionManager.updateCollisionEffects()

                // Mise à jour de l'affichage du score
                updateScoreDisplay()

                invalidate() // Mises à jour des positions
                safeHandler.postDelayed(this, 50)
            }
        }
        safeHandler.post(runnable)
    }

    // Dessiner les éléments du jeu
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (aliens.isEmpty()) return 
        // Dessiner les aliens
        alienManager.drawAliens(canvas)

        // Dessiner les effets de collision
        collisionManager.drawEffects(canvas)

        // Dessiner les missiles
        missileManager.drawMissiles(canvas)

        // Utiliser le ScoreManager pour l'affichage du score
        val scoreManager = ScoreManager
        if (scoreManager.scoreTextView == null) {
            canvas.drawText("Score: ${jeux.score}", 20f, 60f, scorePaint)
            canvas.drawText("Niveau: ${jeux.niveauActuel}", 265f, 60f, scorePaint)
            canvas.drawText("Vies: ${jeux.vie}", 500f, 60f, scorePaint)
        } else {
            // Assurer que le score est à jour
            ScoreManager.refreshScore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()
    }

    // tir de missiles lors du click
    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (jeuEnPause) return false

        val action = e.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            missileManager.lancerMissile(e)
            return true
        }
        return super.onTouchEvent(e)
    }

    // Réinitialisation des aliens
    fun resetAliens(niveau: Int) {
        // Réinitialiser les aliens
        alienManager.resetAliens()

        // Effacer tous les missiles
        missileManager.clearAllMissiles()

        // Redémarrer le mouvement avec la nouvelle vitesse adaptée au niveau
        val vitesseBase = 1.0f
        val nouvelleVitesse = vitesseBase + (niveau - 1) * 0.1f

        handler.removeCallbacksAndMessages(null)
        startMovement(nouvelleVitesse)
    }
}
