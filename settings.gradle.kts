rootProject.name = "PrivacySpace"
include(":app", ":xposed", ":common")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
} 