package com.dicoding.picodiploma.consumerapp.helper

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_AVATAR
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_COMPANY
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_LOCATION
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_NAME
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_TYPE
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_USERNAME
import com.dicoding.picodiploma.consumerapp.model.entity.UserFavorite

object MappingHelper {

    fun mapCursorToArrayList(userFavoritesCursor: Cursor?): ArrayList<UserFavorite> {
        val userFavoriteList = ArrayList<UserFavorite>()
        userFavoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val avatar = getString(getColumnIndexOrThrow(COLUMN_AVATAR))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val username = getString(getColumnIndexOrThrow(COLUMN_USERNAME))
                val type = getString(getColumnIndexOrThrow(COLUMN_TYPE))
                val company = getString(getColumnIndexOrThrow(COLUMN_COMPANY))
                val location = getString(getColumnIndexOrThrow(COLUMN_LOCATION))
                userFavoriteList.add(UserFavorite(id, avatar, name, username, type, company, location))
            }
        }
        return userFavoriteList
    }

}