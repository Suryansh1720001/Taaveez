package com.itssuryansh.taaveez.Util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.itssuryansh.taaveez.Data.NotesDao
import com.itssuryansh.taaveez.Model.NotesEntity
import com.itssuryansh.taaveez.ViewModels.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertBoxBuilder {


    companion object{
        fun DialogBoxBuilder(context: Context,notesViewModel: NotesViewModel):AlertDialog.Builder{

            val builder = AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Warning")
                .setMessage("Do you want to delete all of your notes ?")
                .setPositiveButton("YES",DialogInterface.OnClickListener { dialogInterface, i ->

              //  var notesList = notesViewModel.getAllNotesFromViewModel().value

                        //if there is atleast one list item on recycler view.
                        MainScope().launch {
                            withContext(Dispatchers.IO) {
                                notesViewModel.deleteAllFromViewModel()
                            }
                        }

                })
                .setNegativeButton("NO",DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            return builder

        }
    }
}