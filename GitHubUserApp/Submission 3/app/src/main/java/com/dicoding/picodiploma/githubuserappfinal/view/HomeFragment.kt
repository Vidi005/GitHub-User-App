package com.dicoding.picodiploma.githubuserappfinal.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubuserappfinal.R
import com.dicoding.picodiploma.githubuserappfinal.model.User
import com.dicoding.picodiploma.githubuserappfinal.view.adapter.UserAdapter
import com.dicoding.picodiploma.githubuserappfinal.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var list = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
        setHasOptionsMenu(true)
        showRecyclerView()
        homeViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(HomeViewModel::class.java)
        context?.let { searchUsername(it) }
        homeViewModel.getUsernames().observe(viewLifecycleOwner, Observer { usernameItems ->
            if (usernameItems != null) {
                showUserItems(usernameItems)
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        selectOptionsMenu(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBar() {
        val drawable = ResourcesCompat.getDrawable(resources,
            R.drawable.github_logo, null)
        val bitmap = drawable?.toBitmap()
        val scaledDrawable = BitmapDrawable(resources,
            bitmap?.let { Bitmap.createScaledBitmap(it, 60, 60, true) })
        (activity as AppCompatActivity?)?.supportActionBar?.setHomeAsUpIndicator(scaledDrawable)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)?.supportActionBar?.title = getString(R.string.app_title)
    }

    private fun selectOptionsMenu(selectedMenu: Int) {
        when(selectedMenu) {
            R.id.action_favorite -> {
                NavHostFragment
                    .findNavController(this)
                    .navigate(R.id.action_homeFragment_to_userFavoritesFragment)
                closeKeyboard()
            }
            R.id.action_settings -> NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_homeFragment_to_settingsActivity)
            R.id.action_close_app -> activity?.onBackPressed()
        }
    }

    private fun showRecyclerView() {
        rv_user.layoutManager = LinearLayoutManager(activity)
        userAdapter = UserAdapter(list)
        rv_user.adapter = userAdapter
        userAdapter.notifyDataSetChanged()
        rv_user.setHasFixedSize(true)
        userAdapter.setOnItemClickCallback(object :
            UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) = setSelectedUser(data)
            override fun onItemShared(data: User) = shareSelectedUser(data)
        })
    }

    private fun searchUsername(context: Context) {
        sv_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty()!!) {
                    newText.let { homeViewModel.setUsername(it, context) }
                    showLoading(true)
                    tv_result.text = getString(R.string.searching_status_info)
                } else {
                    showLoading(false)
                    list.clear()
                    tv_result.text = getString(R.string.instruction_status_info)
                }
                return true
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showUserItems(usernameItems: ArrayList<User>) {
        userAdapter.setUserData(usernameItems)
        when {
            usernameItems.size >= 1 -> tv_result.text = "${getString(R.string.users_found_status_info)}: ${usernameItems.size}"
            usernameItems.size == 0 -> {
                list.clear()
                tv_result.text = getString(R.string.user_not_found_status_info)
            }
        }
    }

    private fun shareSelectedUser(data: User) {
        val shareUser = "${getString(R.string.share_github_user)}:\n" +
            "${getString(R.string.share_username)}: ${data.username}\n" +
            "ID: ${data.id}\n" +
            "${getString(R.string.share_type)}: ${data.type}"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareUser)
        shareIntent.type = "text/html"
        activity?.startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)))
        closeKeyboard()
    }

    private fun setSelectedUser(data: User) {
        val mBundle = Bundle().apply {
            putString(DetailUserFragment.EXTRA_USERNAME, data.username)
            putString(DetailUserFragment.EXTRA_AVATAR, data.avatar)
            putString(DetailUserFragment.EXTRA_TYPE, data.type)
        }
        NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_homeFragment_to_detailUserFragment, mBundle)
        closeKeyboard()
    }

    private fun showLoading(state: Boolean) {
        if (state) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun closeKeyboard() {
        val view: View? = activity?.currentFocus
        if (view != null) {
            val mInputMethodManager: InputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
