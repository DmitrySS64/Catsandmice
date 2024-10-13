package ru.sergeevdmitry8i11.catsandmice

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.size
import kotlin.math.atan2
import kotlin.math.hypot

class GameActivity : AppCompatActivity() {
    private val mice = mutableListOf<ImageView>()
    private var clickCount = 0
    private  var mouseClickCount = 0
    private lateinit var gameLayout: FrameLayout
    private lateinit var tvClicks: TextView
    private lateinit var tvHits: TextView

    private lateinit var mouseImages: List<Int>

    private lateinit var dbHelper: DbHelper
    private val startTime = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        mouseImages = loadMouseImages()

        val miceCount = intent.getIntExtra(MICE_COUNT, 1)
        val speed = intent.getIntExtra(SPEED, 200)
        val size = 100 * intent.getIntExtra(SIZE, 1)

        if (savedInstanceState != null){
            clickCount = savedInstanceState.getInt(CLICK_COUNT, 0)
            mouseClickCount = savedInstanceState.getInt(HIT_COUNT, 0)
        }

        dbHelper = DbHelper(this)

        gameLayout = findViewById(R.id.main)
        tvClicks = findViewById(R.id.tv_clicks)
        tvHits = findViewById(R.id.tv_hits)

        updateClicks()
        updateHits()

        gameLayout.setOnClickListener{
            clickCount++
            updateClicks()
        }

        gameLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                gameLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                repeat(miceCount){
                    val mouse = createRandomMouse(size)
                    setMouseToRandomPosition(mouse)
                    gameLayout.addView(mouse)
                    mice.add(mouse)

                    mouse.setOnClickListener{
                        clickCount++
                        mouseClickCount++
                        updateClicks()
                        updateHits()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrate()
                        moveMouseToRandomPoint(mouse, speed)
                    }
                    moveMouseToRandomPoint(mouse, speed)
                }
            }
        })
    }
    private fun createMouse(size: Int): ImageView {
        return ImageView(this).apply {
            setImageResource(R.drawable.mouse_1)
            layoutParams = FrameLayout.LayoutParams(size, size)
        }
    }
    private fun setMouseToRandomPosition(mouse: ImageView){
        val randomX = (0..(gameLayout.width - mouse.width)).random().toFloat()
        val randomY = (0..(gameLayout.height - mouse.height)).random().toFloat()
        mouse.x = randomX
        mouse.y = randomY
    }
    private fun moveMouseToRandomPoint(mouse: ImageView, speed: Int){
        val endX = (0..(gameLayout.width - mouse.width)).random().toFloat()
        val endY = (0..(gameLayout.height - mouse.height)).random().toFloat()
        moveMouse(mouse, speed, endX, endY)
    }

    private fun moveMouse(mouse: ImageView, speed: Int, endX: Float, endY: Float){
        val deltaX = endX - mouse.x
        val deltaY = endY - mouse.y

        val angle = atan2(deltaY, deltaX)
        val targetRotation = angle * (180 / Math.PI.toFloat())

        var scale = -1f

        if (targetRotation > 90 || targetRotation < -90) scale = 1f

        mouse.animate()
            //.rotation(targetRotation)
            .scaleX(scale)
            .setDuration(200L)
            .start()
        val distance = hypot(deltaX, deltaY)
        val duration = (distance/speed * 1000).toLong()
        mouse.animate()
            .x(endX)
            .y(endY)
            .setDuration(duration)
            .withEndAction{
                moveMouseToRandomPoint(mouse, speed)
            }
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        val gameTime = ((System.currentTimeMillis() - startTime) / 1000).toInt()
        dbHelper.insertGameResult(clickCount, mouseClickCount, formatTime(gameTime))
    }


    private fun updateClicks(){
        tvClicks.text = getString(R.string.game_total_clicks, clickCount)
    }
    private fun updateHits(){
        tvHits.text = getString(R.string.game_mouse_clicks, mouseClickCount)
    }
    @SuppressLint("DefaultLocale")
    private fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrate(){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    companion object{
        const val MICE_COUNT = "MICE_COUNT"
        const val SPEED = "SPEED"
        const val SIZE = "SIZE"
        const val CLICK_COUNT = "CLICK_COUNT"
        const val HIT_COUNT = "HIT_COUNT"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CLICK_COUNT, clickCount)
        outState.putInt(HIT_COUNT, mouseClickCount)
    }

    fun loadMouseImages() : List<Int> {
        val typedArray: TypedArray = resources.obtainTypedArray(R.array.mouses)
        val images = List(typedArray.length()) {i-> typedArray.getResourceId(i,0)}
        typedArray.recycle()
        return images
    }

    private fun createRandomMouse(size: Int): ImageView {
        val randomImage = mouseImages.random()
        return ImageView(this).apply {
            setImageResource(randomImage)
            layoutParams = FrameLayout.LayoutParams(size, size)
        }
    }
}