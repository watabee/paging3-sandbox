package com.github.watabee.paging3sandbox

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.watabee.paging3sandbox.data.User

class UserAdapter : PagingDataAdapter<User, UserViewHolder>(createDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position) ?: return
        holder.bind(user)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_item_user
    }

    companion object {
        fun createDiffCallback() = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return newItem == oldItem
            }
        }
    }
}

class UserViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
) {
    private val userImageView: ImageView = itemView.findViewById(R.id.image)
    private val userNameTextView: TextView = itemView.findViewById(R.id.user_name_text)

    fun bind(user: User) {
        userNameTextView.text = user.name
        userImageView.load(user.avatarUrl)
    }
}
