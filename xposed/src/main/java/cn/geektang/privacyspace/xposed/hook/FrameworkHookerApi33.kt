package cn.geektang.privacyspace.xposed.hook

import android.os.Binder
import android.os.Build
import androidx.annotation.RequiresApi
import cn.geektang.privacyspace.common.Constants
import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.util.Log
import cn.geektang.privacyspace.util.Utils
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.findMethodOrNull
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.runCatching

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class FrameworkHookerApi33(private val service: IPrivacyService) : IFrameworkHook {

    companion object {
        private const val TAG = "FrameworkHookerApi33"
    }

    val filterCount = AtomicInteger(0)
    val logs = StringBuilder()
    private val lastFilteredApp = AtomicReference<String?>(null)
    private val getPackagesForUidMethod by lazy {
        findMethod("com.android.server.pm.Computer") { name == "getPackagesForUid" }
    }

    private var hook: XC_MethodHook.Unhook? = null
    private var exphook: XC_MethodHook.Unhook? = null
    private var procMapHook: XC_MethodHook.Unhook? = null

    override fun load(classLoader: ClassLoader) {
        Log.i(TAG, "Loading hook for Android 13")
        
        // Kiểm tra môi trường
        if (Utils.isVirtualEnvironment()) {
            Log.w(TAG, "Running in virtual environment")
        }
        
        if (Utils.isRooted()) {
            Log.i(TAG, "Device is rooted")
        }
        
        // Hook shouldFilterApplication
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
                        if (last != caller) logs.append("Ẩn bởi $caller\n")
                        Log.d(TAG, "Caller: $callingUid, Target: $targetApp")
                        return@hookBefore
                    }
                }
            }.onFailure {
                Log.e(TAG, "Lỗi khi hook shouldFilterApplication", it)
                logs.append("Lỗi: ${it.message}\n")
                unload()
            }
        }

        // Hook getArchivedPackageInternal
        exphook = findMethodOrNull("com.android.server.pm.PackageManagerService", findSuper = true) {
            name == "getArchivedPackageInternal"
        }?.hookBefore { param ->
            runCatching {
                val callingUid = Binder.getCallingUid()
                if (callingUid == Constants.UID_SYSTEM) return@hookBefore
                
                val callingApps = Utils.binderLocalScope {
                    XposedHelpers.callStaticMethod(
                        XposedHelpers.findClass("android.app.ActivityThread", null),
                        "getPackageManager"
                    ).let { pm ->
                        XposedHelpers.callMethod(pm, "getPackagesForUid", callingUid) as Array<String>?
                    }
                } ?: return@hookBefore
                
                val targetApp = param.args[0].toString()
                
                for (caller in callingApps) {
                    if (HookChecker.shouldIntercept(caller, targetApp, service)) {
                        param.result = null
                        filterCount.incrementAndGet()
                        val last = lastFilteredApp.getAndSet(caller)
                        if (last != caller) logs.append("Ẩn gói lưu trữ bởi $caller\n")
                        Log.d(TAG, "Caller: $callingUid, Target: $targetApp (archived)")
                        return@hookBefore
                    }
                }
            }.onFailure {
                Log.e(TAG, "Lỗi khi hook getArchivedPackageInternal", it)
                logs.append("Lỗi: ${it.message}\n")
                unload()
            }
        }

        // Hook getProcMaps để ẩn PrivacySpace
        procMapHook = findMethod("com.android.server.pm.PackageManagerService", findSuper = true) {
            name == "getProcMaps"
        }.hookBefore { param ->
            if (param.result.toString().contains("cn.geektang.privacyspace")) {
                param.result = ""
                Log.d(TAG, "Đã ẩn PrivacySpace khỏi procmaps")
            }
        }
        
        Log.i(TAG, "Hooks đã được cài đặt thành công")
    }

    override fun unload() {
        Log.i(TAG, "Unloading hooks")
        hook?.unhook()
        hook = null
        exphook?.unhook()
        exphook = null
        procMapHook?.unhook()
        procMapHook = null
    }
} 