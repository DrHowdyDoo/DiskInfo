plugins {
    id("com.android.application")
}

android {
    namespace = "com.drhowdydoo.diskinfo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.drhowdydoo.diskinfo"
        minSdk = 27
        targetSdk = 33
        versionCode = 105
        versionName = "3.8.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("E:\\Backup\\AndroidProjects\\Keys\\exo.jks")
            storePassword = "virgo1999"
            keyAlias = "frozen"
            keyPassword = "virgo1999"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

        }
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("me.zhanghai.android.fastscroll:library:1.2.0")
    implementation("com.google.android.play:core:1.10.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.11")
}