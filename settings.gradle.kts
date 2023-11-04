pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()

        rootProject.name = "Chef.IQ"
        include(":app")
    }
}
