package com.example.githubusers.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusers.databinding.LayoutUserMinBinding
import com.example.githubusers.domain.models.GithubUser

/**
 * Created by Rahul Ray.
 * Version 1.0.0 on 05,January,2024
 */
class UserListAdapter(
    private val onUserClicked: (String?) -> Unit
): RecyclerView.Adapter<UserListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserListAdapter.ItemViewHolder {
        val binding = LayoutUserMinBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UserListAdapter.ItemViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class ItemViewHolder(private val binding: LayoutUserMinBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GithubUser) {
            Glide.with(binding.root.context)
                .load(user.avatar_url)
                .into(binding.imageAvatar)

            binding.textName.text = user.login

            binding.root.setOnClickListener {
                onUserClicked(user.login)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<GithubUser>(){
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: GithubUser,
            newItem: GithubUser
        ) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    fun submitData(list: List<GithubUser>){
        differ.submitList(list)
    }
}