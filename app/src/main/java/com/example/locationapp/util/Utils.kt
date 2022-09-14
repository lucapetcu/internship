package com.example.locationapp.util

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

object Utils {

    fun setPreferenceInterval(context: Context, content: Long) {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("interval", content).apply()
    }

    fun setPreferenceFastestInterval(context: Context, content: Long) {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("fastest_interval", content).apply()
    }

    fun getInterval(context: Context): Long {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        return sharedPreferences.getLong("interval", -1L)
    }

    fun getFastestInterval(context: Context): Long {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        return sharedPreferences.getLong("fastest_interval", -1L)
    }

    fun setUsername(context: Context, content: String) {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("username", content).apply()
    }

    fun getUsername(context: Context): String {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "username")!!
    }

    fun setButtonState(context: Context, content: Boolean) {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("button_state", content).apply()
    }

    fun getButtonState(context: Context): Boolean {
        val spFilename: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(spFilename, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("button_state", false)
    }

}