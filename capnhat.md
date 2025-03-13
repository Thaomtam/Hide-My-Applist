Cảm ơn bạn đã cung cấp các file Gradle (build.gradle.kts) từ dự án Hide My Applist (HMA). Dựa trên các file này và phân tích trước đó về HMA, tôi sẽ áp dụng cấu hình Gradle tương tự cho dự án PrivacySpace mới của bạn, đảm bảo cấu trúc thư mục và các module (app, xposed, common) giống HMA. Tôi cũng sẽ tích hợp các chức năng bạn yêu cầu (Hidden Apps, Whitelist, Blacklist, App Management, Restart Options) và áp dụng các kỹ thuật từ HMA (như hook, AIDL, và các thư viện).

Dưới đây là cách tôi sẽ cấu hình Gradle cho PrivacySpace, dựa trên các file bạn cung cấp, cùng với mã nguồn cập nhật.

Phân tích các file Gradle của HMA
1. common/build.gradle.kts
Plugins:
agp.lib: Android Gradle Plugin cho thư viện.
kotlin: Hỗ trợ Kotlin.
kotlin.serialization: Hỗ trợ Kotlin Serialization.
Android:
Namespace: icu.nullptr.hidemyapplist.common.
BuildConfig: Tạo các hằng số như CONFIG_VERSION, SERVICE_VERSION, MIN_BACKUP_VERSION.
AIDL: Kích hoạt để hỗ trợ IHMAService.aidl.
Dependencies:
kotlinx.serialization.json: Xử lý JSON.
dev.rikka.hidden.stub: Compile-only cho tương thích với Hidden API.
2. xposed/build.gradle.kts
Plugins:
agp.lib: Android Gradle Plugin cho thư viện.
kotlin: Hỗ trợ Kotlin.
refine: Plugin tối ưu hóa (có thể liên quan đến R8/ProGuard).
Android:
Namespace: icu.nullptr.hidemyapplist.xposed.
BuildConfig: Tắt.
AfterEvaluate:
Tạo task để sinh Magic.java chứa thông tin chữ ký từ Keystore (dùng KeystoreHelper).
Đảm bảo KotlinCompile phụ thuộc vào task sinh chữ ký.
Dependencies:
androidx.annotation: Anotation cho JVM.
com.android.tools.build.apksig: Xử lý chữ ký APK.
com.github.kyuubiran.ezxhelper: Hỗ trợ Xposed.
dev.rikka.hidden.compat: Tương thích Hidden API.
de.robv.android.xposed.api: API Xposed (compile-only).
dev.rikka.hidden.stub: Stub Hidden API (compile-only).
3. app/build.gradle.kts
Plugins:
agp.app: Android Gradle Plugin cho ứng dụng.
kotlin, kotlin.serialization, ksp: Hỗ trợ Kotlin và KSP.
nav.safeargs.kotlin: Navigation Safe Args.
autoresconfig, materialthemebuilder: Tạo tài nguyên và chủ đề Material.
gms: Firebase (chỉ trong bản chính thức).
Android:
Namespace: com.tsng.hidemyapplist.
BuildConfig: Bật để tạo hằng số.
ViewBinding: Bật.
Packaging: Loại bỏ các tài nguyên không cần thiết (META-INF, androidx, kotlin, okhttp3).
AutoResConfig:
Tạo LangList class cho danh sách ngôn ngữ.
MaterialThemeBuilder:
Tạo các chủ đề Material với nhiều màu sắc (Red, Pink, Purple, v.v.).
Tạo bảng màu Material Design 3.
AfterEvaluate:
Tạo task để đổi tên APK (ví dụ: HMA-V1.0-release.apk).
Dependencies:
Các thư viện UI: androidx.navigation, androidx.preference, com.google.android.material, dev.rikka.rikkax.material.
Thư viện HTTP: com.github.liujingxing.rxhttp, com.squareup.okhttp3.
Thư viện root: com.github.topjohnwu.libsu.core.
Thư viện tiện ích: com.drakeet.about, com.drakeet.multitype, me.zhanghai.android.appiconloader.
Firebase: com.google.firebase.analytics.ktx (chỉ trong bản chính thức).
Loại trừ androidx.appcompat để tránh xung đột.
Cấu hình Gradle cho PrivacySpace
Dựa trên các file Gradle của HMA, tôi sẽ tạo các file Gradle tương ứng cho PrivacySpace.

