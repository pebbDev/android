package com.example.infinite_track.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.infinite_track.R

object DialogHelper {

    private fun getCustomFont(context: Context): Typeface? {
        return ResourcesCompat.getFont(context, R.font.sf_compact_medium)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showDialog(
        context: Context,
        title: String?,
        textContent: String?,
        alertType: Int,
        textConfirm: String = "Ok",
        textConfirmSize: Float = 14f,
        onConfirm: () -> Unit = {}
    ): SweetAlertDialog {

        val customFont = getCustomFont(context)

        val dialog = SweetAlertDialog(context, alertType)
            .setTitleText(title)
            .setConfirmText(textConfirm)
            .setContentText(textContent)
            .setConfirmClickListener {
                onConfirm()
                it.dismissWithAnimation()
            }

        dialog.setCanceledOnTouchOutside(true)

        dialog.show()

        val titleText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.title_text)
        val contentText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)
        val confirmButton = dialog.findViewById<Button>(cn.pedant.SweetAlert.R.id.confirm_button)
        val cancelButton = dialog.findViewById<Button>(cn.pedant.SweetAlert.R.id.cancel_button)

        titleText?.apply {
            typeface = Typeface.create(customFont, Typeface.BOLD)
            setTextColor(ContextCompat.getColor(context, R.color.primary))
        }

        contentText?.typeface = customFont
        confirmButton?.typeface = customFont
        confirmButton?.textSize = textConfirmSize
        cancelButton?.typeface = customFont

        confirmButton?.background = createStateListDrawable(context)

        cancelButton?.apply {
            background = createStateListDrawable1(context)
            setTextColor(ContextCompat.getColor(context, R.color.primary))
        }

        return dialog
    }


    private fun createStateListDrawable1(context: Context): StateListDrawable {
        val strokeColor = ContextCompat.getColor(context, R.color.primary)
        val transparentColor = ContextCompat.getColor(context, android.R.color.transparent)

        val normalDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 12f
            setStroke(3, strokeColor)
            setColor(transparentColor)
        }

        val pressedDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 12f
            setStroke(3, strokeColor)
            setColor(transparentColor)
        }

        return StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
            addState(intArrayOf(), normalDrawable)
        }
    }


    fun showDialogLoading(
        context: Context,
        textContent: String?
    ): SweetAlertDialog {
        val dialog = showDialog(
            context = context,
            title = null,
            textContent = textContent,
            alertType = SweetAlertDialog.PROGRESS_TYPE
        )

        dialog.progressHelper.barColor = "#8A3DFF".toColorInt()

        return dialog
    }

    fun showDialogWarning(
        context: Context,
        title: String?,
        textContent: String?,
        onConfirm: () -> Unit = {},
        onDismis: () -> Unit
    ): SweetAlertDialog {
        val dialog = showDialog(
            context = context,
            title = title,
            textContent = textContent,
            alertType = SweetAlertDialog.CUSTOM_IMAGE_TYPE,
            onConfirm = onConfirm
        )

        val contentText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)
        val customImage = dialog.findViewById<ImageView>(cn.pedant.SweetAlert.R.id.custom_image)

        customImage?.layoutParams?.width = 400
        customImage?.layoutParams?.height = 400
        customImage?.requestLayout()
        contentText.textSize = 14f
        dialog.setCustomImage(R.drawable.icon_error)

        dialog.apply {
            setCancelClickListener { onDismis() }
            showCancelButton(true)
            setCancelText("Cancel")
        }

        return dialog
    }

    fun showDialogError(
        context: Context,
        title: String?,
        textContent: String?,
        onConfirm: () -> Unit = {}
    ): SweetAlertDialog {
        return showDialog(
            context = context,
            title = title,
            textContent = textContent,
            alertType = SweetAlertDialog.ERROR_TYPE,
            onConfirm = onConfirm
        )
    }

    fun showDialogSuccess(
        context: Context,
        title: String?,
        textConfirm: String = "OK",
        textContent: String?,
        textConfirmSize: Float = 14f,
        imageRes: Int,
        onConfirm: () -> Unit = {}
    ): SweetAlertDialog {
        val dialog = showDialog(
            context = context,
            title = title,
            textConfirm = textConfirm,
            textContent = textContent,
            alertType = SweetAlertDialog.CUSTOM_IMAGE_TYPE,
            onConfirm = onConfirm,
            textConfirmSize = textConfirmSize
        )

        val customImage = dialog.findViewById<ImageView>(cn.pedant.SweetAlert.R.id.custom_image)
        customImage?.layoutParams?.width = 400
        customImage?.layoutParams?.height = 400
        customImage?.requestLayout()
        dialog.setCustomImage(imageRes)
        return dialog
    }

    fun showCancelableDialogLoading(
        context: Context,
        textContent: String?,
        onCancel: () -> Unit
    ): SweetAlertDialog {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
            .setTitleText(null)
            .setContentText(textContent)

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        dialog.showCancelButton(true)
        dialog.setCancelText("Cancel")
        dialog.setCancelClickListener {
            onCancel()
            it.dismissWithAnimation()
        }
        dialog.setOnCancelListener {
            onCancel()
        }

        dialog.progressHelper.barColor = "#8A3DFF".toColorInt()
        dialog.show()

        // Apply custom styling
        val contentText = dialog.findViewById<TextView>(cn.pedant.SweetAlert.R.id.content_text)
        val cancelButton = dialog.findViewById<Button>(cn.pedant.SweetAlert.R.id.cancel_button)

        val customFont = getCustomFont(context)
        contentText?.typeface = customFont

        cancelButton?.apply {
            typeface = customFont
            background = createStateListDrawable1(context)
            setTextColor(ContextCompat.getColor(context, R.color.primary))
        }

        return dialog
    }
}

private fun createStateListDrawable(context: Context): StateListDrawable {
    val strokeColor = ContextCompat.getColor(context, R.color.primary)
    val pressedColor = ContextCompat.getColor(context, R.color.primary_light)

    val normalDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = 12f
        setColor(strokeColor)
    }

    val pressedDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = 12f
        setColor(pressedColor)
    }

    return StateListDrawable().apply {
        addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        addState(intArrayOf(), normalDrawable)
    }
}
