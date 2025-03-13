package cn.geektang.privacyspace.xposed.hook

import cn.geektang.privacyspace.common.IPrivacyService
import cn.geektang.privacyspace.util.Log
import java.util.concurrent.ConcurrentHashMap

object HookChecker {
    private const val TAG = "HookChecker"
    
    // Cache kết quả kiểm tra để tăng hiệu suất
    private val interceptCache = ConcurrentHashMap<String, Boolean>()
    
    // Danh sách các ứng dụng hệ thống được phép truy cập
    private val systemAllowList = setOf(
        "com.android.settings",
        "com.android.systemui",
        "com.android.launcher3",
        "com.google.android.apps.nexuslauncher",
        "com.android.permissioncontroller",
        "com.google.android.packageinstaller"
    )
    
    // Danh sách các ứng dụng được biết là kiểm tra danh sách ứng dụng
    private val knownScanners = setOf(
        "com.antivirus",
        "com.avast.android.mobilesecurity",
        "com.avg.cleaner",
        "com.bitdefender.security",
        "com.cleanmaster.mguard",
        "com.eset.ems2.gp",
        "com.kms.free",
        "com.lookout",
        "com.mcafee.security.safefamily",
        "com.qihoo.security",
        "com.symantec.mobile.threat.detection",
        "com.tencent.qqpimsecure",
        "org.malwarebytes.antimalware"
    )
    
    /**
     * Kiểm tra xem có nên chặn ứng dụng gọi truy cập vào ứng dụng mục tiêu hay không
     * 
     * @param callerPackage Gói ứng dụng gọi
     * @param targetPackage Gói ứng dụng mục tiêu
     * @param service Dịch vụ riêng tư để kiểm tra cấu hình
     * @return true nếu nên chặn, false nếu không
     */
    fun shouldIntercept(callerPackage: String, targetPackage: String, service: IPrivacyService): Boolean {
        // Kiểm tra cache trước
        val cacheKey = "$callerPackage:$targetPackage"
        val cachedResult = interceptCache[cacheKey]
        if (cachedResult != null) {
            return cachedResult
        }
        
        // Không chặn nếu caller là ứng dụng hệ thống được phép
        if (systemAllowList.contains(callerPackage)) {
            interceptCache[cacheKey] = false
            return false
        }
        
        // Luôn chặn các ứng dụng quét đã biết
        if (knownScanners.contains(callerPackage)) {
            Log.d(TAG, "Chặn ứng dụng quét đã biết: $callerPackage")
            interceptCache[cacheKey] = true
            return true
        }
        
        try {
            // Kiểm tra cấu hình từ dịch vụ
            val config = service.config
            
            // Nếu caller nằm trong blacklist, luôn chặn
            if (config.blacklist.contains(callerPackage)) {
                Log.d(TAG, "Chặn ứng dụng trong blacklist: $callerPackage")
                interceptCache[cacheKey] = true
                return true
            }
            
            // Nếu caller nằm trong whitelist, không chặn
            if (config.whitelist.contains(callerPackage)) {
                interceptCache[cacheKey] = false
                return false
            }
            
            // Nếu ứng dụng mục tiêu nằm trong danh sách ẩn
            if (config.hiddenApps.contains(targetPackage)) {
                // Nếu caller không nằm trong danh sách ngoại lệ
                if (!config.exceptApps.contains(callerPackage)) {
                    interceptCache[cacheKey] = true
                    return true
                }
            }
            
            // Không chặn trong các trường hợp khác
            interceptCache[cacheKey] = false
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Lỗi khi kiểm tra cấu hình", e)
            // Mặc định không chặn nếu có lỗi
            return false
        }
    }
    
    /**
     * Xóa cache để làm mới kết quả kiểm tra
     */
    fun clearCache() {
        interceptCache.clear()
    }
} 