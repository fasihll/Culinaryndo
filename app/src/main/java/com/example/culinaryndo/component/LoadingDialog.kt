package com.example.culinaryndo.component

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import com.example.culinaryndo.R

class LoadingDialog(private val myActivity: Activity) {
    private var activity: Activity = myActivity
    private lateinit var dialog: Dialog
    val displayRectangle = Rect()

    fun startLoadingDialog(image: Uri){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        val layout = inflater.inflate(R.layout.custom_dialog, null)
        val imageView = layout.findViewById<ImageView>(R.id.imagePreview)

        activity.window.decorView.getWindowVisibleDisplayFrame(displayRectangle)

        builder.setView(layout)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val width = (displayRectangle.width() * 0.8f).toInt()
        val height = (displayRectangle.height() * 0.9f).toInt()
        dialog.window?.setLayout(width, height)

        imageView.setImageURI(image)

        dialog.show()
    }

    fun dismisDialog(){
        dialog.dismiss()
    }

}