package com.itssuryansh.taaveez

import android.app.Application
import com.itssuryansh.taaveez.data.database.NotesDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NotesApp:Application() {
    val db by lazy {
        NotesDatabase.getInstance(this)
    }
}