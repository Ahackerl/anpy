#include <jni.h>
#include <string>
#include "pocketpy.h"
#include <fstream>

#include <fstream>
#include <vector>

char* ret;
void my_print(pkpy_vm* vm, const char* data, int len){

    ret = new char[len];
    memcpy(ret,data,len);
    ret[len - 1] = '\0';

}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_tppy_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {

    // Create a virtual machine
    VM* vm = new VM();

    pkpy_set_output_handler(reinterpret_cast<pkpy_vm *>(vm), &my_print);

    // Create a list
    vm->exec("import re\n"
"\n"
"def test():\n"
"    results = re.split('，','你好，世界')\n"
"    return results\n"
"\n"
"\n"
"print(test())\n"
, "main.py", EXEC_MODE);


    std::string hello = "Hello from C++ and python " + std::string(ret);
    return env->NewStringUTF(hello.c_str());
}