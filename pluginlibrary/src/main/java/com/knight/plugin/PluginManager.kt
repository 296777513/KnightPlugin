package com.knight.plugin

import android.annotation.SuppressLint
import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.util.Log
import com.knight.plugin.utils.FieldUtil
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Array
import java.lang.reflect.Proxy
import kotlin.Exception


object PluginManager {
    const val TAG = "InjectUtil"
    const val CLASS_BASE_DES_CLASSLOADER = "dalvik.system.BaseDexClassLoader"
    const val CLASS_DEX_PATH_LIST = "dalvik.system.DexPathList"
    const val FIELD_PATH_LIST = "pathList"
    const val FIELD_DES_ELEMENTS = "dexElements"

    const val TRANSFER_INTENT = "transfer_intent"

    var placeHolderActivityPath = "com.knight.plugin.PlaceHolderActivity"

    @SuppressLint("PrivateApi")
    @Throws(Exception::class)
    @JvmStatic
    fun hookActivityThreadInstrumentation(application: Application) {
        val activityThreadClazz = Class.forName("android.app.ActivityThread")
        val activityThreadField = activityThreadClazz.getDeclaredField("sCurrentActivityThread")
        activityThreadField.isAccessible = true
        val activityThread = activityThreadField.get(null)

        val instrumentationField = activityThreadClazz.getDeclaredField("mInstrumentation")
        instrumentationField.isAccessible = true
        val instrumentation = instrumentationField.get(activityThread) as Instrumentation
        val proxy = InstrumentationProxy(instrumentation, application.packageManager)
        instrumentationField.set(activityThread, proxy)
    }


    @Throws(Exception::class)
    @JvmStatic
    fun initPlugin(
        application: Application,
        pluginPath: String,
        placeHolderActivityPath: String = "com.knight.plugin.PlaceHolderActivity"
    ): Resources {
        val pluginFile = File(pluginPath)
        if (!pluginFile.exists()) {
            throw RuntimeException("plugin file is not exist")
        }
        this.placeHolderActivityPath = placeHolderActivityPath
        inject(application, application.classLoader, pluginPath)
        hookActivityThreadInstrumentation(application)

//        hookAMS()
//        hookH()
//        hookClassLoader(application)
        return initPluginResource(application, pluginPath)
    }

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun hookClassLoader(context: Application) {
        // 获取Application类的mLoadedApk属性值
        val mLoadedApk = FieldUtil.getField(context::class.java.superclass, context, "mLoadedApk")
        // 获取其mClassLoader属性值以及属性字段
        val mClassLoader = FieldUtil.getField(mLoadedApk.javaClass, mLoadedApk, "mClassLoader") as ClassLoader
        val mClassLoaderField = FieldUtil.getField(mLoadedApk.javaClass, "mClassLoader")
        // 替换成自己的ClassLoader
        mClassLoaderField.set(mLoadedApk, object : ClassLoader() {
            @Throws(ClassNotFoundException::class)
            override fun loadClass(name: String): Class<*> {
//                            var name = name
//                            // 替换Activity
//                            if (name.endsWith("MainActivity2")) {
//                                Log.d(TAG, "loadClass: name = $name")
//                                name = name.replace("MainActivity2", "MainActivity3")
//                                Log.d(TAG, "loadClass: 替换后name = $name")
//                            }
                Log.i("liyachao1", "loadClass Name: $name")
                return mClassLoader.loadClass(name)
            }

            override fun findClass(name: String?): Class<*> {
                Log.i("liyachao1", "findClass Name: $name")
                return super.findClass(name)
            }
        })

    }

    @Throws(Exception::class)
    private fun initPluginResource(context: Context, pluginPath: String): Resources {
        val clazz = AssetManager::class.java
        val assetManager = clazz.newInstance()
        val method = clazz.getMethod("addAssetPath", String::class.java)
        method.invoke(assetManager, pluginPath)
        return Resources(assetManager, context.resources.displayMetrics, context.resources.configuration)
    }

    @SuppressLint("PrivateApi")
    @JvmStatic
    @Throws(Exception::class)
    private fun hookAMS() {
        val singleton = if (Build.VERSION.SDK_INT >= 26) { //大于等于8.0
            val clazz = Class.forName("android.app.ActivityManager")
            FieldUtil.getField(clazz, null, "IActivityManagerSingleton") //拿到静态字段
        } else { // 8.0以下
            val activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative")
            FieldUtil.getField(
                activityManagerNativeClazz,
                null,
                "gDefault"
            )// get the static field
        }

        val singleClazz = Class.forName("android.util.Singleton")
        val getMethod = singleClazz.getMethod("get")
        val iActivityManager = getMethod.invoke(singleton)
        val iActivityManagerClazz = Class.forName("android.app.IActivityManager")
        val proxy = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            arrayOf(iActivityManagerClazz),
            IActivityManagerProxy(iActivityManager)
        )
        FieldUtil.setField(singleClazz, singleton, "mInstance", proxy)
    }

    @JvmStatic
    @Throws(Exception::class)
    private fun hookH() {
        val activityThreadClazz = Class.forName("android.app.ActivityThread")
        val activityThread =
            FieldUtil.getField(activityThreadClazz, null, "sCurrentActivityThread")
        val mH = FieldUtil.getField(activityThreadClazz, activityThread, "mH")
        FieldUtil.setField(Handler::class.java, mH, "mCallback", HCallback())
    }

    @Throws(Exception::class)
    private fun inject(context: Context, origin: ClassLoader, pluginPath: String) {
        val optimizeFile = context.getFileStreamPath("plugin") // /data/data/$packageName/files/plugin
        if (!optimizeFile.exists()) {
            optimizeFile.mkdirs()
        }
        val pluginClassLoader = DexClassLoader(pluginPath, optimizeFile.absolutePath, null, origin)
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
            FieldUtil.getField(
                Class.forName(CLASS_DEX_PATH_LIST),
                originDexPathList,
                FIELD_DES_ELEMENTS
            )

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