1. common/build.gradle.kts
kotlin

Collapse

Wrap

Copy
plugins {
    alias(libs.plugins.agp.lib)
    alias(libs.plugins.refine)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
}

val configVerCode: Int by rootProject.extra
val serviceVerCode: Int by rootProject.extra
val minBackupVerCode: Int by rootProject.extra

android {
    namespace = "cn.geektang.privacyspace.common"

    buildFeatures {
        aidl = true
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("int", "CONFIG_VERSION", configVerCode.toString())
        buildConfigField("int", "SERVICE_VERSION", serviceVerCode.toString())
        buildConfigField("int", "MIN_BACKUP_VERSION", minBackupVerCode.toString())
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(libs.kotlinx.serialization.json)
    compileOnly(libs.dev.rikka.hidden.stub)
}
2. xposed/build.gradle.kts
kotlin

Collapse

Wrap

Copy
import com.android.ide.common.signing.KeystoreHelper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Locale

plugins {
    alias(libs.plugins.agp.lib)
    alias(libs.plugins.refine)
    alias(libs.plugins.kotlin)
}

android {
    namespace = "cn.geektang.privacyspace.xposed"

    buildFeatures {
        buildConfig = false
    }
}

kotlin {
    jvmToolchain(21)
}

afterEvaluate {
    android.libraryVariants.forEach { variant ->
        val variantCapped = variant.name.replaceFirstChar { it.titlecase(Locale.ROOT) }
        val variantLowered = variant.name.lowercase(Locale.ROOT)

        val outSrcDir = layout.buildDirectory.dir("generated/source/signInfo/${variantLowered}")
        val outSrc = outSrcDir.get().file("cn/geektang/privacyspace/Magic.java")
        val signInfoTask = tasks.register("generate${variantCapped}SignInfo") {
            outputs.file(outSrc)
            doLast {
                val sign = android.buildTypes[variantLowered].signingConfig
                outSrc.asFile.parentFile.mkdirs()
                val certificateInfo = KeystoreHelper.getCertificateInfo(
                    sign?.storeType,
                    sign?.storeFile,
                    sign?.storePassword,
                    sign?.keyPassword,
                    sign?.keyAlias
                )
                PrintStream(outSrc.asFile).apply {
                    println("package cn.geektang.privacyspace;")
                    println("public final class Magic {")
                    print("public static final byte[] magicNumbers = {")
                    val bytes = certificateInfo.certificate.encoded
                    print(bytes.joinToString(",") { it.toString() })
                    println("};")
                    println("}")
                }
            }
        }
        variant.registerJavaGeneratingTask(signInfoTask, outSrcDir.get().asFile)

        val kotlinCompileTask = tasks.findByName("compile${variantCapped}Kotlin") as KotlinCompile
        kotlinCompileTask.dependsOn(signInfoTask)
        val srcSet = objects.sourceDirectorySet("magic", "magic").srcDir(outSrcDir)
        kotlinCompileTask.source(srcSet)
    }
}

dependencies {
    implementation(projects.common)

    implementation(libs.androidx.annotation.jvm)
    implementation(libs.com.android.tools.build.apksig)
    implementation(libs.com.github.kyuubiran.ezxhelper)
    implementation(libs.dev.rikka.hidden.compat)
    compileOnly(libs.de.robv.android.xposed.api)
    compileOnly(libs.dev.rikka.hidden.stub)
}
3. app/build.gradle.kts
kotlin

Collapse

Wrap

Copy
val officialBuild: Boolean by rootProject.extra

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.autoresconfig)
    alias(libs.plugins.materialthemebuilder)
    alias(libs.plugins.refine)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nav.safeargs.kotlin)
}

