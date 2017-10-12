package com.wilderpereira.lmgtfygen

import com.wilderpereira.lmgtfygen.dagger.component.AppComponent
import com.wilderpereira.lmgtfygen.dagger.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Created by Wilder on 24/01/17.
 */
class App : DaggerApplication() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .application(this)
                .build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? = appComponent

    override fun onCreate() {
        super.onCreate()
    }
}
