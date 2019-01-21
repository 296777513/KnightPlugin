package com.knight.plugin.utils

import java.lang.Exception
import java.lang.reflect.Field

object FieldUtil {

    @Throws(Exception::class)
    fun getField(clazz: Class<*>, target: Any, name: String): Any {
        val field = clazz.getDeclaredField(name)
        field.isAccessible = true
        return field.get(target)
    }

    @Throws(Exception::class)
    fun getField(clazz: Class<*>, name: String): Field {
        val field = clazz.getDeclaredField(name)
        field.isAccessible = true
        return field
    }

    @Throws(Exception::class)
    fun setField(clazz: Class<*>, target: Any, name: String, value: Any) {
        val field = clazz.getDeclaredField(name)
        field.isAccessible = true
        field.set(target, value)
    }
}