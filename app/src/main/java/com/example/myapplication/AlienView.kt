package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Paint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import java.util.Random

class AlienView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    interface AlienListener {
        fun onAliensReachedPlayer()
        fun updateScore(points: Int)
        fun onMissileHitPlayer()
    }

    private val crabeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.crabe)
    private val poulpeBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.poulpe)
    private val calmarBitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.calmar)
    private val collisionImage: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.explosion)
    private val aliensVivants = Array(5) { Array(10) { true } }
    val aliens = mutableListOf<Bitmap>()
    private var offsetX = 0f
    private var offsetY = 0f
    private var direction = 1 // 1 pour aller à droite, -1 pour aller à gauche
    private val descente = 30f // Décalage vertical à chaque changement de direction
    private val handler = Handler(Looper.getMainLooper())
    private val collisionEffects = mutableListOf<AlienMort>()

    // Variable pour l'affichage du score de secours
    private var scorePaint = Paint()

    // Attributs pour les missiles
    lateinit var Joueur: joueur
    lateinit var aliensList: Aliens
    var screenWidth = 0f
    var screenHeight = 0f
    lateinit var joueurImageView: ImageView
    lateinit var jeux: jeux

    // Gestion des missiles - liste de missiles pour permettre multiples tirs
    private val playerMissiles = mutableListOf<MissileDuJoueur>()
    private val alienMissiles = mutableListOf<missile>()
    private val random = Random()
    private var lastAlienShotTime = 0L
    private val alienShotInterval = 2000L // 2 secondes entre les tirs

    // Maximum de missiles player à l'écran simultanément
    private val MAX_PLAYER_MISSILES = 3

    private data class TemporaryExplosion(val x: Float, val y: Float, var duration: Int = 15)
    private val temporaryExplosions = mutableListOf<TemporaryExplosion>()
    var alienListener: AlienListener? = null
    var jeuEnPause = false

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
            val espaceEntreAliens = largeurEcran * 0.02f
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

    fun setupGame(joueur: joueur, alien: Aliens, joueurView: ImageView, gameInstance: jeux) {
        this.Joueur = joueur
        this.aliensList = alien
        this.joueurImageView = joueurView
        this.jeux = gameInstance
    }

    // Mise à jour de l'affichage du score
    fun updateScoreDisplay() {
        // Utiliser le singleton pour mettre à jour l'affichage
        ScoreManager.refreshScore()
    }

    // Démarrer le mouvement et les tirs des aliens
    fun startMovement(vitesse: Int = 10) {
        val safeHandler = handler ?: Handler(Looper.getMainLooper()).also {
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

                // Faire tirer les aliens aléatoirement
                maybeShootAlienMissile()

                // Mise à jour de tous les missiles
                updateAllMissiles()

                // Vérifier les collisions
                checkCollisions()

                // Mise à jour des effets de collision
                updateCollisionEffects()

                // Mise à jour de l'affichage du score
                updateScoreDisplay()

                invalidate() // Redessiner avec les nouvelles positions
                safeHandler.postDelayed(this, 50) // Utiliser le handler sécurisé
            }
        }
        safeHandler.post(runnable)
        if ((context as? jeux)?.jeuEnPause == true) return

    }

    // Fonction pour faire tirer les aliens aléatoirement
    private fun maybeShootAlienMissile() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAlienShotTime > alienShotInterval) {
            val lignesAliens = 5
            val colonnesAliens = 10

            // Trouver un alien vivant aléatoire pour tirer
            val candidateAliens = mutableListOf<Triple<Int, Int, Float>>()

            for (ligne in 0 until lignesAliens) {
                for (colonne in 0 until colonnesAliens) {
                    if (aliensVivants[ligne][colonne]) {
                        // Donner la priorité aux aliens du bas (plus proches du joueur)
                        candidateAliens.add(Triple(ligne, colonne, (lignesAliens - ligne).toFloat()))
                    }
                }
            }

            if (candidateAliens.isNotEmpty()) {
                // Sélectionner un alien avec une probabilité plus élevée pour ceux du bas
                val totalWeight = candidateAliens.sumOf { it.third.toDouble() }
                var randomValue = random.nextDouble() * totalWeight
                var selectedAlien: Triple<Int, Int, Float>? = null

                for (alien in candidateAliens) {
                    randomValue -= alien.third
                    if (0 >= randomValue) {
                        selectedAlien = alien
                        break
                    }
                }

                selectedAlien = selectedAlien ?: candidateAliens.last()

                // Créer un missile selon le type d'alien
                val ligne = selectedAlien.first
                val colonne = selectedAlien.second

                val largeurEcran = width.toFloat()
                val espaceEntreAliens = largeurEcran * 0.02f
                val tailleAlien = aliens[0].width.toFloat()

                val alienX = colonne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetX + tailleAlien / 2
                val alienY = ligne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetY + tailleAlien

                // Créer le missile selon le type d'alien
                val newMissile = when (ligne % 3) {
                    0 -> missilePoulpe(jeux, this, aliensList, Joueur)
                    1 -> missileCrabe(jeux, this, aliensList, Joueur)
                    else -> missileCalmar(jeux, this, aliensList, Joueur)
                }

                // Positionner le missile
                newMissile.missile.x = alienX
                newMissile.missile.y = alienY
                newMissile.missileVitesseX = 0f  // Pas de déplacement horizontal
                newMissile.missileVitesseY = 100f  // Direction vers le bas
                newMissile.missileOnScreen = true

                // Ajouter le missile à la liste
                alienMissiles.add(newMissile)

                lastAlienShotTime = currentTime
            }
        }
    }

    // Mise à jour de tous les missiles (joueur et aliens)
    private fun updateAllMissiles() {
        // Mise à jour des missiles du joueur
        val playerIterator = playerMissiles.iterator()
        while (playerIterator.hasNext()) {
            val missile = playerIterator.next()
            if (missile.missileOnScreen) {
                missile.update(0.05)  // Paramètre d'intervalle de temps
                missile.degats()
            } else {
                playerIterator.remove()
            }
        }

        // Mise à jour des missiles des aliens
        val alienIterator = alienMissiles.iterator()
        while (alienIterator.hasNext()) {
            val missile = alienIterator.next()
            if (missile.missileOnScreen) {
                missile.update(0.05)
                if (missile.collisionJoueur(0.05)) {
                    jeux.perdreVie()
                    // Ne pas supprimer le missile ici,
                    // l'animation d'explosion s'en chargera
                }
            } else {
                alienIterator.remove()
            }
        }
        val explosionIterator = temporaryExplosions.iterator()
        while (explosionIterator.hasNext()) {
            val explosion = explosionIterator.next()
            explosion.duration--
            if (explosion.duration <= 0) {
                explosionIterator.remove()
            }
        }
    }

    private fun updateCollisionEffects() {
        // Mettre à jour tous les effets de collision
        collisionEffects.forEach { it.update() }

        // Supprimer les effets terminés
        collisionEffects.removeAll { !it.isVisible() }
    }

    // Dessiner les aliens, les missiles et les explosions
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
                // Ne dessiner que les aliens vivants
                if (aliensVivants[ligne][colonne]) {
                    val x = colonne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetX
                    val y = ligne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetY
                    canvas.drawBitmap(alienBitmap, x, y, null)
                }
            }
        }

        // Dessiner les effets de collision actifs
        collisionEffects.forEach { effect ->
            if (effect.isVisible()) {
                val (x, y) = effect.getPosition()
                if (collisionImage != null) {
                    canvas.drawBitmap(
                        collisionImage,
                        x - collisionImage.width / 2,
                        y - collisionImage.height / 2,
                        null
                    )
                }
            }
        }

        // Dessiner tous les missiles du joueur
        playerMissiles.forEach { missile ->
            if (missile.missileOnScreen) {
                missile.dessin(canvas)
            }
        }

        // Dessiner les missiles des aliens
        alienMissiles.forEach { missile ->
            if (missile.missileOnScreen) {
                missile.dessin(canvas)
            }
        }

        temporaryExplosions.forEach { explosion ->
            collisionImage?.let { bitmap ->
                // Faire grossir légèrement l'explosion pour la rendre plus visible
                val scale = 1.5f
                val matrix = android.graphics.Matrix()
                matrix.postScale(scale, scale)
                matrix.postTranslate(
                    explosion.x - bitmap.width * scale / 2,
                    explosion.y - bitmap.height * scale / 2
                )

                // Dessiner l'explosion avec une opacité qui diminue avec le temps
                val paint = Paint().apply {
                    alpha = (explosion.duration * 255 / 15) // Diminuer graduellement l'opacité
                }

                canvas.drawBitmap(bitmap, matrix, paint)
            }
        }

        // Utiliser le ScoreManager pour l'affichage du score
        val scoreManager = ScoreManager
        if (scoreManager.scoreTextView == null) {
            canvas.drawText("Score: ${jeux.score}", 20f, 60f, scorePaint)
            canvas.drawText("Niveau: ${jeux.niveau_actuel}", 265f, 60f, scorePaint)
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

    // Lancer un missile du joueur
    fun lancerMissile(event: MotionEvent) {
        // Vérifier si on peut lancer un nouveau missile (max 3 à l'écran)
        if (playerMissiles.size < MAX_PLAYER_MISSILES) {
            // Créer un nouveau missile
            val newMissile = MissileDuJoueur(this, aliensList, Joueur)

            // Calculer l'angle
            val angle = calculateAngle(event)

            // Lancer le missile depuis le joueur
            newMissile.Missile(angle, joueurImageView)

            // Ajouter le missile à la liste
            playerMissiles.add(newMissile)
        }
    }

    // Calculer l'angle de tir
    private fun calculateAngle(event: MotionEvent): Double {
        val touchPoint = PointF(event.x, event.y)
        val joueurLocation = IntArray(2)
        joueurImageView.getLocationOnScreen(joueurLocation)

        val joueurX = joueurLocation[0] + joueurImageView.width / 2f
        val joueurY = joueurLocation[1] + joueurImageView.height / 2f

        val deltaX = touchPoint.x - joueurX
        val deltaY = joueurY - touchPoint.y

        return Math.atan2(deltaX.toDouble(), deltaY.toDouble())
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

    // Vérifier les collisions des missiles du joueur avec les aliens
    private fun checkCollisions() {
        val lignesAliens = 5
        val colonnesAliens = 10
        val largeurEcran = width.toFloat()
        val espaceEntreAliens = largeurEcran * 0.02f
        val tailleAlien = aliens[0].width.toFloat()

        // Pour chaque missile du joueur
        val playerMissileIterator = playerMissiles.iterator()
        while (playerMissileIterator.hasNext()) {
            val missile = playerMissileIterator.next()
            if (!missile.missileOnScreen) {
                playerMissileIterator.remove()
                continue
            }

            // Rectangle du missile
            val missileRect = RectF(
                missile.missile.x - missile.missileTaille / 2,
                missile.missile.y - missile.missileTaille,
                missile.missile.x + missile.missileTaille / 2,
                missile.missile.y
            )

            // Vérifier chaque alien
            var collisionDetected = false
            outerLoop@ for (ligne in 0 until lignesAliens) {
                for (colonne in 0 until colonnesAliens) {
                    // Ne vérifier que les aliens vivants
                    if (aliensVivants[ligne][colonne]) {
                        val x = colonne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetX
                        val y = ligne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetY

                        // Rectangle de l'alien
                        val alienRect = RectF(
                            x,
                            y,
                            x + tailleAlien,
                            y + tailleAlien
                        )

                        // Vérifier s'il y a collision
                        if (RectF.intersects(missileRect, alienRect)) {
                            // Marquer l'alien comme mort
                            aliensVivants[ligne][colonne] = false

                            // Créer un effet de collision à la position de l'alien
                            createCollisionEffect(x + tailleAlien / 2, y + tailleAlien / 2, ligne, colonne)

                            // Ajouter des points selon le type d'alien
                            jeux.alienTouche(ligne % 3)

                            // Créer une explosion au point d'impact
                            missile.createExplosion(missile.missile.x, missile.missile.y)

                            // Réinitialiser le missile
                            missile.resetMissile()
                            collisionDetected = true

                            // Log pour débuguer
                            Log.d("AlienView", "Collision détectée! Alien détruit à la position $ligne, $colonne")

                            // Vérifier si tous les aliens sont détruits
                            checkAllAliensDestroyed()

                            break@outerLoop // Sortir des deux boucles
                        }
                    }
                }
            }

            if (collisionDetected) {
                playerMissileIterator.remove()
            }
        }

        var lowestAlienY = 0f

        // Trouver l'alien vivant le plus bas
        for (ligne in 0 until lignesAliens) {
            for (colonne in 0 until colonnesAliens) {
                if (aliensVivants[ligne][colonne]) {
                    val y = ligne * (tailleAlien + espaceEntreAliens) + espaceEntreAliens + offsetY
                    val bottomY = y + tailleAlien

                    if (bottomY > lowestAlienY) {
                        lowestAlienY = bottomY
                    }
                }
            }
        }

        // Position Y du joueur (obtenue à partir de la vue du joueur)
        val joueurY = if (::joueurImageView.isInitialized) {
            val location = IntArray(2)
            joueurImageView.getLocationOnScreen(location)
            location[1].toFloat()
        } else {
            height - 100f // Position par défaut si la vue du joueur n'est pas initialisée
        }

        // Vérifier si les aliens ont atteint la position Y du joueur
        if (lowestAlienY >= joueurY - tailleAlien) {
            // Les aliens ont atteint le joueur!
            alienListener?.onAliensReachedPlayer()

            // Optionnel: arrêter temporairement le mouvement
            handler.removeCallbacksAndMessages(null)
        }
    }

    // Méthode pour créer un effet de collision à une position donnée
    private fun createCollisionEffect(x: Float, y: Float, ligne: Int, colonne: Int) {
        val effect = AlienMort(x, y, ligne, colonne)
        collisionEffects.add(effect)
    }

    // Vérifier si tous les aliens sont détruits
    private fun checkAllAliensDestroyed() {
        val lignesAliens = 5
        val colonnesAliens = 10

        var allDestroyed = true
        for (ligne in 0 until lignesAliens) {
            for (colonne in 0 until colonnesAliens) {
                if (aliensVivants[ligne][colonne]) {
                    allDestroyed = false
                    break
                }
            }
            if (!allDestroyed) break
        }

        if (allDestroyed) {
            // Tous les aliens sont détruits, passer au niveau suivant
            jeux.verifierNiveauTermine()
        }
    }

    fun resetAliens(niveau: Int) {
        // Réinitialiser la position des aliens
        offsetX = 0f
        offsetY = 0f

        // Réinitialiser le statut des aliens (tous vivants)
        for (ligne in aliensVivants.indices) {
            for (colonne in aliensVivants[ligne].indices) {
                aliensVivants[ligne][colonne] = true
            }
        }

        // Effacer tous les missiles
        playerMissiles.clear()
        alienMissiles.clear()

        // Effacer tous les effets de collision
        collisionEffects.clear()

        // Augmenter la vitesse en fonction du niveau
        val vitesseBase = 10
        val nouvelleVitesse = vitesseBase + (niveau - 1) * 5

        // Réinitialiser le handler pour les mouvements
        handler.removeCallbacksAndMessages(null)

        // Redémarrer le mouvement avec la nouvelle vitesse
        startMovement(nouvelleVitesse)
    }
}
