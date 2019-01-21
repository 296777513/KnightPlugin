package com.knight.plugin.utils

import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import java.lang.Exception
import java.lang.reflect.Array
import java.lang.reflect.Field

object InjectUtil {
    const val TAG = "InjectUtil"
    const val CLASS_BASE_DES_CLASSLOADER = "dalvik.system.BaseDexClassLoader"
    const val CLASS_DEX_PATH_LIST = "dalvik.system.DexPathList"
    const val FIELD_PATH_LIST = "pathList"
    const val FIELD_DES_ELEMENTS = "dexElements"


    @Throws(Exception::class)
    fun inject(context: Context, origin: ClassLoader) {
        var pluginFile =
            context.getExternalFilesDir("plugin")// /sotrage/emulated/0/Android/data/$packageName/files/plugin
        if (pluginFile == null || !pluginFile.exists() || pluginFile.listFiles().isEmpty()) {
            Log.i(TAG, "this plugin file is not exist")
            return
        }
        pluginFile = pluginFile.listFiles()[0] // 获取插件apk文件
        val optimizeFile = context.getFileStreamPath("plugin") // /data/data/$packageName/files/plugin
        if (!optimizeFile.exists()) {
            optimizeFile.mkdirs()
        }
        val pluginClassLoader = DexClassLoader(pluginFile.absolutePath, optimizeFile.absolutePath, null, origin)
        val pluginDexPathList = FieldUtil.getField(
            Class.forName(CLASS_BASE_DES_CLASSLOADER), pluginClassLoader,
            FIELD_PATH_LIST
        )
        val pluginElements = FieldUtil.getField(
            Class.forName(CLASS_DEX_PATH_LIST),
            pluginDexPathList,
            FIELD_DES_ELEMENTS
        ) // 拿到插件中的Elements

        val originDexPathList = FieldUtil.getField(
            Class.forName(CLASS_BASE_DES_CLASSLOADER), origin,
            FIELD_PATH_LIST
        )
        val originElements =
            FieldUtil.getField(Class.forName(CLASS_DEX_PATH_LIST), originDexPathList, FIELD_DES_ELEMENTS)

        val array = combineArray(originElements, pluginElements) // 合并数组
        FieldUtil.setField(
            Class.forName(CLASS_DEX_PATH_LIST),
            originDexPathList,
            FIELD_DES_ELEMENTS,
            array
        )// 设置回pathClassLoader
        Log.i(TAG, "plugin success to load")
    }


    fun combineArray(pathElements: Any, dexElements: Any): Any {
        val componentType = pathElements.javaClass.componentType
        val i = Array.getLength(pathElements)
        val j = Array.getLength(dexElements)
        val k = i + j
        val result = Array.newInstance(componentType, k)
        System.arraycopy(dexElements, 0, result, 0, j)
        System.arraycopy(pathElements, 0, result, j, i)
        return result
    }
}