package com.bartex.statesmvvm.common

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bartex.statesmvvm.R

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, 0, 250)
        show()
    }
}

fun getStubAlertDialog(context: Context): AlertDialog {
    return getAlertDialog(context, null, null)
}

fun getAlertDialog(context: Context, title: String?, message: String?): AlertDialog {
    val builder = AlertDialog.Builder(context)
    var finalTitle: String? = context.getString(R.string.dialog_title_stub)
    if (!title.isNullOrBlank()) {
        finalTitle = title
    }
    builder.setTitle(finalTitle)
    if (!message.isNullOrBlank()) {
        builder.setMessage(message)
    }
    builder.setCancelable(true)
    builder.setPositiveButton(R.string.dialog_button_cancel) { dialog, _ -> dialog.dismiss() }
    return builder.create()
}