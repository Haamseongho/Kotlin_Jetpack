import groovy.lang.GString
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetHierarchy.SourceSetTree.Companion.main

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.chapter3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chapter3"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}
val ktlint: Configuration by configurations.creating
val detekt: Configuration by configurations.creating
configurations {
    ktlint
    detekt
}


val detektTask = tasks.register<JavaExec>("detekt") {
   // main = "io.gitlab.arturbosch.detekt.cli.Main"
    classpath = detekt

    val input = projectDir
    val exclude = ".*/build/.*,.*/resources/.*"
    val config: String = "$projectDir/detekt.yml"
    val params = listOf("-i", input, "-c", config, "-ex", exclude)

    args(params)
}

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.3.1") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
    detekt("io.gitlab.arturbosch.detekt:detekt-cli:1.23.6")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}
val ktlintCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}

tasks.check {
    dependsOn(ktlintCheck)
    dependsOn(detektTask)
}
tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "-F",
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}
//// Kotlin DSL
//tasks.withType<Detekt>().configureEach {
//    reports {
//        xml.required.set(true)
//        html.required.set(true)
//        txt.required.set(true)
//        sarif.required.set(true)
//        md.required.set(true)
//    }
//}