package cn.geektang.privacyspace.xposed.service

import android.content.Context
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.os.RemoteException
import cn.geektang.privacyspace.common.Constants
import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.common.JsonConfig
import cn.geektang.privacyspace.util.Log
import cn.geektang.privacyspace.xposed.hook.HookChecker
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicReference

class PrivacyService(private val context: Context) : IPrivacyService.Stub() {
    companion object {
        private const val TAG = "PrivacyService"
        private const val CONFIG_FILE = "privacy_config.json"
    }

    private val configRef = AtomicReference<JsonConfig>(JsonConfig())
    private val configFile by lazy { File(context.filesDir, CONFIG_FILE) }

    init {
        loadConfig()
        Log.i(TAG, "PrivacyService đã được khởi tạo")
    }

    override fun getConfig(): JsonConfig {
        enforceCallingPermission()
        return configRef.get()
    }

    override fun updateConfig(config: JsonConfig): Boolean {
        enforceCallingPermission()
        configRef.set(config)
        HookChecker.clearCache()
        return saveConfig()
    }

    override fun getFilterCount(): Int {
        enforceCallingPermission()
        return 0 // Sẽ được cập nhật bởi hooker
    }

    override fun getLogs(): String {
        enforceCallingPermission()
        return "" // Sẽ được cập nhật bởi hooker
    }

    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        try {
            // Kiểm tra tính hợp lệ của giao dịch
            if (code != IBinder.INTERFACE_TRANSACTION && 
                code != IBinder.DUMP_TRANSACTION) {
                enforceCallingPermission()
            }
            
            // Thêm thông tin gỡ lỗi
            Log.d(TAG, "Giao dịch: $code từ UID: ${Binder.getCallingUid()}")
            
            return super.onTransact(code, data, reply, flags)
        } catch (e: RemoteException) {
            Log.e(TAG, "Lỗi trong onTransact", e)
            throw e
        } catch (e: SecurityException) {
            Log.e(TAG, "Lỗi bảo mật trong onTransact", e)
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi không xác định trong onTransact", e)
            return false
        }
    }

    private fun enforceCallingPermission() {
        val callingUid = Binder.getCallingUid()
        if (callingUid != Constants.UID_SYSTEM && callingUid != context.applicationInfo.uid) {
            Log.w(TAG, "Từ chối truy cập từ UID: $callingUid")
            throw SecurityException("Không có quyền truy cập PrivacyService")
        }
    }

    private fun loadConfig(): Boolean {
        try {
            if (!configFile.exists()) {
                Log.i(TAG, "Tệp cấu hình không tồn tại, tạo mới")
                return saveConfig()
            }

            FileInputStream(configFile).use { fis ->
                val bytes = ByteArray(fis.available())
                fis.read(bytes)
                val json = String(bytes)
                val config = JsonConfig.fromJson(json)
                configRef.set(config)
                Log.i(TAG, "Đã tải cấu hình: $config")
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi tải cấu hình", e)
            return false
        }
    }

    private fun saveConfig(): Boolean {
        try {
            val config = configRef.get()
            val json = config.toJson()
            
            // Đảm bảo thư mục tồn tại
            configFile.parentFile?.mkdirs()
            
            FileOutputStream(configFile).use { fos ->
                fos.write(json.toByteArray())
                fos.flush()
            }
            
            Log.i(TAG, "Đã lưu cấu hình: $config")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi lưu cấu hình", e)
            return false
        }
    }
} 