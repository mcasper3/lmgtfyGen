package com.wilderpereira.lmgtfygen.dagger.component

import com.wilderpereira.lmgtfygen.dagger.module.MainModule
import com.wilderpereira.lmgtfygen.dagger.module.NetworkModule
import com.wilderpereira.lmgtfygen.presentation.main.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Wilder on 25/01/17.
 */
@Singleton
@Component(modules = arrayOf(NetworkModule::class, MainModule::class))
interface MainComponent {
    fun inject(activity: MainActivity)
}