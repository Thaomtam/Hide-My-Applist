package cn.geektang.privacyspace.data

import android.content.Context
import cn.geektang.privacyspace.common.JsonConfig
import java.io.File

object ConfigManager {
    private const val CONFIG_FILE = "config.json"

    fun saveConfig(context: Context, config: JsonConfig) {
        context.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE).use {
            it.write(config.toJson().toByteArray())
        }
    }

    fun loadConfig(context: Context): JsonConfig {
        val file = File(context.filesDir, CONFIG_FILE)
        return if (file.exists()) {
            try {
                JsonConfig.fromJson(file.readText())
            } catch (e: Exception) {
                JsonConfig()
            }
        } else {
            JsonConfig()
        }
    }
} 