package com.knight.hotfixlibrary

import android.app.Application
import android.util.Log
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.StringBuilder
import java.lang.reflect.Array

object HotFixManager {
    const val TAG = "HotFixManager"
    const val FIELD_DEX_ELEMENTS = "dexElements"
    const val FIELD_PATH_LIST = "pathList"
    const val CLASS_NAME = "dalvik.system.BaseDexClassLoader"

    const val DEX_SUFFIX = ".dex"
    const val JAR_SUFFIX = ".jar"
    const val APK_SUFFIX = ".apk"
    const val SOURCE_DIR = "patch"

    @Throws(IllegalAccessException::class, NoSuchFieldException::class, ClassNotFoundException::class)
    fun startFix(sourceFile: File, optFile: File, application: Application) {
        if (!sourceFile.exists()) {
            Log.i(TAG, "patch file is not exist~")
            return
        }

        if (!optFile.exists()) {
            optFile.mkdir()
        }

        val sb = StringBuilder()
        val listFiles = sourceFile.listFiles()
        for (i in 0 until listFiles.size) {
            val it = listFiles[i]
            if (it.name.startsWith(SOURCE_DIR) && (it.name.endsWith(DEX_SUFFIX)
                        || it.name.endsWith(JAR_SUFFIX)
                        || it.name.endsWith(APK_SUFFIX))) {// 遍历查找以.dex .jar .apk结尾的文件
                if (i != 0) {
                    sb.append(File.pathSeparator)//使用文件分隔符，将这些文件的路径分隔开
                }
                sb.append(it.absolutePath)
            }
        }
        Log.i(TAG, sb.toString())
        val dexPath = sb.toString()
        val optPath = optFile.absolutePath

        val dexClassLoader = DexClassLoader(dexPath, optPath, null, application.classLoader)//使用DexClassLoader来加载我们自己的类
        val pathElements = getElements(application.classLoader)// 获取PathClassLoader的Element数组
        val dexElements = getElements(dexClassLoader)// 获取DexClassLoader的Element数组
        val combineArray = combineArray(pathElements, dexElements) // 合并上面两个数组
        setDexElements(application.classLoader, combineArray)// 将合并的数组插入到PathClassLoader中
    }

    /**
     * 获取ClassLoader中的dexElements数组
     */
    private fun getElements(classLoader: ClassLoader): Any {
        val baseDexClassLoaderClazz = Class.forName(CLASS_NAME)
        val pathListField = baseDexClassLoaderClazz.getDeclaredField(FIELD_PATH_LIST)
        pathListField.isAccessible = true
        val dexPathList = pathListField.get(classLoader)
        val dexElementsField = dexPathList.javaClass.getDeclaredField(FIELD_DEX_ELEMENTS)
        dexElementsField.isAccessible = true
        return dexElementsField.get(dexPathList)
    }

    /**
     * 合并Element数组
     */
    private fun combineArray(pathElements: Any, dexElements: Any): Any {
        val componentType = pathElements.javaClass.componentType
        val i = Array.getLength(pathElements)
        val j = Array.getLength(dexElements)
        val k = i + j
        val result = Array.newInstance(componentType, k)
        System.arraycopy(dexElements, 0, result, 0, j)
        System.arraycopy(pathElements, 0, result, j, i)
        return result
    }

    /**
     * 使用反射设置合并后的Element数组
     */
    @Throws(ClassNotFoundException::class, NoSuchFieldException::class)
    private fun setDexElements(classLoader: ClassLoader, value: Any) {
        val baseDexClassLoaderClazz = Class.forName(CLASS_NAME)
        val pathListField = baseDexClassLoaderClazz.getDeclaredField(FIELD_PATH_LIST)
        pathListField.isAccessible = true
        val dexPathList = pathListField.get(classLoader)
        val dexElementsField = dexPathList.javaClass.getDeclaredField(FIELD_DEX_ELEMENTS)
        dexElementsField.isAccessible = true
        dexElementsField.set(dexPathList, value)
    }
}