package com.dicoding.picodiploma.consumerapp.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.model.entity.UserFavorite
import kotlinx.android.synthetic.main.item_user_favorite.view.*
import java.util.*
import kotlin.collections.ArrayList

class UserFavoriteAdapter(private val userFavorites: ArrayList<UserFavorite>) :
    RecyclerView.Adapter<UserFavoriteAdapter.RecyclerViewHolder>(), Filterable {

    private val TAG = UserFavoriteAdapter::class.java.simpleName
    private lateinit var onItemClickDetail: OnItemClickCallBack
    private var filterUserFavorite: ArrayList<UserFavorite> = userFavorites

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickDetail = onItemClickCallBack
    }

    fun setUserFavoriteData(item: ArrayList<UserFavorite>) {
        userFavorites.clear()
        userFavorites.addAll(item)
        notifyDataSetChanged()
        Log.d(TAG, "$item")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user_favorite, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = filterUserFavorite.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(filterUserFavorite[position])

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userFavorite: UserFavorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(userFavorite.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(riv_item_favorite_avatar)
                if (userFavorite.name == "null") {
                    tv_item_favorite_name.apply {
                        setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                        text = context.getString(R.string.no_name_detail)
                    }
                } else tv_item_favorite_name.text = userFavorite.name
                tv_item_favorite_username.text = userFavorite.username
                if (userFavorite.company.isNullOrEmpty() || userFavorite.company == "null") {
                    tv_item_favorite_company.apply {
                        setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                        text = context.getString(R.string.no_company_detail)
                    }
                } else tv_item_favorite_company.text = userFavorite.company
                if (userFavorite.location.isNullOrEmpty() || userFavorite.location == "null") {
                    tv_item_favorite_location.apply {
                        setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                        text = context.getString(R.string.no_location_detail)
                    }
                } else tv_item_favorite_location.text = userFavorite.location
                itemView.setOnClickListener { onItemClickDetail.onItemClicked(userFavorite) }
            }
        }
    }

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val itemSearch = constraint.toString()
            filterUserFavorite = if (itemSearch.isEmpty()) userFavorites
            else {
                val itemList = ArrayList<UserFavorite>()
                for (item in userFavorites) {
                    val name = item.name
                        ?.toLowerCase(Locale.ROOT)
                        ?.contains(itemSearch.toLowerCase(Locale.ROOT))
                    val userName = item.username
                        ?.toLowerCase(Locale.ROOT)
                        ?.contains(itemSearch.toLowerCase(Locale.ROOT))
                    if (name == true || userName == true) itemList.add(item)
                }
                itemList
            }
            val filterResults = FilterResults()
            filterResults.values = filterUserFavorite
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filterUserFavorite = results?.values as ArrayList<UserFavorite>
            notifyDataSetChanged()
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: UserFavorite)
    }
}