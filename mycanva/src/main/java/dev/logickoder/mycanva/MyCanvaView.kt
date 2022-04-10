package dev.logickoder.mycanva

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.absoluteValue

class MyCanvaView(context: Context) : View(context) {
    private lateinit var frame: Rect

    private val color: Int = Color.BLACK
    private val strokeWidth: Float = 12f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private var inFrame = false

    private var motion = 0f to 0f
    private var point = 0f to 0f

    // the paint class encapsulates the color amd style information
    // about how to draw the geometrics.text and bitmaps
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = this@MyCanvaView.color
        strokeWidth = this@MyCanvaView.strokeWidth
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        alpha = 0xff
    }
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)

    // list to store all the strokes drawn by the user on the canvas
    private val paths = mutableListOf<Path>()
    private var currentPath = Path()

    private var bitmap: Bitmap? = null
    private var drawingCanvas: Canvas? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //create a rectangular frame around the drawing
        val inset = 50
        frame = Rect(inset, inset * 2, w - inset, h - inset * 2)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).also {
            drawingCanvas = Canvas(it)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        // save the current state of the canvas before, to draw the background of the canvas
        canvas.save()
        drawingCanvas?.run {
            // default color of the canvas
            drawColor(Color.WHITE)
            // iterate over each stroke and draw it on the canvas
            paths.forEach { path ->
                drawPath(path, paint)
            }
            // draw frame
            drawRect(frame, paint)
        }
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, bitmapPaint) }
        canvas.restore()
    }

    // the below methods manages the touch response of the user on the screen
    private fun touchStart(x: Float, y: Float) {
        // save the current coordinates of the finger
        currentPath = Path().apply {
            reset()
            moveTo(x, y)
        }
        paths += currentPath
        point = x to y
    }

    private fun touchMove(x: Float, y: Float) = with(point) {
        if (!inFrame) {
            inFrame = true
            touchStart(x, y)
        } else {
            val dx = (x - first).absoluteValue
            val dy = (y - second).absoluteValue

            if (dx >= touchTolerance || dy >= touchTolerance) {
                currentPath.quadTo(first, second, (first + x) / 2, (second + y) / 2)
                point = x to y
            }
        }
    }

    private fun touchUp() {
        currentPath.lineTo(point.first, point.second)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val x = event.x
        val y = event.y
        motion = x to y

        if (inFrame()) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> touchStart(x, y)
                MotionEvent.ACTION_MOVE -> touchMove(x, y)
                MotionEvent.ACTION_UP -> touchUp()
            }
            invalidate()
        } else {
            inFrame = false
        }
        return true
    }

    /**
     * True if the current stroke is in drawing frame
     */
    private fun inFrame(): Boolean = with(motion) {
        first.let { it > frame.left + strokeWidth && it < frame.right - strokeWidth } &&
                second.let { it > frame.top + strokeWidth && it < frame.bottom - strokeWidth }
    }
}
