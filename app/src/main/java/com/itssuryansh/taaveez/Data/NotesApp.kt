package com.itssuryansh.taaveez.Data

import android.app.Application
import com.itssuryansh.taaveez.Model.NotesDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NotesApp:Application() {
    private val scope : CoroutineScope = CoroutineScope(SupervisorJob())
    lateinit var repository:NotesRepository

    override fun onCreate() {
        super.onCreate()

        initializeRoomDatabase()
    }

    private fun initializeRoomDatabase() {
        val dbInstance = NotesDatabase.getInstance(applicationContext,scope)
        repository = NotesRepository(dbInstance)
    }

    val db by lazy {
        NotesDatabase.getInstance(this,scope)
    }


}