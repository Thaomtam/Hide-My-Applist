package cn.geektang.privacyspace.xposed

import android.os.Binder
import android.os.IBinder
import cn.geektang.privacyspace.common.Constants
import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.common.JsonConfig

class UserService : IPrivacyService.Stub() {

    private val hmaService = HMAService()

    override fun asBinder(): IBinder = this

    override fun getConfig(): JsonConfig {
        if (Binder.getCallingUid() == Constants.UID_SYSTEM) return JsonConfig()
        return hmaService.config
    }

    override fun updateConfig(config: JsonConfig): Boolean {
        if (Binder.getCallingUid() == Constants.UID_SYSTEM) return false
        hmaService.config = config
        return true
    }

    override fun getFilterCount(): Int = hmaService.getFilterCount()

    override fun getLogs(): String = hmaService.getLogs()
} 