package cn.geektang.privacyspace.ui

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.geektang.privacyspace.R
import cn.geektang.privacyspace.data.ConfigManager
import cn.geektang.privacyspace.service.PrivacyServiceClient
import cn.geektang.privacyspace.common.JsonConfig

class AppListFragment : Fragment() {
    companion object {
        const val TYPE_HIDDEN = 0
        const val TYPE_EXCEPT = 1
        const val TYPE_WHITELIST = 2
        const val TYPE_BLACKLIST = 3
        private const val ARG_TYPE = "type"

        fun newInstance(type: Int): AppListFragment {
            return AppListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TYPE, type)
                }
            }
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppAdapter
    private var type: Int = TYPE_HIDDEN
    private var config by lazy { ConfigManager.loadConfig(requireContext()) }
    private val serviceClient by lazy { PrivacyServiceClient(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(ARG_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        val apps = loadApps()
        adapter = AppAdapter(apps) { app, isChecked ->
            config = when (type) {
                TYPE_HIDDEN -> {
                    if (isChecked) {
                        config.copy(hiddenApps = config.hiddenApps + app.packageName)
                    } else {
                        config.copy(hiddenApps = config.hiddenApps - app.packageName)
                    }
                }
                TYPE_EXCEPT -> {
                    if (isChecked) {
                        config.copy(exceptApps = config.exceptApps + app.packageName)
                    } else {
                        config.copy(exceptApps = config.exceptApps - app.packageName)
                    }
                }
                TYPE_WHITELIST -> {
                    if (isChecked) {
                        config.copy(whitelist = config.whitelist + app.packageName)
                    } else {
                        config.copy(whitelist = config.whitelist - app.packageName)
                    }
                }
                TYPE_BLACKLIST -> {
                    if (isChecked) {
                        config.copy(blacklist = config.blacklist + app.packageName)
                    } else {
                        config.copy(blacklist = config.blacklist - app.packageName)
                    }
                }
                else -> config
            }
            ConfigManager.saveConfig(requireContext(), config)
            serviceClient.syncConfig(config)
        }
        recyclerView.adapter = adapter
    }

    private fun loadApps(): List<ApplicationInfo> {
        val pm = requireContext().packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA).filter {
            it.packageName != requireContext().packageName && 
            (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }
    }

    inner class AppAdapter(
        private val apps: List<ApplicationInfo>,
        private val onCheckedChange: (ApplicationInfo, Boolean) -> Unit
    ) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val appIcon: ImageView = view.findViewById(R.id.app_icon)
            val appName: TextView = view.findViewById(R.id.app_name)
            val appPackage: TextView = view.findViewById(R.id.app_package)
            val checkbox: CheckBox = view.findViewById(R.id.checkbox)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_app, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val app = apps[position]
            val pm = requireContext().packageManager
            
            holder.appIcon.setImageDrawable(app.loadIcon(pm))
            holder.appName.text = app.loadLabel(pm)
            holder.appPackage.text = app.packageName
            
            holder.checkbox.isChecked = when (type) {
                TYPE_HIDDEN -> config.hiddenApps.contains(app.packageName)
                TYPE_EXCEPT -> config.exceptApps.contains(app.packageName)
                TYPE_WHITELIST -> config.whitelist.contains(app.packageName)
                TYPE_BLACKLIST -> config.blacklist.contains(app.packageName)
                else -> false
            }
            
            holder.itemView.setOnClickListener {
                holder.checkbox.isChecked = !holder.checkbox.isChecked
                onCheckedChange(app, holder.checkbox.isChecked)
            }
            
            holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(app, isChecked)
            }
        }

        override fun getItemCount() = apps.size
    }
} 