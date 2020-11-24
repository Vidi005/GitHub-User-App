package com.dicoding.picodiploma.githubuserappfinal.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubuserappfinal.R
import com.dicoding.picodiploma.githubuserappfinal.model.Following
import kotlinx.android.synthetic.main.item_following.view.*

class FollowingAdapter(private val following: ArrayList<Following>) :
    RecyclerView.Adapter<FollowingAdapter.RecyclerViewHolder>() {

    fun setFollowingData(item: ArrayList<Following>) {
        following.clear()
        following.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_following, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = following.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(following[position])

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(following: Following) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(following.avatar)
                    .apply { RequestOptions().override(100, 100) }
                    .into(riv_item_avatar_following)
                tv_item_username_following.text = following.username
                tv_item_id_following.text = following.id.toString()
                tv_item_type_following.text = following.type
            }
        }
    }
}