package com.example.todolist.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.example.todolist.model.Todo
import kotlinx.serialization.json.Json

val TodoNavType = object : NavType<Todo?>(isNullableAllowed = true) {

    override fun get(bundle: Bundle, key: String): Todo? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, Todo::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }
    }

    override fun parseValue(value: String): Todo? {
        return if (value == "null") null else Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: Todo?) {
        bundle.putParcelable(key, value)
    }

    override fun serializeAsValue(value: Todo?): String {
        return Uri.encode(Json.encodeToString(value))
    }
}