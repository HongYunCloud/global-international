rootProject.name = "global-international"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://r.irepo.space/maven/") {
            mavenContent {
                includeModuleByRegex(
                    "^org\\.inksnow(\\.ankh-invoke.+|)\$",
                    "^(org.inksnow.|)ankh-invoke(-.+|)\$"
                )
            }
        }
    }
}

include("bukkit")
include("velocity")
include("netty")
