package com.example.brownfieldplayground

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import java.lang.reflect.InvocationTargetException


class MainActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {
    companion object {
        const val OVERLAY_PERMISSION_REQ_CODE = 1  // Choose any value
    }
    lateinit var reactRootView: ReactRootView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package: $packageName"))
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }

        BridgeManager.loadReactNative(this)

        reactRootView = ReactRootView(this)
        reactRootView?.startReactApplication(
            BridgeManager.reactNativeHost?.reactInstanceManager,
            "MyReactComponent",
            bundleOf("text" to "Hello Brownfield")
        )

        setContentView(reactRootView)

        initializeFlipper()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted
                }
            }
        }
        BridgeManager.reactNativeHost?.reactInstanceManager?.onActivityResult(this, requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        BridgeManager.reactNativeHost?.reactInstanceManager?.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()
        BridgeManager.reactNativeHost?.reactInstanceManager?.onHostResume(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BridgeManager.reactNativeHost?.reactInstanceManager?.onHostDestroy(this)
        reactRootView.unmountReactApplication()
    }

    override fun onBackPressed() {
        BridgeManager.reactNativeHost?.reactInstanceManager?.onBackPressed()
        super.onBackPressed()
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU &&  BridgeManager.reactNativeHost?.reactInstanceManager != null) {
            BridgeManager.reactNativeHost?.reactInstanceManager?.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    private fun initializeFlipper() {
        if (BuildConfig.DEBUG) {
            try {
                val aClass = Class.forName("com.example.brownfieldplayground.ReactNativeFlipper")
                aClass
                    .getMethod(
                        "initializeFlipper",
                        this::class.java,
                        ReactInstanceManager::class.java
                    )
                    .invoke(null, this, BridgeManager.reactNativeHost?.reactInstanceManager)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }
}