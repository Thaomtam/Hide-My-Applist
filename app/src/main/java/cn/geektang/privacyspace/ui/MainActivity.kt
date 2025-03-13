package cn.geektang.privacyspace.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.geektang.privacyspace.BuildConfig
import cn.geektang.privacyspace.R
import cn.geektang.privacyspace.databinding.ActivityMainBinding
import cn.geektang.privacyspace.util.SecurityChecker
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Kiểm tra bảo mật
        performSecurityChecks()

        // Thiết lập ViewPager2 và TabLayout
        setupViewPager()
    }

    private fun setupViewPager() {
        val pagerAdapter = MainPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        // Thiết lập TabLayout với ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_hidden_apps)
                1 -> getString(R.string.tab_except_apps)
                2 -> getString(R.string.tab_whitelist)
                3 -> getString(R.string.tab_blacklist)
                else -> "Tab ${position + 1}"
            }
        }.attach()
    }

    private fun performSecurityChecks() {
        // Chỉ kiểm tra trong bản phát hành
        if (!BuildConfig.DEBUG) {
            // Kiểm tra môi trường giả lập
            if (SecurityChecker.isRunningInEmulator()) {
                showSecurityAlert(getString(R.string.emulator_detected))
                return
            }

            // Kiểm tra ứng dụng phát hiện Xposed
            if (SecurityChecker.hasXposedDetectors(this)) {
                showSecurityAlert(getString(R.string.xposed_detector_found))
                return
            }

            // Kiểm tra ứng dụng phát hiện root
            if (SecurityChecker.hasRootDetectors(this)) {
                showSecurityAlert(getString(R.string.root_detector_found))
                return
            }

            // Kiểm tra sửa đổi ứng dụng
            if (SecurityChecker.isAppModified(this)) {
                showSecurityAlert(getString(R.string.app_modified))
                return
            }
        }
    }

    private fun showSecurityAlert(message: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.security_alert)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        val message = getString(R.string.about_message, BuildConfig.VERSION_NAME)
        AlertDialog.Builder(this)
            .setTitle(R.string.about)
            .setMessage(message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    private inner class MainPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AppListFragment.newInstance(AppListFragment.TYPE_HIDDEN)
                1 -> AppListFragment.newInstance(AppListFragment.TYPE_EXCEPT)
                2 -> AppListFragment.newInstance(AppListFragment.TYPE_WHITELIST)
                3 -> AppListFragment.newInstance(AppListFragment.TYPE_BLACKLIST)
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}