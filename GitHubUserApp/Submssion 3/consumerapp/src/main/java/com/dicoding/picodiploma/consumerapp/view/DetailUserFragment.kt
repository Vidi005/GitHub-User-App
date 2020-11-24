package com.dicoding.picodiploma.consumerapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.model.UserDetail
import com.dicoding.picodiploma.consumerapp.model.entity.UserFavorite
import com.dicoding.picodiploma.consumerapp.view.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.consumerapp.viewmodel.DetailUserViewModel
import kotlinx.android.synthetic.main.fragment_detail_user.*
import kotlinx.android.synthetic.main.layout_followers_and_following.*

class DetailUserFragment : Fragment() {

    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var image: String
    private lateinit var username: String
    private lateinit var userType: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_detail_user, container, false)

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
        setHasOptionsMenu(true)
        getSelectedUser()
        showLoadingUserDetails(true)
        setViewPager()
        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailUserViewModel::class.java)
        username.let { detailUserViewModel.setDetailUser(it, requireContext()) }
        detailUserViewModel.getDetailUser().observe(viewLifecycleOwner, Observer { detailUserItems ->
            if (detailUserItems != null) {
                showUserDetails(detailUserItems)
                showLoadingUserDetails(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onNavigateUp(item.itemId)
        shareUserDetail(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBar() {
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.app_title_detail)
        }
    }

    private fun onNavigateUp(itemId: Int) {
        if (itemId == android.R.id.home) activity?.onBackPressed()
    }

    private fun getSelectedUser() {
        if (arguments != null) {
            val userFavorite = arguments?.getParcelable<UserFavorite>(
                UserFavoritesFragment.EXTRA_FAVORITE_USER) as UserFavorite
            image = userFavorite.avatar.toString()
            username = userFavorite.username.toString()
            userType = userFavorite.type.toString()
            Glide.with(this).load(image).into(civ_avatar_received)
            tv_username_received.text = username
            when (userType) {
                "User" -> Glide.with(this).load(R.drawable.ic_user).into(iv_type_account)
                else -> Glide.with(this).load(R.drawable.ic_organization).into(iv_type_account)
            }
        }
    }

    private fun shareUserDetail(sharedUser: Int) {
        if (sharedUser == R.id.app_bar_share){
            val name = detailUserViewModel.getDetailUser().value?.name
            val username = username
            val type = userType
            val company = detailUserViewModel.getDetailUser().value?.company
            val location = detailUserViewModel.getDetailUser().value?.location
            val repositories = detailUserViewModel.getDetailUser().value?.repositories
            val followers = detailUserViewModel.getDetailUser().value?.followers
            val following = detailUserViewModel.getDetailUser().value?.following
            val shareUser = "${getString(R.string.share_github_user_detail)}:\n" +
                    "${getString(R.string.share_name)}: $name\n" +
                    "${getString(R.string.share_username)}: $username\n" +
                    "${getString(R.string.share_type)}: $type" +
                    "${getString(R.string.share_company)}: $company\n" +
                    "${getString(R.string.share_location)}: $location\n" +
                    "${getString(R.string.share_repositories)}: ${repositories.toString()}\n" +
                    "${getString(R.string.share_followers)}: ${followers.toString()}\n" +
                    "${getString(R.string.share_following)}: ${following.toString()}"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareUser)
            shareIntent.type = "text/html"
            activity?.startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)))
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setViewPager() {
        val sectionsPagerAdapter = context?.let {
            SectionsPagerAdapter(it, childFragmentManager)
        }
        sectionsPagerAdapter?.setUsername(username)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        activity?.actionBar?.elevation = 0f
    }

    private fun showUserDetails(detailUserItems: UserDetail) {
        if (detailUserItems.name?.isEmpty() == true || detailUserItems.name == "null") {
            tv_name_detail.apply {
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
                text = getString(R.string.no_name_detail)
            }
        } else tv_name_detail.text = detailUserItems.name
        if (detailUserItems.company?.isEmpty() == true || detailUserItems.company == "null") {
            tv_company_detail.apply {
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
                text = getString(R.string.no_company_detail)
            }
        } else tv_company_detail.text = detailUserItems.company
        if (detailUserItems.location?.isEmpty() == true || detailUserItems.location == "null") {
            tv_location_detail.apply {
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
                text = getString(R.string.no_location_detail)
            }
        } else tv_location_detail.text = detailUserItems.location
        tv_repositories_detail.text = detailUserItems.repositories.toString()
        tv_tab_item_followers.text = detailUserItems.followers.toString()
        tv_tab_item_following.text = detailUserItems.following.toString()
    }

    private fun showLoadingUserDetails(state: Boolean) {
        if (state) group_loading_bar_user.visibility = View.VISIBLE
        else group_loading_bar_user.visibility = View.GONE
    }
}
