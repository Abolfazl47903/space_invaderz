package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PointF
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView

class AlienView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val crabeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.calmar)
    private val poulpeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.poulpe)
    private val calmarBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.calmar)

    val aliens = mutableListOf<Bitmap>()
    private var offsetX = 0f
    private var offsetY = 0f
    private var direction = 1 // 1 pour aller à droite, -1 pour aller à gauche
    private val descente = 30f // Décalage vertical à chaque changement de direction
    private val handler = Handler(Looper.getMainLooper())

    // Attributs pour le missile
    var missileOnScreen = false
    var missileX = 0f
    var missileY = 0f
    var missileVitesse = 0f
    var missileTaille = 0f
    var missileAngle = 0.0

    lateinit var Joueur: joueur
    lateinit var aliensList: Aliens
    var screenWidth = 0f
    var screenHeight = 0f
    lateinit var joueurImageView: ImageView

    init {
        setWillNotDraw(false)  // Force le dessin
        isClickable = true  // Rend la vue cliquable pour les événements tactiles

        post {
            if (crabeBitmap == null || poulpeBitmap == null || calmarBitmap == null) {
                throw RuntimeException("Les images nécessaires sont manquantes dans les ressources.")
            }

            val largeurEcran = width.toFloat()
            val colonnesAliens = 10
            val espaceEntreAliens = largeurEcran * 0.02f
            val tailleAlien =
                ((largeurEcran - (colonnesAliens + 1) * espaceEntreAliens) / colonnesAliens).toInt()

            aliens.add(Bitmap.createScaledBitmap(crabeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(poulpeBitmap, tailleAlien, tailleAlien, false))
            aliens.add(Bitmap.createScaledBitmap(calmarBitmap, tailleAlien, tailleAlien, false))
        }
    }

    fun setupGame(joueur: joueur, alien: Aliens, joueurView: ImageView) {
        this.Joueur = joueur
        this.aliensList = alien
        this.joueurImageView = joueurView

        // Initialisation des paramètres du missile
        missileTaille = width / 36f
        missileVitesse = width * 3 / 2f
    }

    // Ajout du mouvement horizontal et vertical des aliens
    fun startMovement(vitesse: Int = 10) {
        // Ensure handler is initialized with main looper if null
        val safeHandler = handler ?: Handler(Looper.getMainLooper()).also {
            // If we needed to create a new handler, we'd need a way to assign it back to the property
            // Since handler is a val, we can't reassign it, which suggests there's a deeper issue
            Log.w("AlienView", "Handler was null when starting movement")
        }

        val runnable = object : Runnable {
            override fun run() {
                val largeurEcran = width.toFloat()

                // Mettre à jour le décalage horizontal
                offsetX += direction * vitesse
                if (offsetX >= largeurEcran * 0.1 || offsetX <= -largeurEcran * 0.1) {
                    direction *= -1 // Inverser la direction
                    offsetY += descente // Descendre les aliens verticalement
                }

                // Mise à jour du missile s'il est à l'écran
                if (missileOnScreen) {
                    updateMissile()
                }

                invalidate() // Redessiner avec les nouvelles positions
                safeHandler.postDelayed(this, 50) // Utiliser le handler sécurisé
            }
        }

        safeHandler.post(runnable) // Utiliser le handler sécurisé
    }

    // Dessiner les aliens et le missile
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (aliens.isEmpty()) return // Attendre que les bitmaps soient prêts

        val lignesAliens = 5
        val colonnesAliens = 10
        val largeurEcran = width.toFloat()
        val espaceEntreAliens = largeurEcran * 0.02f
        val tailleAlien = aliens[0].width.toFloat() // Taille déjà redimensionnée

        // Dessiner les aliens
        for (ligne in 0 until lignesAliens) {
            val alienBitmap = aliens[ligne % aliens.size] // Choisir l'image en fonction de la ligne
            for (colonne in 0 until colonnesAliens) {
                val x = colonne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetX
                val y = ligne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetY
                canvas.drawBitmap(alienBitmap, x, y, null)
            }
        }

        // Dessiner le missile s'il est à l'écran
        if (missileOnScreen) {
            dessinMissile(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()

        // Mise à jour des paramètres du missile en fonction de la taille de l'écran
        missileTaille = (w / 36f)
        missileVitesse = (w * 3 / 2f)
    }

    // Lancer un missile
    fun lancerMissile(event: MotionEvent) {
        if (!missileOnScreen) {
            // Calcul de l'angle du tir
            val angle = calculateAngle(event)

            // Position initiale du missile (au centre bas de l'écran)
            missileX = screenWidth / 2
            missileY = screenHeight - 100 // Ajustez selon la position de votre vaisseau
            missileAngle = angle
            missileOnScreen = true
        }
    }

    // Calculer l'angle de tir
    private fun calculateAngle(event: MotionEvent): Double {
        val touchPoint = PointF(event.x, event.y)
        val deltaX = touchPoint.x - (screenWidth / 2)
        val deltaY = (screenHeight - touchPoint.y)  // Inversé car l'origine est en haut

        // Calcul de l'angle avec atan2 (gère tous les quadrants)
        var angle = Math.atan2(deltaX.toDouble(), deltaY.toDouble())

        // Ajustement pour avoir un angle entre -π/2 et π/2 (limité à ±90°)
        angle = angle.coerceIn(-Math.PI/2, Math.PI/2)

        return -angle
    }

    // Mise à jour de la position du missile
    private fun updateMissile() {
        // Calcul des composantes X et Y de la vitesse
        val missileVelocityX = (missileVitesse * Math.sin(missileAngle)).toFloat()
        val missileVelocityY = (missileVitesse * Math.cos(missileAngle)).toFloat()

        // Mise à jour des positions
        missileX += missileVelocityX * 0.05f  // Multiplié par l'intervalle (50ms = 0.05s)
        missileY -= missileVelocityY * 0.05f  // Note: on soustrait car Y est inversé à l'écran

        // Vérifier si le missile est sorti de l'écran
        if (missileY < 0 || missileY > screenHeight || missileX < 0 || missileX > screenWidth) {
            resetMissile()
        }

        // Vérifier les collisions avec les aliens (à implémenter)
        checkCollisions()
    }

    // Dessin du missile
    private fun dessinMissile(canvas: Canvas) {
        // Créer un rectangle pour représenter le missile
        val left = missileX - missileTaille / 2
        val top = missileY - missileTaille
        val right = missileX + missileTaille / 2
        val bottom = missileY

        // Dessiner un rectangle comme missile (vous pouvez remplacer par une image si vous préférez)
        canvas.drawRect(left, top, right, bottom, android.graphics.Paint().apply {
            color = android.graphics.Color.RED
        })
    }

    // Réinitialiser le missile
    fun resetMissile() {
        missileOnScreen = false
    }

    // Vérifier les collisions
    private fun checkCollisions() {
        // À implémenter: vérification des collisions entre le missile et les aliens
        // Cette méthode devrait parcourir vos aliens et vérifier si le missile les touche
    }

    // Gestion des événements tactiles
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val action = e.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            lancerMissile(e)
            return true
        }
        return super.onTouchEvent(e)
    }

    fun resetAliens(niveau: Int) {
        // Réinitialiser la position des aliens
        offsetX = 0f
        offsetY = 0f

        // Augmenter la vitesse en fonction du niveau
        val vitesseBase = 10
        val nouvelleVitesse = vitesseBase + (niveau - 1) * 5

        // Réinitialiser le handler pour les mouvements
        handler.removeCallbacksAndMessages(null)

        // Redémarrer le mouvement avec la nouvelle vitesse
        startMovement(nouvelleVitesse)
    }
}
