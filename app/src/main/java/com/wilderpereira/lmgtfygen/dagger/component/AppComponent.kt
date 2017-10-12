package com.wilderpereira.lmgtfygen.dagger.component

import android.app.Application
import com.wilderpereira.lmgtfygen.App
import com.wilderpereira.lmgtfygen.dagger.module.ActivityBindingModule
import com.wilderpereira.lmgtfygen.dagger.module.AppModule
import com.wilderpereira.lmgtfygen.dagger.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

/**
 * Created by Wilder on 25/01/17.
 */
@Singleton
@Component(modules = arrayOf(
        NetworkModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        AppModule::class
))
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: App)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}
