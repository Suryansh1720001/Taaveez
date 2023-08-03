package com.itssuryansh.taaveez

import android.app.Application
import com.itssuryansh.taaveez.database.TaaveezDatabase

class TaaveezApp:Application() {
    val db by lazy {
        TaaveezDatabase.getInstance(this)
    }
}