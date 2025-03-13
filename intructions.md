Cảm ơn bạn đã cung cấp phân tích chi tiết về dự án Hide My Applist (HMA). Dựa trên phân tích này, tôi sẽ điều chỉnh và tối ưu hóa cấu trúc thư mục cũng như cách phát triển dự án PrivacySpace mới của bạn để phản ánh chặt chẽ hơn cách hoạt động và tổ chức của HMA. Tôi sẽ giữ các chức năng bạn yêu cầu (Hidden Apps, Whitelist, Blacklist, App Management, Restart Options) và tích hợp các kỹ thuật từ HMA (bao gồm PmsHookTarget34, AIDL, và các kỹ thuật khác như hook Zygote, reflection, v.v.). Tôi cũng sẽ áp dụng gợi ý từ phần "Thêm tính năng mới" để mở rộng dự án nếu cần.

Dựa trên ảnh chụp màn hình và phân tích, tôi sẽ định nghĩa lại cấu trúc thư mục và cung cấp mã nguồn mẫu, đảm bảo tuân thủ cách HMA được xây dựng với ba module chính (app, xposed, common) và các thư viện, kỹ thuật được đề cập.

Phân tích dự án Hide My Applist và áp dụng cho PrivacySpace
1. Quy trình hoạt động áp dụng cho PrivacySpace
Khởi động mô-đun Xposed:
Sử dụng XposedInit.kt trong module xposed để tải PrivacySpace khi hệ thống khởi động.
Hook vào Package Manager Service (PMS):
Hook ServiceManager để bắt sự kiện PMS, tương tự HMA, và khởi tạo PrivacyService.
Thiết lập dịch vụ:
PrivacyService trong module service quản lý cấu hình và hook (Api34, tương lai Api15).
Lọc truy vấn danh sách ứng dụng:
Sử dụng FrameworkHookerApi34 để lọc dựa trên whitelist/blacklist, tương tự cách HMA lọc.
Giao diện người dùng:
MainActivity và các Fragment trong module app cho phép cấu hình JSON.
Kiểm tra hiệu quả:
Thêm công cụ kiểm tra ẩn (tương tự HMA) trong SettingsFragment.
2. Cấu trúc thư mục dựa trên HMA
Dựa trên phân tích và ảnh chụp màn hình, tôi sẽ tổ chức PrivacySpace với ba module (app, xposed, common) như HMA:

text

Collapse

Wrap

