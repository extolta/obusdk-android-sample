pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://extol.mycloudrepo.io/public/repositories/extol-android")
        }
    }
}

rootProject.name = "obusdk-android-sample"
include(":app")
 