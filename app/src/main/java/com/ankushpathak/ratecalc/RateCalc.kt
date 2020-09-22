package com.ankushpathak.ratecalc

import androidx.multidex.MultiDexApplication

class RateCalc : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}