package cn.geektang.privacyspace.service

import android.content.Context
import android.os.IBinder
import android.os.ServiceManager
import cn.geektang.privacyspace.common.Constants
import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.common.JsonConfig

class PrivacyServiceClient(private val context: Context) {
    companion object {
        private const val TAG = "PrivacyServiceClient"
    }

    private fun getService(): IPrivacyService {
        val binder: IBinder = ServiceManager.getService(Constants.PRIVACY_SERVICE)
            ?: throw IllegalStateException("Dịch vụ không khả dụng")
        return IPrivacyService.Stub.asInterface(binder)
    }

    fun syncConfig(config: JsonConfig) {
        try {
            getService().updateConfig(config)
        } catch (e: Exception) {
            // Xử lý lỗi khi không thể kết nối với dịch vụ
        }
    }
    
    fun getConfig(): JsonConfig {
        return try {
            getService().config
        } catch (e: Exception) {
            JsonConfig()
        }
    }

    fun getFilterCount(): Int {
        return try {
            getService().filterCount
        } catch (e: Exception) {
            0
        }
    }

    fun getLogs(): String {
        return try {
            getService().logs
        } catch (e: Exception) {
            "Không thể lấy logs: ${e.message}"
        }
    }
} 