Copy
PrivacySpace/
├── .github/                        # CI/CD (GitHub Actions)
│   └── workflows/
│       └── build.yml
├── app/                           # Module ứng dụng chính
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── cn/
│   │   │   │       └── geektang/
│   │   │   │           └── privacyspace/
│   │   │   │               ├── ui/                  # Giao diện người dùng
│   │   │   │               │   ├── MainActivity.kt
│   │   │   │               │   ├── AppListFragment.kt
│   │   │   │               │   └── SettingsFragment.kt
│   │   │   │               ├── data/                # Quản lý dữ liệu
│   │   │   │               │   └── ConfigManager.kt
│   │   │   │               ├── service/             # Dịch vụ UI
│   │   │   │               │   └── PrivacyServiceClient.kt
│   │   │   │               └── util/                # Tiện ích UI
│   │   │   │                   └── AppUtils.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   └── fragment_applist.xml
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── styles.xml
│   │   │   ├── AndroidManifest.xml
│   │   │   └── proguard-rules.pro
│   │   └── test/
├── xposed/                        # Module Xposed
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── cn/
│   │   │   │       └── geektang/
│   │   │   │           └── privacyspace/
│   │   │   │               ├── xposed/              # Logic hook
│   │   │   │               │   ├── XposedEntry.kt
│   │   │   │               │   ├── HMAService.kt    # Dịch vụ hook (tương tự HMA)
│   │   │   │               │   ├── UserService.kt   # Giao diện cho UI
│   │   │   │               │   └── hook/
│   │   │   │               │       ├── FrameworkHookerApi34.kt
│   │   │   │               │       └── HookChecker.kt
│   │   │   │               ├── service/             # Dịch vụ Xposed
│   │   │   │               │   └── PrivacyService.kt
│   │   │   │               └── util/                # Tiện ích Xposed
│   │   │   │                   └── XposedUtils.kt
│   │   │   ├── AndroidManifest.xml
│   │   │   └── proguard-rules.pro
│   │   └── test/
├── common/                        # Module dùng chung
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── cn/
│   │   │   │       └── geektang/
│   │   │   │           └── privacyspace/
│   │   │   │               ├── common/              # Hằng số và cấu hình
│   │   │   │               │   ├── IPrivacyService.aidl
│   │   │   │               │   ├── Constants.kt
│   │   │   │               │   └── JsonConfig.kt
│   │   │   │               └── util/                # Tiện ích chung
│   │   │   │                   └── CommonUtils.kt
│   │   │   ├── AndroidManifest.xml
│   │   │   └── proguard-rules.pro
│   │   └── test/
├── .gitattributes                 # Cấu hình Git
├── .gitignore                     # Loại trừ file
├── README.md                      # Tài liệu
├── README-zh_CN.md                # Tài liệu tiếng Trung
├── banner.png                     # Hình ảnh
├── build.gradle.kts               # Cấu hình Gradle cấp cao
├── crowdin.yml                    # Cấu hình Crowdin
├── gradle.properties              # Thuộc tính Gradle
├── gradle/                        # Script Gradle
│   ├── wrapper/
│   │   ├── gradle-wrapper.jar
│   │   └── gradle-wrapper.properties
│   ├── gradlew
│   └── gradlew.bat
├── settings.gradle.kts            # Cấu hình project
Điểm tương đồng với HMA:
Chia thành ba module: app (UI), xposed (hook), common (chung).
Sử dụng xposed trong xposed/src/main/java cho logic hook, với XposedEntry.kt làm entry point.
Có service trong cả app và xposed để phân biệt UI và Xposed service.
Bao gồm .github, gradle, và các file như build.gradle.kts, settings.gradle.kts.
Điều chỉnh cho PrivacySpace:
Thay icu.nullptr.hidemyapplist bằng cn.geektang.privacyspace.
Thêm FrameworkHookerApi34.kt trong xposed/hook để hỗ trợ Android 14.
Tạo ConfigManager.kt trong app/data để quản lý JSON, tương tự JsonConfig.kt trong common.
File chính và mã nguồn
1. common/src/main/java/cn/geektang/privacyspace/common/IPrivacyService.aidl
aidl

Collapse

Wrap

Copy
package cn.geektang.privacyspace.common;

interface IPrivacyService {
    void syncConfig(String json) = 1;
    int getFilterCount() = 2;
    String getLogs() = 3;
}
2. common/src/main/java/cn/geektang/privacyspace/common/Constants.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.common

object Constants {
    const val UID_SYSTEM = 1000
}
3. common/src/main/java/cn/geektang/privacyspace/common/JsonConfig.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.common

import org.json.JSONObject

data class JsonConfig(
    var whitelist: Set<String> = emptySet(),
    var blacklist: Set<String> = emptySet(),
    var hiddenApps: Set<String> = emptySet()
) {
    fun toJson(): String = JSONObject().apply {
        put("whitelist", whitelist.toTypedArray())
        put("blacklist", blacklist.toTypedArray())
        put("hiddenApps", hiddenApps.toTypedArray())
    }.toString()

    companion object {
        fun fromJson(json: String): JsonConfig {
            val obj = JSONObject(json)
            return JsonConfig(
                whitelist = obj.getJSONArray("whitelist").toSet(),
                blacklist = obj.getJSONArray("blacklist").toSet(),
                hiddenApps = obj.getJSONArray("hiddenApps").toSet()
            )
        }
    }
}

fun JSONArray.toSet(): Set<String> = (0 until length()).map { getString(it) }.toSet()
4. xposed/src/main/java/cn/geektang/privacyspace/xposed/XposedEntry.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.xposed

