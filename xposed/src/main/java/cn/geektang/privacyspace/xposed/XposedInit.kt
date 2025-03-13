package cn.geektang.privacyspace.xposed

import android.content.Context
import android.os.Build
import android.os.ServiceManager
import cn.geektang.privacyspace.common.Constants
import cn.geektang.privacyspace.util.Log
import cn.geektang.privacyspace.xposed.hook.FrameworkHookerApi33
import cn.geektang.privacyspace.xposed.hook.FrameworkHookerApi34
import cn.geektang.privacyspace.xposed.hook.IFrameworkHook
import cn.geektang.privacyspace.xposed.service.PrivacyService
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlin.runCatching

class XposedInit : IXposedHookZygoteInit, IXposedHookLoadPackage {
    companion object {
        private const val TAG = "XposedInit"
        private var hook: IFrameworkHook? = null
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        Log.i(TAG, "Khởi tạo Zygote")
        Log.setDebug(true)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "android" || lpparam.processName != "android") {
            return
        }

        Log.i(TAG, "Xử lý gói android")
        
        // Khởi tạo EzXHelper
        EzXHelperInit.initHandleLoadPackage(lpparam)
        EzXHelperInit.setLogTag("PrivacySpace")
        EzXHelperInit.setToastTag("PrivacySpace")

        runCatching {
            // Lấy context hệ thống
            val activityThread = Class.forName("android.app.ActivityThread")
            val systemMain = activityThread.getDeclaredMethod("systemMain")
            val systemContext = activityThread.getDeclaredMethod("getSystemContext")
                .invoke(systemMain.invoke(null)) as Context

            // Khởi tạo dịch vụ
            val service = PrivacyService(systemContext)
            
            // Đăng ký dịch vụ
            ServiceManager.addService(Constants.PRIVACY_SERVICE, service)
            Log.i(TAG, "Đã đăng ký dịch vụ: ${Constants.PRIVACY_SERVICE}")

            // Khởi tạo hook phù hợp với phiên bản Android
            hook = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                    Log.i(TAG, "Sử dụng hook cho Android 14")
                    FrameworkHookerApi34(service)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    Log.i(TAG, "Sử dụng hook cho Android 13")
                    FrameworkHookerApi33(service)
                }
                else -> {
                    Log.e(TAG, "Phiên bản Android không được hỗ trợ: ${Build.VERSION.SDK_INT}")
                    null
                }
            }

            // Tải hook
            hook?.load(lpparam.classLoader)
            Log.i(TAG, "Đã tải hook thành công")
        }.onFailure {
            Log.e(TAG, "Lỗi khi khởi tạo module", it)
        }
    }
} 