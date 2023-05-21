package com.itssuryansh.taaveez.Util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.itssuryansh.taaveez.ViewModels.NotesViewModel

class AlertBoxBuilder {


    companion object{
        fun DialogBoxBuilder(context: Context,notesViewModel: NotesViewModel):AlertDialog.Builder{

            val builder = AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Warning")
                .setMessage("Do you want to delete all of your notes ?")
                .setPositiveButton("YES",DialogInterface.OnClickListener { dialogInterface, i ->
                    notesViewModel.deleteAllFromViewModel()
                    Toast.makeText(context,"All records deleted !",Toast.LENGTH_SHORT)
                        .show()
                })
                .setNegativeButton("NO",DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            return builder

        }
    }
}