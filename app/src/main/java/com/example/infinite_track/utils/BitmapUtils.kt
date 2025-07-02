package com.example.infinite_track.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap

/**
 * Utility functions for bitmap operations
 */

/**
 * Extension function to convert VectorDrawable to Bitmap
 * @param drawableId The resource ID of the vector drawable
 * @return Bitmap representation of the vector drawable, or a default bitmap if conversion fails
 */
fun Context.bitmapFromVector(drawableId: Int): Bitmap {
    return try {
        val vectorDrawable = ContextCompat.getDrawable(this, drawableId) as? VectorDrawable
        vectorDrawable?.let { drawable ->
            val bitmap = createBitmap(
                if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 48,
                if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 48
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } ?: createDefaultMarkerBitmap()
    } catch (e: Exception) {
        android.util.Log.e("BitmapUtils", "Error converting vector to bitmap", e)
        createDefaultMarkerBitmap()
    }
}

/**
 * Create a default marker bitmap as fallback
 */
private fun createDefaultMarkerBitmap(): Bitmap {
    val size = 48
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)
    return bitmap
}
