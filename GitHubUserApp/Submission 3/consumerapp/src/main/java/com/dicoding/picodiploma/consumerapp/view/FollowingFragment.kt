package com.dicoding.picodiploma.consumerapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.model.Following
import com.dicoding.picodiploma.consumerapp.view.adapter.FollowingAdapter
import com.dicoding.picodiploma.consumerapp.view.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.consumerapp.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var dataUsername: String
    private var list = arrayListOf<Following>()
    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_following, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingFollowing(true)
        getUsernameData()
        showRecyclerView()
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)
        dataUsername.let { followingViewModel.setFollowing(it, requireContext()) }
        Log.d(TAG, dataUsername)
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingItems ->
            if (followingItems != null) {
                showFollowingItems(followingItems)
                showLoadingFollowing(false)
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
        rv_following.layoutManager = LinearLayoutManager(activity)
        followingAdapter = FollowingAdapter(list)
        rv_following.adapter = followingAdapter
        followingAdapter.notifyDataSetChanged()
        rv_following.setHasFixedSize(true)
    }

    private fun showFollowingItems(followingItems: ArrayList<Following>) {
        followingAdapter.setFollowingData(followingItems)
        when (followingItems.size) {
            0 -> tv_no_following.visibility = View.VISIBLE
            else -> tv_no_following.visibility = View.GONE
        }
    }

    private fun showLoadingFollowing(state: Boolean) {
        if (state) progressBar3.visibility = View.VISIBLE
        else progressBar3.visibility = View.GONE
    }
}
