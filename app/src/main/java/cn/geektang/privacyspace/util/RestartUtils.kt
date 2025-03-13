package cn.geektang.privacyspace.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import cn.geektang.privacyspace.R
import java.io.DataOutputStream
import java.io.IOException

/**
 * Tiện ích để khởi động lại các thành phần hệ thống
 */
object RestartUtils {
    private const val TAG = "RestartUtils"
    
    /**
     * Khởi động lại launcher
     */
    fun restartLauncher(context: Context) {
        try {
            // Danh sách các launcher phổ biến
            val launchers = arrayOf(
                "com.android.launcher3/.Launcher",
                "com.google.android.apps.nexuslauncher/.NexusLauncherActivity",
                "com.sec.android.app.launcher/.TwelveKeyProvider",
                "com.miui.home/.launcher.Launcher",
                "com.huawei.android.launcher/.Launcher"
            )
            
            // Thử khởi động lại từng launcher
            for (launcher in launchers) {
                try {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.component = ComponentName.unflattenFromString(launcher)
                    context.startActivity(intent)
                    
                    // Nếu không có lỗi, thoát khỏi vòng lặp
                    break
                } catch (e: Exception) {
                    // Thử launcher tiếp theo
                    continue
                }
            }
            
            // Nếu không thể khởi động lại launcher cụ thể, sử dụng intent mặc định
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            
            Toast.makeText(context, R.string.restart_launcher_success, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi khởi động lại launcher", e)
            Toast.makeText(context, R.string.restart_launcher_failed, Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Đặt lại ứng dụng Cài đặt
     */
    fun resetSettings(context: Context) {
        try {
            // Tắt ứng dụng Cài đặt
            val intent = Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS")
            context.sendBroadcast(intent)
            
            // Mở lại ứng dụng Cài đặt
            val settingsIntent = Intent(android.provider.Settings.ACTION_SETTINGS)
            settingsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(settingsIntent)
            
            Toast.makeText(context, R.string.reset_settings_success, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi đặt lại ứng dụng Cài đặt", e)
            Toast.makeText(context, R.string.reset_settings_failed, Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Khởi động lại hệ thống (yêu cầu quyền root)
     */
    fun resetSystem(context: Context) {
        if (hasRootAccess()) {
            try {
                // Hiển thị thông báo
                Toast.makeText(context, R.string.reset_system_progress, Toast.LENGTH_LONG).show()
                
                // Thực hiện lệnh khởi động lại
                val process = Runtime.getRuntime().exec("su")
                val os = DataOutputStream(process.outputStream)
                os.writeBytes("reboot\n")
                os.writeBytes("exit\n")
                os.flush()
                os.close()
                process.waitFor()
                
                // Nếu không khởi động lại được bằng su, thử phương pháp khác
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
                    powerManager.reboot(null)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi khi khởi động lại hệ thống", e)
                Toast.makeText(context, R.string.reset_system_failed, Toast.LENGTH_SHORT).show()
            }
        } else {
            // Không có quyền root
            Toast.makeText(context, R.string.no_root_access, Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Kiểm tra xem thiết bị có quyền root không
     */
    private fun hasRootAccess(): Boolean {
        try {
            val process = Runtime.getRuntime().exec("su -c id")
            val exitValue = process.waitFor()
            return exitValue == 0
        } catch (e: IOException) {
            return false
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            return false
        }
    }
} 