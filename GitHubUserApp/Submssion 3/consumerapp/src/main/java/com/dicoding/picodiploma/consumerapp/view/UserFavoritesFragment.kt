package com.dicoding.picodiploma.consumerapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
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
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.model.entity.UserFavorite
import com.dicoding.picodiploma.consumerapp.view.adapter.UserFavoriteAdapter
import com.dicoding.picodiploma.consumerapp.viewmodel.UserFavoritesViewModel
import kotlinx.android.synthetic.main.fragment_user_favorites.*

class UserFavoritesFragment : Fragment() {

    private lateinit var userFavoriteAdapter: UserFavoriteAdapter
    private lateinit var userFavoritesViewModel: UserFavoritesViewModel
    private lateinit var searchView: SearchView
    private var list = ArrayList<UserFavorite>()
    companion object {
        private val TAG = UserFavoritesFragment::class.java.simpleName
        const val EXTRA_FAVORITE_USER = "extra_favorite_user"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_user_favorites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
        setHasOptionsMenu(true)
        showRecyclerView()
        userFavoritesViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(UserFavoritesViewModel::class.java).apply {
                tv_favorite_result.text = resources.getString(R.string.load_database)
                showLoadingUserFavorites(true)
                setUserFavorite(requireContext())
                getUserFavorite().observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        showUserFavoriteItems(it)
                        Log.d(TAG, "$it")
                        showLoadingUserFavorites(false)
                    }
                })
            }
    }

    private fun setActionBar() {
        val drawable = ResourcesCompat.getDrawable(resources,
            R.drawable.github_logo, null)
        val bitmap = drawable?.toBitmap()
        val scaledDrawable = BitmapDrawable(resources,
            bitmap?.let { Bitmap.createScaledBitmap(it, 60, 60, true) })
        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            setHomeAsUpIndicator(scaledDrawable)
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.app_title_user_favorites)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem: MenuItem? = menu.findItem(R.id.app_bar_search)
        searchView = (searchItem?.actionView as SearchView).apply {
            queryHint = resources.getString(R.string.search_items)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                @SuppressLint("SetTextI18n")
                override fun onQueryTextChange(newText: String?): Boolean {
                    userFavoriteAdapter.filter.filter(newText)
                    if (newText.isNullOrEmpty()) {
                        if (list.size >= 1) tv_favorite_result.text =
                            "${resources.getString(R.string.favored_user)}: ${list.size}"
                        else if (list.size == 0) tv_favorite_result.text =
                            getString(R.string.favorite_status_info)
                    } else tv_favorite_result.text = getString(R.string.user_favorites_search_result)
                    return true
                }
            })
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showRecyclerView() {
        rv_favorite_user.setHasFixedSize(true)
        rv_favorite_user.layoutManager = LinearLayoutManager(activity)
        userFavoriteAdapter = UserFavoriteAdapter(list)
        rv_favorite_user.adapter = userFavoriteAdapter
        userFavoriteAdapter.notifyDataSetChanged()
        userFavoriteAdapter.setOnItemClickCallback(object :
            UserFavoriteAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: UserFavorite) = setSelectedUser(data)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showUserFavoriteItems(userFavoriteItems: ArrayList<UserFavorite>) {
        userFavoriteAdapter.setUserFavoriteData(userFavoriteItems)
        if (userFavoriteItems.size >= 1) tv_favorite_result.text =
            "${resources.getString(R.string.favored_user)}: ${userFavoriteItems.size}"
        else if (userFavoriteItems.size == 0) tv_favorite_result.text =
            getString(R.string.favorite_status_info)
    }

    private fun setSelectedUser(data: UserFavorite) {
        val mBundle = Bundle()
        mBundle.putParcelable(EXTRA_FAVORITE_USER, data)
        NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_userFavoritesFragment_to_detailUserFragment, mBundle)
        closeKeyboard()
    }

    private fun showLoadingUserFavorites(state: Boolean) {
        if (state) progressBar8.visibility = View.VISIBLE
        else progressBar8.visibility = View.GONE
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
