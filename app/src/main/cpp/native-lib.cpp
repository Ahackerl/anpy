#include <jni.h>
#include <string>
#include "pocketpy.h"
#include <fstream>
#include <vector>

#define LOG(...) __android_log_print(ANDROID_LOG_DEBUG, "Native", __VA_ARGS__)

static const char *classPath = "com/example/tppy/PyVm";

JavaVM *gJavaVm;

jbyteArray charToJByteArray(JNIEnv *env, unsigned char *buf, int len) {

    jbyteArray array = env->NewByteArray(len);
    env->SetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte *>(buf));

    return array;
}

char *ret;
void my_print(pkpy_vm *vm, const char *data, int len) {

    ret = new char[len];
    memcpy(ret, data, len);
    ret[len - 1] = '\0';

    JNIEnv *env;
    gJavaVm->AttachCurrentThread(&env, nullptr);
    jclass javaClass = env->FindClass(classPath);
    jmethodID javaCallback = env->GetStaticMethodID(javaClass, "onPyVMCallback", "([B)V");

    env->CallStaticVoidMethod(javaClass, javaCallback,
                              charToJByteArray(env,reinterpret_cast<unsigned char *>(ret), len - 1));

}

VM *vm = new VM();

extern "C" JNIEXPORT void JNICALL
Java_com_example_tppy_PyVm_initVM(
        JNIEnv *env,
        jclass clazz) {

    pkpy_set_output_handler(reinterpret_cast<pkpy_vm *>(vm), &my_print);
    env->GetJavaVM(&gJavaVm);
}


const char *jstringToChar(JNIEnv *env, jstring jstr) {
    const char* __tmp_dn__  = env->GetStringUTFChars(jstr, NULL);
    return __tmp_dn__;
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_tppy_PyVm_lazyModules(
        JNIEnv *env,
        jclass clazz,
        jstring module_source,
        jstring module_name) {

    vm->_lazy_modules[jstringToChar(env, module_name)] = jstringToChar(env, module_source);

}

extern "C" JNIEXPORT void JNICALL
Java_com_example_tppy_PyVm_exec(
        JNIEnv *env,
        jclass clazz,
        jstring module_source) {

    vm->exec(jstringToChar(env, module_source), "main.py", EXEC_MODE);

}