if (officialBuild) {
    plugins.apply(libs.plugins.gms.get().pluginId)
}

android {
    namespace = "cn.geektang.privacyspace"

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    packaging {
        dex.useLegacyPackaging = true
        resources {
            excludes += arrayOf(
                "/META-INF/*",
                "/META-INF/androidx/**",
                "/kotlin/**",
                "/okhttp3/**",
            )
        }
    }
}

kotlin {
    jvmToolchain(21)
}

autoResConfig {
    generateClass.set(true)
    generateRes.set(false)
    generatedClassFullName.set("cn.geektang.privacyspace.util.LangList")
    generatedArrayFirstItem.set("SYSTEM")
}

materialThemeBuilder {
    themes {
        for ((name, color) in listOf(
            "Red" to "F44336",
            "Pink" to "E91E63",
            "Purple" to "9C27B0",
            "DeepPurple" to "673AB7",
            "Indigo" to "3F51B5",
            "Blue" to "2196F3",
            "LightBlue" to "03A9F4",
            "Cyan" to "00BCD4",
            "Teal" to "009688",
            "Green" to "4FAF50",
            "LightGreen" to "8BC3A4",
            "Lime" to "CDDC39",
            "Yellow" to "FFEB3B",
            "Amber" to "FFC107",
            "Orange" to "FF9800",
            "DeepOrange" to "FF5722",
            "Brown" to "795548",
            "BlueGrey" to "607D8F",
            "Sakura" to "FF9CA8"
        )) {
            create("Material$name") {
                lightThemeFormat = "ThemeOverlay.Light.%s"
                darkThemeFormat = "ThemeOverlay.Dark.%s"
                primaryColor = "#$color"
            }
        }
    }
    generatePalette = true
}

fun afterEval() = android.applicationVariants.forEach { variant ->
    val variantCapped = variant.name.replaceFirstChar { it.titlecase(Locale.ROOT) }
    val variantLowered = variant.name.lowercase(Locale.ROOT)

    task<Sync>("build$variantCapped") {
        dependsOn("assemble$variantCapped")
        from(layout.buildDirectory.dir("outputs/apk/$variantLowered"))
        into(layout.buildDirectory.dir("apk/$variantLowered"))
        rename(".*.apk", "PrivacySpace-V${variant.versionName}-${variant.buildType.name}.apk")
    }
}

afterEvaluate {
    afterEval()
}

dependencies {
    implementation(projects.common)
    runtimeOnly(projects.xposed)

    implementation(platform(libs.com.google.firebase.bom))
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.com.drakeet.about)
    implementation(libs.com.drakeet.multitype)
    implementation(libs.com.github.kirich1409.viewbindingpropertydelegate)
    implementation(libs.com.github.liujingxing.rxhttp)
    implementation(libs.com.github.liujingxing.rxhttp.converter.serialization)
    implementation(libs.com.github.topjohnwu.libsu.core)
    implementation(libs.com.google.android.material)
    implementation(libs.com.google.android.gms.play.services.ads)
    implementation(libs.com.google.firebase.analytics.ktx)
    implementation(libs.com.squareup.okhttp3)
    implementation(libs.dev.rikka.hidden.compat)
    implementation(libs.dev.rikka.rikkax.material)
    implementation(libs.dev.rikka.rikkax.material.preference)
    implementation(libs.me.zhanghai.android.appiconloader)
    compileOnly(libs.dev.rikka.hidden.stub)
    ksp(libs.com.github.liujingxing.rxhttp.compiler)
}

configurations.all {
    exclude("androidx.appcompat", "appcompat")
}
4. settings.gradle.kts
kotlin

Collapse

Wrap

Copy
rootProject.name = "PrivacySpace"
include(":app", ":xposed", ":common")
5. gradle.properties
properties

