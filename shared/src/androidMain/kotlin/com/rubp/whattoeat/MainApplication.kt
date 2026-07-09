package com.rubp.whattoeat

import android.app.Application
import com.rubp.whattoeat.data.local.database.AppDatabaseContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabaseContext.context = this
    }
}