package com.dicoding.picodiploma.consumerapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.model.Follower
import com.dicoding.picodiploma.consumerapp.view.adapter.FollowerAdapter
import com.dicoding.picodiploma.consumerapp.view.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.consumerapp.viewmodel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {

    private lateinit var followerAdapter: FollowerAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var dataUsername: String
    private var list: ArrayList<Follower> = arrayListOf()
    companion object {
        private val TAG = FollowersFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_followers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingFollowers(true)
        getUsernameData()
        showRecyclerView()
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)
        dataUsername.let { followersViewModel.setFollower(it, requireContext()) }
        Log.d(TAG, dataUsername)
        followersViewModel.getFollowers().observe(viewLifecycleOwner, Observer { followerItems ->
            if (followerItems != null) {
                showFollowerItems(followerItems)
                showLoadingFollowers(false)
            }
        })
    }

    private fun getUsernameData() {
        if (arguments != null) {
            dataUsername = arguments?.getString(SectionsPagerAdapter.EXTRA_USERNAME).toString()
            Log.d(TAG, "$arguments")
        }
    }

    private fun showRecyclerView() {
        rv_followers.layoutManager = LinearLayoutManager(activity)
        followerAdapter = FollowerAdapter(list)
        rv_followers.adapter = followerAdapter
        followerAdapter.notifyDataSetChanged()
        rv_followers.setHasFixedSize(true)
    }

    private fun showFollowerItems(followerItems: ArrayList<Follower>) {
        followerAdapter.setFollowerData(followerItems)
        when (followerItems.size) {
            0 -> tv_no_follower.visibility = View.VISIBLE
            else -> tv_no_follower.visibility = View.GONE
        }
    }

    private fun showLoadingFollowers(state: Boolean) {
        if (state) progressBar2.visibility = View.VISIBLE
        else progressBar2.visibility = View.GONE
    }
}
