package com.github.sendiko.penghitungsembako.core.di

import android.app.Application

class SembakoApplication: Application() {

    lateinit var module: AppModule

    override fun onCreate() {
        super.onCreate()
        module = AppModuleImpl(this)
    }

}