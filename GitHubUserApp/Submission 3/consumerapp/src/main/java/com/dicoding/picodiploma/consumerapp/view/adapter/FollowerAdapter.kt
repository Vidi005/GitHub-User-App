package com.dicoding.picodiploma.consumerapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.model.Follower
import kotlinx.android.synthetic.main.item_follower.view.*

class FollowerAdapter(private val followers: ArrayList<Follower>) :
    RecyclerView.Adapter<FollowerAdapter.RecyclerViewHolder>() {

    fun setFollowerData(item: ArrayList<Follower>) {
        followers.clear()
        followers.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_follower, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = followers.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(followers[position])

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(follower: Follower) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(follower.avatar)
                    .apply { RequestOptions().override(100, 100) }
                    .into(riv_item_avatar_follower)
                tv_item_username_follower.text = follower.username
                tv_item_id_follower.text = follower.id.toString()
                tv_item_type_follower.text = follower.type
            }
        }
    }
}