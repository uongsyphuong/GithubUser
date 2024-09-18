package com.example.githubuser.storage

import android.content.Context
import com.example.githubuser.model.api.UserResponse
import com.google.gson.Gson
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Singleton

@Singleton
class AppDataStorage(
    private val context: Context,
    private val gson: Gson
) {

    fun storeUserListToInternalStorage(userList: List<UserResponse>) {
        val file = File(context.filesDir, "users.json")
        val jsonString = gson.toJson(userList)
        FileOutputStream(file).use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
    }

    fun getUserListFromInternalStorage(): List<UserResponse>? {
        val file = File(context.filesDir, "users.json")
        if (file.exists()) {
            val jsonString = FileInputStream(file).bufferedReader().use { it.readText() }
            return gson.fromJson(jsonString, Array<UserResponse>::class.java).toList()
        }
        return null
    }
}