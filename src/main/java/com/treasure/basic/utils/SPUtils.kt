package com.treasure.basic.utils;

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * SharedPreferences 工具类
 * 使用前需要先初始化 SpUtils.init(context)
 */
object SpUtils {

    private lateinit var sharedPreferences: SharedPreferences
    private const val DEFAULT_SP_NAME = "default_sp"

    /**
     * 初始化，建议在Application中调用
     */
    fun init(context: Context, spName: String = DEFAULT_SP_NAME) {
        sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    /**
     * 保存数据
     */
    fun put(key: String, value: Any) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported value type: ${value.javaClass.simpleName}")
        }
    }

    /**
     * 获取数据
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, defaultValue: T): T {
        return when (defaultValue) {
            is String -> getString(key, defaultValue) as T
            is Int -> getInt(key, defaultValue) as T
            is Long -> getLong(key, defaultValue) as T
            is Float -> getFloat(key, defaultValue) as T
            is Boolean -> getBoolean(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported value type: $defaultValue")
        }
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        sharedPreferences.edit { putFloat(key, value) }
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    /**
     * 移除某个key对应的值
     */
    fun remove(key: String) {
        sharedPreferences.edit { remove(key) }
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        sharedPreferences.edit { clear() }
    }

    /**
     * 检查是否包含某个key
     */
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    /**
     * 获取所有键值对
     */
    fun getAll(): Map<String, *> {
        return sharedPreferences.all
    }
}