package cn.geektang.privacyspace.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.MessageDigest
import java.util.Locale

/**
 * Lớp kiểm tra bảo mật cho ứng dụng
 */
object SecurityChecker {
    private const val TAG = "SecurityChecker"
    
    // Danh sách các ứng dụng phát hiện Xposed
    private val xposedDetectors = setOf(
        "com.lvlup.xposed.checker",
        "com.davisale.xposeddetector",
        "com.pyler.xposeddetector",
        "de.robv.android.xposed.installer",
        "org.meowcat.edxposed.manager",
        "io.github.lsposed.manager",
        "com.topjohnwu.magisk"
    )
    
    // Danh sách các ứng dụng phát hiện root
    private val rootDetectors = setOf(
        "com.joeykrim.rootcheck",
        "com.zachspong.temprootchecker",
        "com.jrummyapps.rootchecker",
        "com.devadvance.rootchecker",
        "com.joeykrim.rootcheckpro"
    )
    
    /**
     * Kiểm tra xem ứng dụng có đang chạy trong môi trường Xposed hay không
     */
    fun isRunningInXposed(): Boolean {
        return try {
            val stackTrace = Thread.currentThread().stackTrace
            for (element in stackTrace) {
                if (element.className.contains("de.robv.android.xposed") ||
                    element.className.contains("org.lsposed") ||
                    element.className.contains("com.elderdrivers.riru") ||
                    element.className.contains("me.weishu.exposed")) {
                    return true
                }
            }
            
            // Kiểm tra các lớp Xposed
            try {
                Class.forName("de.robv.android.xposed.XposedBridge")
                return true
            } catch (e: ClassNotFoundException) {
                // Không tìm thấy lớp
            }
            
            false
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi kiểm tra Xposed", e)
            false
        }
    }
    
    /**
     * Kiểm tra xem ứng dụng có đang chạy trong môi trường giả lập hay không
     */
    fun isRunningInEmulator(): Boolean {
        return try {
            (Build.FINGERPRINT.startsWith("generic") ||
                    Build.FINGERPRINT.startsWith("unknown") ||
                    Build.MODEL.contains("google_sdk") ||
                    Build.MODEL.contains("Emulator") ||
                    Build.MODEL.contains("Android SDK built for x86") ||
                    Build.MANUFACTURER.contains("Genymotion") ||
                    Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                    "google_sdk" == Build.PRODUCT ||
                    System.getProperty("ro.kernel.qemu") == "1")
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi kiểm tra giả lập", e)
            false
        }
    }
    
    /**
     * Kiểm tra xem ứng dụng có bị sửa đổi hay không
     */
    fun isAppModified(context: Context): Boolean {
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName, PackageManager.GET_SIGNATURES)
            
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }
            
            if (signatures.isEmpty()) {
                return true
            }
            
            val signatureBytes = signatures[0].toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val signatureHash = bytesToHex(md.digest(signatureBytes))
            
            // Kiểm tra hash chữ ký với giá trị đã biết
            // Lưu ý: Thay thế bằng hash chữ ký thực tế của ứng dụng
            val expectedHash = "YOUR_APP_SIGNATURE_HASH_HERE"
            
            return signatureHash != expectedHash
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi kiểm tra chữ ký ứng dụng", e)
            return true
        }
    }
    
    /**
     * Kiểm tra xem thiết bị có bị root hay không
     */
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }
    
    /**
     * Kiểm tra xem có ứng dụng phát hiện Xposed nào được cài đặt không
     */
    fun hasXposedDetectors(context: Context): Boolean {
        return hasPackagesInstalled(context, xposedDetectors)
    }
    
    /**
     * Kiểm tra xem có ứng dụng phát hiện root nào được cài đặt không
     */
    fun hasRootDetectors(context: Context): Boolean {
        return hasPackagesInstalled(context, rootDetectors)
    }
    
    /**
     * Kiểm tra xem có các gói được chỉ định nào được cài đặt không
     */
    private fun hasPackagesInstalled(context: Context, packages: Set<String>): Boolean {
        val pm = context.packageManager
        for (packageName in packages) {
            try {
                pm.getPackageInfo(packageName, 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                // Gói không được cài đặt
            }
        }
        return false
    }
    
    /**
     * Kiểm tra root bằng cách kiểm tra các tệp hệ thống
     */
    private fun checkRootMethod1(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su"
        )
        
        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }
    
    /**
     * Kiểm tra root bằng cách thử thực thi lệnh su
     */
    private fun checkRootMethod2(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readLine()
            reader.close()
            
            output != null && output.lowercase(Locale.getDefault()).contains("uid=0")
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Kiểm tra root bằng cách kiểm tra các ứng dụng quản lý root
     */
    private fun checkRootMethod3(): Boolean {
        val rootApps = arrayOf(
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.topjohnwu.magisk"
        )
        
        try {
            for (app in rootApps) {
                if (File("/data/app/$app-1.apk").exists() ||
                    File("/data/app/$app-2.apk").exists() ||
                    File("/data/app/$app.apk").exists()) {
                    return true
                }
            }
        } catch (e: Exception) {
            // Bỏ qua lỗi
        }
        
        return false
    }
    
    /**
     * Chuyển đổi mảng byte thành chuỗi hex
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }
} 