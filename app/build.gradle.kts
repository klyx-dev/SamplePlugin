plugins {
    alias(libs.plugins.klyx)
}

klyx {
    // path to the plugin icon (PNG or JPG).
    // auto-detected if not set: root dir, case-insensitive match
    //   for "icon.png" or "icon.jpg"
    // included inside the bundle as `icon.<ext>`
    // icon.set(file("icon.png"))

    // readme file. auto-detected: root dir, case-insensitive "readme.md"
    // included inside the bundle as `readme.md`
    // readme.set(file("readme.md"))

    // changelog file. auto-detected: root dir, case-insensitive "changelog.md"
    // included inside the bundle as `changelog.md`
    // changelog.set(file("changelog.md"))

    // additional files/directories to pack into the bundle root.
    // airectories are included with their name as sub-path.
    extraFiles.from(
        // e.g., file("src/main/keepRules"),
        // file("LICENSE")
    )

    // alternative name for the output bundle (without extension).
    // default: rootProject.name
    // outputFileName.set("SamplePlugin")

    // output folder for built .klyx bundles.
    // default: build/klyx/
    outputDirectory = rootProject.file("output")
}

android {
    namespace = "com.klyx.sampleplugin"

    compileSdk {
        version = release(37)
    }

    buildTypes {
        release {
            optimization {
                // Klyx plugins should disable R8 optimization
                // since the plugin class is loaded reflectively
                // from the entryClass in plugin.json
                enable = false
            }
        }
    }

    // klyx-compiler-plugin auto-configures:
    //   defaultConfig.minSdk = 28
    //   compileOptions.sourceCompatibility = JavaVersion.VERSION_21
    //   compileOptions.targetCompatibility = JavaVersion.VERSION_21
}
