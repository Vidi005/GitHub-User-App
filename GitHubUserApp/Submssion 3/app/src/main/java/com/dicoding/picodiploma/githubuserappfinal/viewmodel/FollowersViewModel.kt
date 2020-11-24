package com.dicoding.picodiploma.githubuserappfinal.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubuserappfinal.BuildConfig
import com.dicoding.picodiploma.githubuserappfinal.model.Follower
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class FollowersViewModel : ViewModel() {

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
        private const val GITHUB_API_KEY = BuildConfig.GithubAPIKEY
    }
    private val listFollowers = MutableLiveData<ArrayList<Follower>>()

    fun setFollower(username: String, context: Context) {
        val listItems = ArrayList<Follower>()
        val followerUrl = "https://api.github.com/users/$username/followers"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $GITHUB_API_KEY")
        client.addHeader("User-Agent", "request")
        client.get(followerUrl, object : AsyncHttpResponseHandler(){
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
                        val follower = Follower(dataAvatar, dataUsername, dataId, dataType)
                        listItems.add(follower)
                    }
                    listFollowers.postValue(listItems)
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

    fun getFollowers(): LiveData<ArrayList<Follower>> = listFollowers
}