package com.dicoding.picodiploma.consumerapp.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.consumerapp.BuildConfig
import com.dicoding.picodiploma.consumerapp.model.Following
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class FollowingViewModel : ViewModel() {

    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
        private const val GITHUB_API_KEY = BuildConfig.GithubAPIKEY
    }
    private val listFollowing = MutableLiveData<ArrayList<Following>>()

    fun setFollowing(username: String, context: Context) {
        val listItems: ArrayList<Following> = arrayListOf()
        val followingUrl = "https://api.github.com/users/$username/following"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $GITHUB_API_KEY")
        client.addHeader("User-Agent", "request")
        client.get(followingUrl, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    Log.d(TAG, result!!)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val item = responseArray.getJSONObject(i)
                        val dataAvatar = item.getString("avatar_url")
                        val dataUsername = item.getString("login")
                        val dataId = item.getInt("id")
                        val dataType = item.getString("type")
                        val following = Following(dataAvatar, dataUsername, dataId, dataType)
                        listItems.add(following)
                    }
                    listFollowing.postValue(listItems)
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d(TAG, errorMsg)
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun getFollowing(): LiveData<ArrayList<Following>> = listFollowing
}