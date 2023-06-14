package com.itssuryansh.taaveez

import android.app.Application

class NotesApp : Application() {
    val db by lazy {
        NotesDatabase.getInstance(this)
    }
}