Collapse

Wrap

Copy
org.gradle.jvmargs=-Xmx4g -Dfile.encoding=UTF-8
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official
configVerCode=1
serviceVerCode=1
minBackupVerCode=1
officialBuild=false
6. build.gradle.kts (cấp cao)
kotlin

Collapse

Wrap

Copy
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" apply false
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}
7. gradle/libs.versions.toml
toml

Collapse

Wrap

Copy
[versions]
agp = "8.1.0"
kotlin = "1.9.0"
ezxhelper = "2.9.2"
androidx-navigation = "2.7.0"
androidx-preference = "1.2.1"
material = "1.9.0"
rikkax-material = "2.0.0"
topjohnwu-libsu = "5.2.0"
liujingxing-rxhttp = "3.1.0"
okhttp3 = "4.10.0"
appiconloader = "1.5.0"
firebase-bom = "32.3.0"

[libraries]
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version = "1.5.1" }
androidx-annotation-jvm = { module = "androidx.annotation:annotation-jvm", version = "1.7.0" }
com-android-tools-build-apksig = { module = "com.android.tools.build:apksig", version = "8.1.0" }
com-github-kyuubiran-ezxhelper = { module = "com.github.kyuubiran:EzXHelper", version.ref = "ezxhelper" }
androidx-navigation-fragment-ktx = { module = "androidx.navigation:navigation-fragment-ktx", version.ref = "androidx-navigation" }
androidx-navigation-ui-ktx = { module = "androidx.navigation:navigation-ui-ktx", version.ref = "androidx-navigation" }
androidx-preference-ktx = { module = "androidx.preference:preference-ktx", version.ref = "androidx-preference" }
androidx-swiperefreshlayout = { module = "androidx.swiperefreshlayout:swiperefreshlayout", version = "1.1.0" }
com-drakeet-about = { module = "com.drakeet.about:about", version = "3.0.0" }
com-drakeet-multitype = { module = "com.drakeet.multitype:multitype", version = "4.3.0" }
com-github-kirich1409-viewbindingpropertydelegate = { module = "com.github.kirich1409:viewbindingpropertydelegate", version = "1.5.6" }
com-github-liujingxing-rxhttp = { module = "com.github.liujingxing:rxhttp", version.ref = "liujingxing-rxhttp" }
com-github-liujingxing-rxhttp-converter-serialization = { module = "com.github.liujingxing:rxhttp-converter-serialization", version.ref = "liujingxing-rxhttp" }
com-github-liujingxing-rxhttp-compiler = { module = "com.github.liujingxing:rxhttp-compiler", version.ref = "liujingxing-rxhttp" }
com-github-topjohnwu-libsu-core = { module = "com.github.topjohnwu.libsu:core", version.ref = "topjohnwu-libsu" }
com-google-android-material = { module = "com.google.android.material:material", version.ref = "material" }
com-google-android-gms-play-services-ads = { module = "com.google.android.gms:play-services-ads", version = "22.3.0" }
com-google-firebase-bom = { module = "com.google.firebase:firebase-bom", version.ref = "firebase-bom" }
com-google-firebase-analytics-ktx = { module = "com.google.firebase:firebase-analytics-ktx" }
com-squareup-okhttp3 = { module = "com.squareup.okhttp3:okhttp", version.ref = "okhttp3" }
dev-rikka-hidden-compat = { module = "dev.rikka.hidden:compat", version = "2.0.0" }
dev-rikka-hidden-stub = { module = "dev.rikka.hidden:stub", version = "2.0.0" }
dev-rikka-rikkax-material = { module = "dev.rikka.rikkax:material", version.ref = "rikkax-material" }
dev-rikka-rikkax-material-preference = { module = "dev.rikka.rikkax:material-preference", version.ref = "rikkax-material" }
me-zhanghai-android-appiconloader = { module = "me.zhanghai.android:appiconloader", version.ref = "appiconloader" }
de-robv-android-xposed-api = { module = "de.robv.android.xposed:api", version = "82" }

