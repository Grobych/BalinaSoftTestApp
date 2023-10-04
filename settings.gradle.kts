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
    }
}

rootProject.name = "BalinaSoft Test App"
include(":app")
include(":network")
include(":data:login")
include(":data:location")
include(":common")
include(":feature:login")
include(":database")
include(":data:photos")
include(":feature:photos")
include(":feature:camera")
include(":feature:photodetails")
include(":data:comments")
