package com.example.brownfieldplayground

import com.facebook.react.PackageList
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.soloader.SoLoader

object BridgeManager {
    var reactNativeHost: ReactNativeHost? = null

    fun loadReactNative(activity: MainActivity) {
        if (reactNativeHost == null) {
            SoLoader.init(activity, false)
            reactNativeHost = object : ReactNativeHost(activity.application) {
                override fun getUseDeveloperSupport(): Boolean {
                    return BuildConfig.DEBUG
                }

                override fun getPackages(): MutableList<ReactPackage> {
                    return PackageList(application).packages
                }

                override fun getJSMainModuleName(): String {
                    return "index"
                }

                override fun getBundleAssetName(): String? {
                    return "index.android.bundle"
                }
            }
        }

        reactNativeHost?.reactInstanceManager?.createReactContextInBackground()
    }
}