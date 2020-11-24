package com.dicoding.picodiploma.githubuserappfinal.view.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodiploma.githubuserappfinal.R
import com.dicoding.picodiploma.githubuserappfinal.view.FollowersFragment
import com.dicoding.picodiploma.githubuserappfinal.view.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private const val TAG = "BundleFragment"
    }
    private lateinit var username: String

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_followers,
        R.string.tab_following
    )
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowersFragment()
                val mBundle = Bundle()
                mBundle.putString(EXTRA_USERNAME, getUsername())
                fragment.arguments = mBundle
                Log.d(TAG, fragment.arguments.toString())
            }
            1 -> {
                fragment = FollowingFragment()
                val mBundle = Bundle()
                mBundle.putString(EXTRA_USERNAME, getUsername())
                fragment.arguments = mBundle
                Log.d(TAG, fragment.arguments.toString())
            }
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? =
        mContext.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = 2

    fun setUsername(dataUsername: String) {
        username = dataUsername
    }

    private fun getUsername() : String? = username
}