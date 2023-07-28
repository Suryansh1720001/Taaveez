package com.itssuryansh.taaveez

import android.app.Application
import com.itssuryansh.taaveez.database.NotesDatabase

class NotesApp:Application() {
    val db by lazy {
        NotesDatabase.getInstance(this)
    }
}