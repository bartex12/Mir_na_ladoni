package com.bartex.statesmvvm.common

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.bartex.statesmvvm.R

class AlertDialogFragment : AppCompatDialogFragment() {

    companion object {
        private const val TITLE_EXTRA = "TITLE_EXTRA"
        private const val MESSAGE_EXTRA = "MESSAGE_EXTRA"

        fun newInstance(title: String?, message: String?): AlertDialogFragment {
            val dialogFragment = AlertDialogFragment()
            val args = Bundle()
            args.putString(TITLE_EXTRA, title)
            args.putString(MESSAGE_EXTRA, message)
            dialogFragment.arguments = args
            return dialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var title:String? = ""
        var message:String? = ""
       // var alertDialog = getStubAlertDialog(requireContext())
        val args = arguments
        if (args != null) {
            title = args.getString(TITLE_EXTRA)
            message = args.getString(MESSAGE_EXTRA)
           // alertDialog = getAlertDialog(requireContext(), title, message)
        }
        val builder = AlertDialog.Builder(requireContext())
        var finalTitle: String? = context?.getString(R.string.dialog_title_stub)
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
}
