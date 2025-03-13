package cn.geektang.privacyspace.service

import android.os.IBinder
import android.os.ServiceManager
import cn.geektang.privacyspace.common.Constants
import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.xposed.UserService
import de.robv.android.xposed.XposedHelpers

class PrivacyService {
    companion object {
        private const val TAG = "PrivacyService"
    }

    private val userService = UserService()

    init {
        try {
            val sm = XposedHelpers.findClass("android.os.ServiceManager", null)
            XposedHelpers.callStaticMethod(
                sm,
                "addService",
                Constants.PRIVACY_SERVICE,
                userService as IBinder,
                true
            )
        } catch (e: Throwable) {
            // Xử lý lỗi
        }
    }

    fun getService(): IPrivacyService? {
        return try {
            IPrivacyService.Stub.asInterface(
                ServiceManager.getService(Constants.PRIVACY_SERVICE)
            )
        } catch (e: Throwable) {
            null
        }
    }
} 