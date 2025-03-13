package cn.geektang.privacyspace.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedEntry : IXposedHookLoadPackage {
    private val xposedInit = XposedInit()

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        xposedInit.handleLoadPackage(lpparam)
    }
} 