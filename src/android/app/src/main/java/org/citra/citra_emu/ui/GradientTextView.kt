// Copyright Citra Emulator Project / Azahar Emulator Project
// Licensed under GPLv2 or any later version
// Refer to the license.txt file included.

package org.citra.citra_emu.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import org.citra.citra_emu.R

class GradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var fillShader: Shader? = null
    private val outlineColor = ContextCompat.getColor(context, R.color.vr_azahar_wordmark_outline)
    private val outlineWidth = resources.displayMetrics.density * 1.5f

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        updateGradient()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        super.onTextChanged(text, start, before, count)
        updateGradient()
    }

    override fun onDraw(canvas: Canvas) {
        val originalStyle = paint.style
        val originalStrokeWidth = paint.strokeWidth
        val originalColor = paint.color
        val originalShader = paint.shader
        val textValue = text?.toString().orEmpty()
        val textX = compoundPaddingLeft.toFloat()
        val textY = baseline.toFloat()

        paint.shader = null
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = outlineWidth
        paint.color = outlineColor
        canvas.drawText(textValue, textX, textY, paint)

        paint.shader = fillShader ?: originalShader
        paint.style = Paint.Style.FILL
        paint.strokeWidth = originalStrokeWidth
        canvas.drawText(textValue, textX, textY, paint)

        paint.shader = originalShader
        paint.style = originalStyle
        paint.strokeWidth = originalStrokeWidth
        paint.color = originalColor
    }

    private fun updateGradient() {
        val gradientColors = intArrayOf(
            ContextCompat.getColor(context, R.color.vr_azahar_wordmark_gradient_start),
            ContextCompat.getColor(context, R.color.vr_azahar_wordmark_gradient_mid),
            ContextCompat.getColor(context, R.color.vr_azahar_wordmark_gradient_end)
        )
        val gradientStops = floatArrayOf(0f, 0.5f, 1f)
        val textWidth = paint.measureText(text, 0, text.length).coerceAtLeast(1f)
        fillShader = LinearGradient(
            0f,
            0f,
            textWidth,
            0f,
            gradientColors,
            gradientStops,
            Shader.TileMode.CLAMP
        )
        paint.shader = fillShader
        invalidate()
    }
}
