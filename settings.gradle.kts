pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
        maven ("url https://jitpack.io") // add like this
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven (" url 'https://jitpack.io") // add like this
    }
}
rootProject.name = "StayFit"
include (":app")
