package id.my.sendiko.sembako.core.di

import android.app.Application

class SembakoApplication: Application() {

    companion object {
        lateinit var module: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        module = AppModuleImpl(this)
    }

}