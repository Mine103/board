package com.kaibeu.board

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

class Util {

    fun toast(ctx: Context, msg: String, short: Boolean) {
        if(short) Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
        else Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

    fun shackBar(layout: View, msg: String, short: Boolean) {
        if(short) Snackbar.make(layout, msg, Snackbar.LENGTH_SHORT).show()
        else Snackbar.make(layout, msg, Snackbar.LENGTH_LONG).show()
    }

    fun showDialog(ctx: Context, title: String, msg: String) {
        val dialog = AlertDialog.Builder(ctx)
        dialog.setTitle(title)
        dialog.setMessage(msg)
        dialog.setNegativeButton("닫기", null)
        dialog.show()
    }

}