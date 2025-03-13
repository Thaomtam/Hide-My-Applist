package cn.geektang.privacyspace.util

import android.os.Binder
import de.robv.android.xposed.XposedHelpers
import java.io.File

object Utils {
    /**
     * Lấy tên gói từ PackageSettings sử dụng reflection
     */
    fun getPackageNameFromPackageSettings(obj: Any): String {
        return XposedHelpers.getObjectField(obj, "packageName") as String
    }

    /**
     * Thực thi code trong phạm vi Binder local
     */
    inline fun <T> binderLocalScope(crossinline block: () -> T): T {
        val callingUid = Binder.getCallingUid()
        val token = Binder.clearCallingIdentity()
        try {
            return block()
        } finally {
            Binder.restoreCallingIdentity(token)
        }
    }

    /**
     * Kiểm tra xem thiết bị có root không
     */
    fun isRooted(): Boolean {
        return File("/system/xbin/su").exists() || 
               File("/system/bin/su").exists() ||
               File("/sbin/su").exists() ||
               File("/su/bin/su").exists()
    }

    /**
     * Kiểm tra xem ứng dụng có đang chạy trong môi trường ảo không
     */
    fun isVirtualEnvironment(): Boolean {
        return try {
            File("/proc/self/maps").readText().contains("virtual") ||
            System.getProperty("ro.kernel.qemu")?.equals("1") == true
        } catch (e: Exception) {
            false
        }
    }
} 