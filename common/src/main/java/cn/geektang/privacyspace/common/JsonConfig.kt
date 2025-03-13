package cn.geektang.privacyspace.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class JsonConfig(
    val whitelist: Set<String> = emptySet(),
    val blacklist: Set<String> = emptySet(),
    val hiddenApps: Set<String> = emptySet(),
    val exceptApps: Set<String> = emptySet()
) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): JsonConfig = Json.decodeFromString(json)
    }
} 