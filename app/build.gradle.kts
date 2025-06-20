import com.android.build.api.variant.BuildConfigField
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
}


android {
    namespace = "com.esy.weatherappmvi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.esy.weatherappmvi"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

androidComponents {
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    val key = keystoreProperties["apiKey"] as String

    onVariants { variant ->
        variant.buildConfigFields?.put(
            "WEATHER_API_KEY",
            BuildConfigField(
                type = "String",
                value = "\"$key\"",
                comment = "API key for weather requests"
            )
        )

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.serialization.json)

    //MVIKotlin
    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.coroutines)

    //Decompose
    implementation(libs.decompose.core)
    implementation(libs.decompose.jetpack)

    //Room
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    //Dagger
    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)

    //Glide
    implementation(libs.glide.compose)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    //Icons
    implementation(libs.icons)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}