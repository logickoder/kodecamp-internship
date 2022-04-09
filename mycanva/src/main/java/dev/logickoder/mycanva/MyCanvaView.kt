package dev.logickoder.mycanva

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

private const val STROKE_WIDTH = 12f

class MyCanvaView(context: Context) : View(context) {
    private lateinit var frame: Rect

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private var currentX = 0f
    private var currentY = 0f

    private val bgColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.black, null)

    private var path = Path()

    private val drawing = Path()

    private val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //create a rectangular frame around the drawing
        val inset = 50
        frame = Rect(inset, inset * 2, w - inset, h - inset * 2)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
        canvas?.run {
            drawColor(bgColor)
            drawPath(drawing, paint)
            drawPath(path, paint)
            drawRect(frame, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            motionTouchEventX = event.x
            motionTouchEventY = event.y
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> if (inFrame()) touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        invalidate()

        return true
    }

    private fun touchUp() {
        drawing.addPath(path)
        path.reset()
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY
        }
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    /**
     * True if the current stroke is in drawing frame
     */
    private fun inFrame(): Boolean {
        return motionTouchEventX.let { it > frame.left && it < frame.right } &&
                motionTouchEventY.let { it > frame.top && it < frame.bottom }
    }
}
