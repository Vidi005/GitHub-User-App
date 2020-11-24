package com.dicoding.picodiploma.githubuserappfinal.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.AUTHORITY
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.dicoding.picodiploma.githubuserappfinal.database.UserFavoriteHelper

class UserFavoriteProvider : ContentProvider() {

    companion object {
        private const val USER_FAVORITE = 1
        private const val USER_FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userFavoriteHelper: UserFavoriteHelper

        init {
            sUriMatcher.apply {
                addURI(AUTHORITY, TABLE_NAME, USER_FAVORITE)
                addURI(AUTHORITY, "$TABLE_NAME/#", USER_FAVORITE_ID)
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_FAVORITE_ID) {
            sUriMatcher.match(uri) -> userFavoriteHelper
                .deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER_FAVORITE) {
            sUriMatcher.match(uri) -> userFavoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        userFavoriteHelper = UserFavoriteHelper.getInstance(context as Context)
        userFavoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER_FAVORITE -> userFavoriteHelper.queryAll()
            USER_FAVORITE_ID -> userFavoriteHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when (USER_FAVORITE_ID) {
            sUriMatcher.match(uri) -> userFavoriteHelper
                .update(uri.lastPathSegment.toString(), values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}
