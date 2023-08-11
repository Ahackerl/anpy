plugins {
    id("com.android.application")
}


android {
    namespace = "com.example.tppy"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.tppy"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

tasks.whenTaskAdded {

    if (name.equals("packageDebug")) {

        val inputFile = File(projectDir.absolutePath
                + File.separator
                + "src"
                + File.separator
                + "main"
                + File.separator
                + "python"
                + File.separator
                + "test.py")

        val outputFile = File(projectDir.absolutePath
                + File.separator
                + "src"
                + File.separator
                + "main"
                + File.separator
                + "cpp"
                + File.separator
                + "native-lib.cpp")

        var inputText = inputFile.readText()

        var replaceText = "";
        inputText.lines().forEach { line ->
            // 在这里处理每一行
            println(line)
            replaceText = replaceText + "\"" + line + "\\n\"" + "\n"
        }

        val outputText = "#include <jni.h>\n" +
                "#include <string>\n" +
                "#include \"pocketpy.h\"\n" +
                "#include <fstream>\n" +
                "\n" +
                "#include <fstream>\n" +
                "#include <vector>\n" +
                "\n" +
                "char* ret;\n" +
                "void my_print(pkpy_vm* vm, const char* data, int len){\n" +
                "\n" +
                "    ret = new char[len];\n" +
                "    memcpy(ret,data,len);\n" +
                "    ret[len - 1] = '\\0';\n" +
                "\n" +
                "}\n" +
                "\n" +
                "extern \"C\" JNIEXPORT jstring JNICALL\n" +
                "Java_com_example_tppy_MainActivity_stringFromJNI(\n" +
                "        JNIEnv* env,\n" +
                "        jobject /* this */) {\n" +
                "\n" +
                "    // Create a virtual machine\n" +
                "    VM* vm = new VM();\n" +
                "\n" +
                "    pkpy_set_output_handler(reinterpret_cast<pkpy_vm *>(vm), &my_print);\n" +
                "\n" +
                "    // Create a list\n" +
                "    vm->exec(" +
                replaceText +
                ", \"main.py\", EXEC_MODE);\n" +
                "\n" +
                "\n" +
                "    std::string hello = \"Hello from C++ and python \" + std::string(ret);\n" +
                "    return env->NewStringUTF(hello.c_str());\n" +
                "}"

        outputFile.writeText(outputText)

    }

}



dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}