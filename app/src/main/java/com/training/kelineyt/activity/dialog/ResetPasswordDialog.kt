package com.training.kelineyt.activity.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.training.kelineyt.R

fun Fragment.setUpBottomSheetDialog(
    onSendClick:(String)->Unit
){

    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStyle)
    val view =layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialog.apply {
        setContentView(view)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        show()
    }



    val edEmail = view.findViewById<EditText>(R.id.ed_resetEmail)
    val buttonSend = view.findViewById<Button>(R.id.btn_send)
    val buttonCancel = view.findViewById<Button>(R.id.btn_cancel)

    buttonSend.setOnClickListener {
        val email = edEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }
    buttonCancel.setOnClickListener {
        dialog.dismiss()
    }



}