import cn.geektang.privacyspace.service.PrivacyService
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedEntry : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "android") return

        val classLoader = lpparam.classLoader
        XposedBridge.hookMethod(
            XposedHelpers.findMethodExact(
                "com.android.server.SystemServer",
                classLoader,
                "startOtherServices"
            ),
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val service = PrivacyService()
                    val hooker = FrameworkHookerApi34(service)
                    hooker.load(classLoader)
                }
            }
        )
    }
}
5. xposed/src/main/java/cn/geektang/privacyspace/xposed/HMAService.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.xposed

import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.util.Log
import java.util.concurrent.CopyOnWriteArrayList

class HMAService : IPrivacyService.Stub() {

    private val frameworkHooks = CopyOnWriteArrayList<FrameworkHookerApi34>()
    var config: JsonConfig = JsonConfig()

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
6. xposed/src/main/java/cn/geektang/privacyspace/xposed/UserService.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.xposed

import android.os.Binder
import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.util.Log

class UserService : IPrivacyService.Stub() {

    private val hmaService = HMAService()

    override fun asBinder(): IBinder = this

    override fun syncConfig(json: String) {
        if (Binder.getCallingUid() == Constants.UID_SYSTEM) return
        hmaService.syncConfig(json)
        Log.i("UserService", "Config synced from UI")
    }

    override fun getFilterCount(): Int = hmaService.getFilterCount()

    override fun getLogs(): String = hmaService.getLogs()
}
7. xposed/src/main/java/cn/geektang/privacyspace/xposed/hook/FrameworkHookerApi34.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.xposed.hook

import android.os.Binder
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.findMethodOrNull
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.util.Log
import cn.geektang.privacyspace.util.Utils
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.runCatching

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class FrameworkHookerApi34(private val service: IPrivacyService) : IFrameworkHook {

    companion object {
        private const val TAG = "FrameworkHookerApi34"
    }

    val filterCount = AtomicInteger(0)
    val logs = StringBuilder()
    private val lastFilteredApp = AtomicReference<String?>(null)
    private val getPackagesForUidMethod by lazy {
        findMethod("com.android.server.pm.Computer") { name == "getPackagesForUid" }
    }

    private var hook: XC_MethodHook.Unhook? = null
    private var exphook: XC_MethodHook.Unhook? = null

    override fun load() {
        Log.i(TAG, "Loading hook")
        hook = findMethod("com.android.server.pm.AppsFilterImpl", findSuper = true) {
            name == "shouldFilterApplication"
        }.hookBefore { param ->
            runCatching {
                val snapshot = param.args[0]
                val callingUid = param.args[1] as Int
                if (callingUid == Constants.UID_SYSTEM) return@hookBefore
                val callingApps = Utils.binderLocalScope {
                    getPackagesForUidMethod.invoke(snapshot, callingUid) as Array<String>?
                } ?: return@hookBefore
                val targetApp = Utils.getPackageNameFromPackageSettings(param.args[3])
                for (caller in callingApps) {
                    if (HookChecker.shouldIntercept(caller, targetApp, service)) {
                        param.result = true
                        filterCount.incrementAndGet()
                        val last = lastFilteredApp.getAndSet(caller)
                        if (last != caller) logs.append("Hidden by $caller\n")
                        Log.d(TAG, "Caller: $callingUid, Target: $targetApp")
                        return@hookBefore
                    }
                }
            }.onFailure {
                Log.e(TAG, "Error", it)
                unload()
            }
        }

        exphook = findMethodOrNull("com.android.server.pm.PackageManagerService", findSuper = true) {
            name == "getArchivedPackageInternal"
        }?.hookBefore { param ->
            runCatching {
                val callingUid = Binder.getCallingUid()
                if (callingUid == Constants.UID_SYSTEM) return@hookBefore
                val callingApps = Utils.binderLocalScope {
                    service.getPackagesForUid(callingUid)
                } ?: return@hookBefore
                val targetApp = param.args[0].toString()
                for (caller in callingApps) {
                    if (HookChecker.shouldIntercept(caller, targetApp, service)) {
                        param.result = null
                        filterCount.incrementAndGet()
                        val last = lastFilteredApp.getAndSet(caller)
                        if (last != caller) logs.append("Hidden archived by $caller\n")
                        Log.d(TAG, "Caller: $callingUid, Target: $targetApp")
                        return@hookBefore
                    }
                }
            }.onFailure {
                Log.e(TAG, "Error", it)
                unload()
            }
        }

        // Interception from HMA
        findMethod("com.android.server.pm.PackageManagerService", findSuper = true) {
            name == "getProcMaps"
        }.hookBefore { param ->
            if (param.result.toString().contains("cn.geektang.privacyspace")) {
                param.result = ""
            }
        }
    }

    override fun unload() {
        hook?.unhook()
        hook = null
        exphook?.unhook()
        exphook = null
    }
}

interface IFrameworkHook {
    fun load()
    fun unload()
}
8. xposed/src/main/java/cn/geektang/privacyspace/xposed/hook/HookChecker.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.xposed.hook

import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.common.JsonConfig
import org.json.JSONObject

object HookChecker {
    fun shouldIntercept(callingPackageName: String, targetPackageName: String, service: IPrivacyService): Boolean {
        val config = JsonConfig.fromJson(service.getConfig() ?: "{}")
        return config.blacklist.contains(callingPackageName) ||
                (!config.whitelist.contains(callingPackageName) && config.hiddenApps.contains(targetPackageName))
    }
}
9. app/src/main/java/cn/geektang/privacyspace/ui/MainActivity.kt
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        val adapter = AppListPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Hidden Apps"
                1 -> "Whitelist"
                2 -> "Blacklist"
                3 -> "Settings"
                else -> ""
            }
        }.attach()
    }
}
10. app/src/main/java/cn/geektang/privacyspace/data/ConfigManager.kt
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
11. build.gradle.kts (cấp cao)
kotlin

