import com.squareup.sqldelight.gradle.SqlDelightDatabase
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("androidx.navigation.safeargs.kotlin")
    id("jacoco-android")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)
    defaultConfig {
        applicationId = "ca.llamabagel.transpo"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "git describe --tag".execute().text.trim()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val props = Properties()
        val localPropsFile = project.rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            props.load(localPropsFile.inputStream())
        }

        if (props.containsKey("api.endpoint") && props.containsKey("mapbox.key")) {
            buildConfigField("String", "API_ENDPOINT", "\"${props.getProperty("api.endpoint")}\"")
            buildConfigField("String", "MAPBOX_KEY", "\"${props.getProperty("mapbox.key")}\"")
        } else {
            throw GradleException("mapbox.key and api.endpoint not declared in local.properties")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        setSourceCompatibility(1.8)
        setTargetCompatibility(1.8)
    }

    dataBinding {
        isEnabled = true
    }
    androidExtensions {
        isExperimental = true
    }

    kotlinOptions {
        this as KotlinJvmOptions
        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to arrayOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}")
    implementation("androidx.appcompat:appcompat:${Versions.APPCOMPAT}")
    implementation("androidx.core:core-ktx:${Versions.KTX}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}")
    implementation("com.google.android.material:material:${Versions.MATERIAL}")
    implementation("com.squareup.sqldelight:android-driver:${Versions.SQLDELIGHT}")
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:${Versions.SQLDELIGHT}")
    implementation("ca.llamabagel.transpo:shared:${Versions.TRANSPO}")
    implementation("androidx.fragment:fragment-ktx:${Versions.FRAGMENT}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}")
    implementation("androidx.work:work-runtime-ktx:${Versions.WORK_MANAGER}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.RETROFIT_SERIALIZATION}")
    implementation("com.google.dagger:dagger:${Versions.DAGGER}")
    kapt("com.google.dagger:dagger-compiler:${Versions.DAGGER}")
    kapt("com.google.dagger:dagger-android-processor:${Versions.DAGGER}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.SERIALIZATION}")
    compileOnly("com.squareup.inject:assisted-inject-annotations-dagger2:${Versions.DAGGER_ASSIST}")
    kapt("com.squareup.inject:assisted-inject-processor-dagger2:${Versions.DAGGER_ASSIST}")
    implementation("com.squareup.okhttp3:okhttp:${Versions.OKHTTP}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}")
    implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:${Versions.MAPBOX}")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:${Versions.MAPBOX_JAVA_SDK}")

    testImplementation("androidx.arch.core:core-testing:${Versions.LIFECYCLE}")
    testImplementation("org.mockito:mockito-core:${Versions.MOCKITO}")
    testImplementation("org.mockito:mockito-inline:${Versions.MOCKITO_INLINE}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.MOCKITO_KOTLIN}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}")
    androidTestImplementation("androidx.work:work-testing:${Versions.WORK_MANAGER}")
    testImplementation("junit:junit:${Versions.JUNIT}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}")
    androidTestImplementation("androidx.test:rules:${Versions.TEST_RULES}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.TEST_JUNIT}")
    testImplementation("com.squareup.sqldelight:sqlite-driver:${Versions.SQLDELIGHT}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.OKHTTP}")
}

sqldelight {
    // TODO: Replace this with proper DSL method once new version of sqldelight is released
    methodMissing("TransitDatabase", arrayOf(closureOf<SqlDelightDatabase> {
        packageName = "ca.llamabagel.transpo.data.db"
    }))
}

jacoco {
    toolVersion = "0.8.3"
}

jacocoAndroidUnitTestReport {
    html.enabled(false)
    xml.enabled(true)
}
