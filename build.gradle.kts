import io.gitlab.arturbosch.detekt.detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath(kotlin("gradle-plugin", version = Versions.KOTLIN))
        classpath(kotlin("serialization", version = Versions.KOTLIN))
        classpath("com.squareup.sqldelight:gradle-plugin:${Versions.SQLDELIGHT}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
        classpath("com.dicedmelon.gradle:jacoco-android:0.1.4")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:8.1.0")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14"
    id("org.jlleitschuh.gradle.ktlint") version "8.1.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/dellisd/transpo")
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        config = files("$rootDir/default-detekt-config.yml")
        filters = ".*build.*,.*/resources/.*,.*/tmp/.*"
        //Optional baseline, uncomment & run gradle command detektBaseline to exclude existing issues
        //baseline = file("detekt-baseline.xml")
    }

}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
