pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "News App Case Study"
include(":app")
include(":news:data")
include(":news:domain")
include(":news:presentation")
include(":core:presentation")
include(":core:domain")
include(":core:data")
include(":favorites:presentation")
include(":favorites:data")
include(":favorites:domain")
