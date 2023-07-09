package com.example.android_verticalseekbar_example

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.roundToInt

class VerticalSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var minProgress: Int = 0
        set(value) {
            if (value < 0) throw IllegalArgumentException("It must be minProgress >= 0.")
            if (field == value) return
            field = value
            invalidate()
        }
    var maxProgress: Int = 100
        set(value) {
            if (value <= minProgress) throw IllegalArgumentException("It must be maxProgress > minProgress.")
            if (field == value) return
            field = value
            invalidate()
        }
    var progress: Int = minProgress
        set(value) {
            val coercedValue: Int = value.coerceAtLeast(minProgress).coerceAtMost(maxProgress)
            if (field == coercedValue) return
            field = coercedValue
            onProgressChange?.invoke(field)
            invalidate()
        }
    var onProgressChange: ((progress: Int) -> Unit)? = null
    private val track = object {
        var drawable: Drawable = ShapeDrawable(RectShape()).apply {
            paint.color = 0x22000000.toInt()
        }
        var width: Int = 0
        var height: Int = 0
        val left: Int
            get() = 0
        val top: Int
            get() = (this@VerticalSeekBar.height - height) / 2
        val right: Int
            get() = this@VerticalSeekBar.width
        val bottom: Int
            get() = top + height
    }
    private val thumb = object {
        var drawable: Drawable = ShapeDrawable(OvalShape()).apply {
            paint.color = 0xFF000000.toInt()
        }
        var width: Int = 0
        var height: Int = 0
        val left: Int
            get() = 0
        val top: Int
            get() = ((maxProgress - progress) * track.height.toFloat() / (maxProgress - minProgress)).roundToInt()
        val right: Int
            get() = width
        val bottom: Int
            get() = top + height
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.VerticalSeekBar, defStyleAttr, defStyleRes) {
            minProgress = getInt(R.styleable.VerticalSeekBar_min, minProgress)
            maxProgress = getInt(R.styleable.VerticalSeekBar_max, maxProgress)
            progress = getInt(R.styleable.VerticalSeekBar_progress, progress)
            getDrawable(R.styleable.VerticalSeekBar_track)?.let {
                track.drawable = it
            }
            getDrawable(R.styleable.VerticalSeekBar_thumb)?.let {
                thumb.drawable = it
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth: Int = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val measuredHeight: Int = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        thumb.width = measuredWidth
        thumb.height = thumb.width
        track.width = measuredWidth
        track.height = measuredHeight - thumb.height
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        track.let {
            it.drawable.setBounds(it.left, it.top, it.right, it.bottom)
            it.drawable.draw(canvas)
        }
        thumb.let {
            it.drawable.setBounds(it.left, it.top, it.right, it.bottom)
            it.drawable.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val dh: Float = track.height.toFloat() / (maxProgress - minProgress)
        progress = maxProgress - ((event.y - track.top + (dh * 0.5f)) / dh).toInt()
        return true
    }
}
