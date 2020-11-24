package com.dicoding.picodiploma.githubuserappfinal.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import android.util.Log
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_NAME
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_USERNAME
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME

class UserFavoriteHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: UserFavoriteHelper? = null
        fun getInstance(context: Context): UserFavoriteHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) INSTANCE = UserFavoriteHelper(context)
                }
            }
            return INSTANCE as UserFavoriteHelper
        }
        private val TAG = UserFavoriteHelper::class.java.simpleName
    }

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun queryAll(): Cursor {
        return database.query(DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_NAME COLLATE NOCASE",
            null)
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(DATABASE_TABLE,
            null,
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long = database.insert(DATABASE_TABLE, null, values)

    fun update(id: String, values: ContentValues?): Int =
        database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))

    fun deleteByUsername(username: String): Int =
        database.delete(DATABASE_TABLE,"$COLUMN_USERNAME = '$username'", null)

    fun checkUsername(username: String): Boolean {
        val cursor = database.query(DATABASE_TABLE,
            null, "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null)
        var check = false
        if (cursor.moveToFirst()) {
            check = true
            var cursorIndex = 0
            while (cursor.moveToNext()) cursorIndex++
            Log.d(TAG, "username found: $cursorIndex")
        }
        cursor.close()
        return check
    }
}