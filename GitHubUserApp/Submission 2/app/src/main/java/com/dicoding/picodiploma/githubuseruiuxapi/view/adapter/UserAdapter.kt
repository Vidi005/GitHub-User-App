package com.dicoding.picodiploma.githubuseruiuxapi.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubuseruiuxapi.R
import com.dicoding.picodiploma.githubuseruiuxapi.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private val users: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.RecyclerViewHolder>() {

    private lateinit var onItemClickDetail: OnItemClickCallBack
    private lateinit var onItemClickShare: OnItemClickCallBack

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickDetail = onItemClickCallBack
        this.onItemClickShare = onItemClickCallBack
    }

    fun setUserData(items: ArrayList<User>) {
        users.clear()
        users.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(users[position])

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(100, 100))
                    .into(riv_item_avatar)
                tv_item_username.text = user.username
                tv_item_id.text = user.id.toString()
                tv_item_type.text = user.type
                itemView.setOnClickListener { onItemClickDetail.onItemClicked(user) }
                btn_share.setOnClickListener { onItemClickShare.onItemShared(user) }
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: User)
        fun onItemShared(data: User)
    }
}