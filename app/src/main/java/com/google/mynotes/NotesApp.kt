package com.google.mynotes

import android.app.Application

class NotesApp:Application() {
    val db by lazy {
        NotesDatabase.getInstance(this)
    }
}