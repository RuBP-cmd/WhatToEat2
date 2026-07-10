package com.rubp.whattoeat

import android.app.Application
import com.rubp.whattoeat.data.local.database.AppDatabase
import com.rubp.whattoeat.data.local.database.getDatabaseBuilder

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(getDatabaseBuilder(this))
    }
}