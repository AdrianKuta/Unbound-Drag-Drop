import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.library.publish)
}

android {
    namespace = "dev.adriankuta.unbounddragdrop"
    version = "0.0.4"

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    mavenPublishing {
        coordinates("dev.adriankuta", "unbound-drag-drop", version.toString())
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
        signAllPublications()
        pom {
            name = "Unbound Drag & Drop"
            description =
                "Unbound Drag & Drop enhances your Android apps by enabling drag and drop across multiple RecyclerViews, unlike the default single RecyclerView restriction. This feature allows users to seamlessly move items between different RecyclerViews, offering a more flexible and intuitive user experience."
            inceptionYear = "2024"
            url = "https://github.com/AdrianKuta/Unbound-Drag-Drop"
            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://github.com/AdrianKuta/Unbound-Drag-Drop/blob/master/LICENSE")
                    distribution.set("repo")
                }
            }
            developers {
                developer {
                    id = "AdrianKuta"
                    name = "Adrian Kuta"
                    url = "https://adriankuta.dev/"
                }
            }
            scm {
                url = "https://github.com/AdrianKuta/Unbound-Drag-Drop"
                connection = "scm:git:git://github.com/AdrianKuta/Unbound-Drag-Drop.git"
                developerConnection = "scm:git:ssh://git@github.com/AdrianKuta/Unbound-Drag-Drop.git"
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}