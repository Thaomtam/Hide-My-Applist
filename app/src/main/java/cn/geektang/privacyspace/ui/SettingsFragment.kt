package cn.geektang.privacyspace.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import cn.geektang.privacyspace.R
import cn.geektang.privacyspace.data.ConfigManager
import cn.geektang.privacyspace.service.PrivacyServiceClient
import com.github.topjohnwu.superuser.Shell
import cn.geektang.privacyspace.util.RestartUtils

class SettingsFragment : PreferenceFragmentCompat() {

    private val serviceClient by lazy { PrivacyServiceClient(requireContext()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        findPreference<Preference>("check_status")?.setOnPreferenceClickListener {
            checkModuleStatus()
            true
        }

        findPreference<Preference>("reset_config")?.setOnPreferenceClickListener {
            resetConfig()
            true
        }

        findPreference<Preference>("about")?.setOnPreferenceClickListener {
            openGitHub()
            true
        }

        findPreference<SwitchPreferenceCompat>("new_hiding_method")?.setOnPreferenceChangeListener { _, newValue ->
            val config = ConfigManager.loadConfig(requireContext())
            // Cập nhật cấu hình với phương pháp ẩn mới
            ConfigManager.saveConfig(requireContext(), config)
            serviceClient.syncConfig(config)
            true
        }
        
        // Xử lý tùy chọn khởi động lại
        findPreference<SwitchPreferenceCompat>("restart_launcher")?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                restartLauncher()
                findPreference<SwitchPreferenceCompat>("restart_launcher")?.isChecked = false
            }
            false
        }
        
        findPreference<SwitchPreferenceCompat>("reset_settings")?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                resetSettings()
                findPreference<SwitchPreferenceCompat>("reset_settings")?.isChecked = false
            }
            false
        }
        
        findPreference<SwitchPreferenceCompat>("reset_system")?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                resetSystem()
                findPreference<SwitchPreferenceCompat>("reset_system")?.isChecked = false
            }
            false
        }
        
        // Thêm chức năng kiểm tra hiệu quả ẩn ứng dụng
        findPreference<Preference>("check_effectiveness")?.setOnPreferenceClickListener {
            checkEffectiveness()
            true
        }
    }

    private fun checkModuleStatus() {
        try {
            val count = serviceClient.getFilterCount()
            val logs = serviceClient.getLogs()
            
            val message = if (count > 0) {
                "Module đang hoạt động! Đã lọc $count lần.\n\nLogs:\n$logs"
            } else {
                "Module đã được cài đặt nhưng chưa lọc ứng dụng nào."
            }
            
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Module chưa được kích hoạt hoặc không hoạt động: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    private fun checkEffectiveness() {
        val pm = requireContext().packageManager
        val installedApps = pm.getInstalledPackages(0).map { it.packageName }
        val config = ConfigManager.loadConfig(requireContext())
        val hiddenApps = config.hiddenApps
        val visibleApps = installedApps.filterNot { hiddenApps.contains(it) }
        
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.effectiveness_check)
            .setMessage(getString(R.string.effectiveness_result, 
                visibleApps.size, 
                hiddenApps.size, 
                hiddenApps.joinToString("\n")))
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    private fun resetConfig() {
        val config = ConfigManager.loadConfig(requireContext()).copy(
            whitelist = emptySet(),
            blacklist = emptySet(),
            hiddenApps = emptySet(),
            exceptApps = emptySet()
        )
        ConfigManager.saveConfig(requireContext(), config)
        serviceClient.syncConfig(config)
        
        Toast.makeText(requireContext(), "Đã đặt lại cấu hình", Toast.LENGTH_SHORT).show()
    }

    private fun openGitHub() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://github.com/geektang/privacyspace")
        }
        startActivity(intent)
    }
    
    private fun restartLauncher() {
        context?.let { RestartUtils.restartLauncher(it) }
    }
    
    private fun resetSettings() {
        context?.let { RestartUtils.resetSettings(it) }
    }
    
    private fun resetSystem() {
        context?.let { RestartUtils.resetSystem(it) }
    }
} 