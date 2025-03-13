package cn.geektang.privacyspace.xposed

import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.common.JsonConfig
import cn.geektang.privacyspace.xposed.hook.FrameworkHookerApi34
import java.util.concurrent.CopyOnWriteArrayList
import android.os.IBinder

class HMAService : IPrivacyService.Stub() {

    private val frameworkHooks = CopyOnWriteArrayList<FrameworkHookerApi34>()
    var config: JsonConfig = JsonConfig()

    fun installHooks(classLoader: ClassLoader) {
        val hooker = FrameworkHookerApi34(this)
        frameworkHooks.add(hooker)
        hooker.load(classLoader)
    }

    override fun getConfig(): JsonConfig = config

    override fun updateConfig(config: JsonConfig): Boolean {
        this.config = config
        return true
    }

    override fun getFilterCount(): Int = frameworkHooks.sumOf { it.filterCount.get() }

    override fun getLogs(): String = frameworkHooks.joinToString("\n") { it.logs.toString() }
} 