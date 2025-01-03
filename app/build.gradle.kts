plugins {
    alias(libs.plugins.application.android)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    alias(libs.plugins.room.plugin)
}

room {
    schemaDirectory("$projectDir/schemas/")
}

android {
    namespace = "io.github.livenlearnaday.countrylistkotlin"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.livenlearnaday.countrylistkotlin"
        minSdk =  libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildFeatures {
        viewBinding = true
        buildConfig = true

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }


    testOptions {
        animationsDisabled = true
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
        unitTests.all {
            it.jvmArgs(
                listOf(
                    "-Djdk.attach.allowAttachSelf=true",
                    "-XX:+StartAttachListener",
                    "-Djdk.instrument.traceUsage"
                )
            )
        }
    }

}

dependencies {

    implementation(libs.androidx.activity)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)


    //lifecycle
    implementation(libs.bundles.lifecycle)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Timber logging
    implementation(libs.jakewharton.timber)

    // database
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)



    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // retrofit
    implementation(libs.squareup.okhttp3.okhttp)
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.converter.gson)

    //Image loading
    implementation(libs.bundles.coil)

    // Hilt
    implementation (libs.hilt.google.android)
    kapt (libs.hilt.google.compiler)
    kapt (libs.hilt.androidx.compiler)


    //Hilt Testing
    // For Robolectric tests
    testImplementation (libs.hilt.testing.google)
    // hilt testing
    kaptTest (libs.hilt.google.compiler)
    // For instrumented tests
    androidTestImplementation (libs.hilt.testing.google)
    // hilt testing
    kaptAndroidTest (libs.hilt.google.compiler)

    //testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.coroutinesTest)
    debugImplementation (libs.androidx.fragment.testing)


}

kapt.correctErrorTypes = true




