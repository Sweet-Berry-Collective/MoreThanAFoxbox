pluginManagement {
	repositories {
		maven { url = uri("https://maven.fabricmc.net/") }
		gradlePluginPortal()
	}
}

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("libs.versions.toml"))
		}
	}
}