Collapse

Wrap

Copy
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}
12. app/build.gradle.kts
kotlin

Collapse

Wrap

Copy
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "cn.geektang.privacyspace"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("com.github.kyuubiran:EzXHelper:2.9.2")
    implementation("de.robv.android.xposed:api:82")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
}
13. settings.gradle.kts
kotlin

Collapse

Wrap

Copy
rootProject.name = "PrivacySpace"
include(":app", ":xposed", ":common")
Thêm tính năng mới (theo gợi ý HMA)
Ví dụ: Thêm phương pháp ẩn ứng dụng mới
Tạo NewHidingMethod.kt trong xposed/src/main/java/cn/geektang/privacyspace/xposed/hook/:
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.xposed.hook

import cn.geektang.privacyspace.common.IPrivacyService
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore

class NewHidingMethod(private val service: IPrivacyService) : IFrameworkHook {

    override fun load() {
        findMethod("com.android.server.pm.PackageManagerService", findSuper = true) {
            name == "getInstalledPackages"
        }.hookBefore { param ->
            val callingUid = Binder.getCallingUid()
            if (callingUid != Constants.UID_SYSTEM) {
                param.result = (param.result as List<*>).filter {
                    !HookChecker.shouldIntercept(it.packageName, it.packageName, service)
                }
            }
        }
    }

    override fun unload() {
        // Dọn dẹp
    }
}
Đăng ký trong HMAService.kt:
kotlin

Collapse

Wrap

Copy
fun installHooks(classLoader: ClassLoader) {
    frameworkHooks.add(FrameworkHookerApi34(this))
    frameworkHooks.add(NewHidingMethod(this))
    frameworkHooks.forEach { it.load() }
}
Thêm UI trong SettingsFragment.kt (mẫu cơ bản):
kotlin

Collapse

Wrap

Copy
package cn.geektang.privacyspace.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        findPreference<androidx.preference.SwitchPreference>("new_hiding_method")?.setOnPreferenceChangeListener { _, newValue ->
            // Cập nhật config
            true
        }
    }
}
Ghi chú triển khai
Thư viện: Thêm androidx.navigation, dev.rikka.rikkax.material nếu cần UI nâng cao.
Test: Chạy trên Android 14 với LSPosed, kiểm tra các tab và hook mới.
Tài liệu: Cập nhật README.md với hướng dẫn cài đặt.