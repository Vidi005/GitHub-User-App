package com.dicoding.picodiploma.consumerapp.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_NAME
import com.dicoding.picodiploma.consumerapp.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.consumerapp.helper.MappingHelper
import com.dicoding.picodiploma.consumerapp.model.entity.UserFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoritesViewModel : ViewModel() {

    companion object {
        private val TAG = UserFavoritesViewModel::class.java.simpleName
    }
    private val listUserFavorites = MutableLiveData<ArrayList<UserFavorite>>()

    fun setUserFavorite(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val deferredUserFavorites = async(Dispatchers.IO) {
                    val cursor = context.contentResolver.query(
                        CONTENT_URI,
                        null,
                        null,
                        null,
                        "$COLUMN_NAME COLLATE NOCASE")
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val userFavorites = deferredUserFavorites.await()
                Log.d(TAG, "$userFavorites")
                listUserFavorites.postValue(userFavorites)
                Log.d(TAG, "$listUserFavorites")
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                Toast.makeText(context, "Error: ${e.message.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun getUserFavorite(): LiveData<ArrayList<UserFavorite>> = listUserFavorites
}