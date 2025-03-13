Yêu cầu cuối cùng cho PrivacySpace

PrivacySpace cần được hoàn thiện với các chức năng: Hidden Apps, Whitelist, Blacklist, App Management, Restart Options (Restart Launcher, Reset Settings App, Reset System).

Áp dụng các kỹ thuật của HMA: hook Zygote, hook Package Manager, reflection, coroutines, MVVM, dependency injection, reactive programming, kiểm tra chữ ký, phát hiện môi trường ảo, hỗ trợ đa phiên bản Android, đa ngôn ngữ.
Cấu trúc dự án phải giống HMA (có module app, xposed, common, file settings.xml với Restart Options và Check Effectiveness).
File Gradle (build.gradle.kts) phải giống HMA, bao gồm task sinh Magic.java và các thư viện như androidx.navigation, kotlinx.serialization.json.
Vui lòng kiểm tra lại các file đã triển khai (MainActivity.kt, AppListFragment.kt, SettingsFragment.kt, FrameworkHookerApi34.kt, v.v.) và đảm bảo chúng đáp ứng đúng yêu cầu. Nếu có lỗi, hãy tự kiểm tra và sửa.