[plugins]
agp-app = { id = "com.android.application", version.ref = "agp" }
agp-lib = { id = "com.android.library", version.ref = "agp" }
kotlin = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version = "1.9.0-1.0.13" }
nav-safeargs-kotlin = { id = "androidx.navigation.safeargs.kotlin", version.ref = "androidx-navigation" }
autoresconfig = { id = "io.github.turastory.auto-res-config", version = "0.0.5" }
materialthemebuilder = { id = "dev.rikka.tools.materialthemebuilder", version = "2.0.0" }
refine = { id = "dev.rikka.tools.refine", version = "4.3.0" }
gms = { id = "com.google.gms.google-services", version = "4.3.15" }
Cập nhật mã nguồn PrivacySpace
Dựa trên cấu hình Gradle mới, tôi sẽ cập nhật một số file mã nguồn để phù hợp với các thư viện và kỹ thuật từ HMA.

1. common/src/main/java/cn/geektang/privacyspace/common/JsonConfig.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class JsonConfig(
    val whitelist: Set<String> = emptySet(),
    val blacklist: Set<String> = emptySet(),
    val hiddenApps: Set<String> = emptySet()
) {
    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): JsonConfig = Json.decodeFromString(json)
    }
}
2. xposed/src/main/java/cn/geektang/privacyspace/xposed/HMAService.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.xposed

import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.common.JsonConfig
import cn.geektang.privacyspace.util.Log
import java.util.concurrent.CopyOnWriteArrayList

class HMAService : IPrivacyService.Stub() {

    private val frameworkHooks = CopyOnWriteArrayList<IFrameworkHook>()
    private var config: JsonConfig = JsonConfig()

    fun installHooks(classLoader: ClassLoader) {
        val hooker = FrameworkHookerApi34(this)
        frameworkHooks.add(hooker)
        hooker.load(classLoader)
        Log.i("HMAService", "Hooks installed")
    }

    override fun syncConfig(json: String) {
        config = JsonConfig.fromJson(json)
        Log.i("HMAService", "Config synced: $json")
    }

    override fun getFilterCount(): Int = frameworkHooks.sumOf { it.filterCount }

    override fun getLogs(): String = frameworkHooks.joinToString("\n") { it.logs }
}
3. app/src/main/java/cn/geektang/privacyspace/data/ConfigManager.kt
kotlin

Collapse

Wrap

Copy
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
            JsonConfig.fromJson(file.readText())
        } else {
            JsonConfig()
        }
    }
}
4. app/src/main/res/xml/settings.xml (cho SettingsFragment)
xml

Collapse

Wrap

Copy
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
    <PreferenceCategory android:title="Restart Options">
        <SwitchPreference
            android:key="restart_launcher"
            android:title="Restart Launcher"
            android:summary="Restart the launcher to apply changes" />
        <SwitchPreference
            android:key="reset_settings"
            android:title="Reset Settings App"
            android:summary="Reset the settings app to apply changes" />
        <SwitchPreference
            android:key="reset_system"
            android:title="Reset System"
            android:summary="Restart the system to apply changes" />
    </PreferenceCategory>
</PreferenceScreen>
Ghi chú triển khai
Thư viện: Đã tích hợp các thư viện từ HMA, bao gồm kotlinx.serialization.json để thay thế JSON thủ công.
Tính năng: Các chức năng (Hidden Apps, Whitelist, Blacklist, App Management, Restart Options) được giữ nguyên.
Tương lai: Có thể thêm chủ đề Material và hỗ trợ đa ngôn ngữ như HMA bằng cách sử dụng materialthemebuilder và crowdin.yml.
Bạn có muốn tôi phát triển thêm phần UI hoặc tích hợp tính năng mới (như kiểm tra hiệu quả ẩn ứng dụng) không? (Hiện tại là 09:08 AM PDT, thứ Năm, 13/03/2025.)