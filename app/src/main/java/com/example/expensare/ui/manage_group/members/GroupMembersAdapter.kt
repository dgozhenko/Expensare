package com.example.expensare.ui.manage_group.members

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.User
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GroupMembersAdapter: RecyclerView.Adapter<GroupMembersAdapter.ViewHolder>() {

    private var userItem = arrayListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_member_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(userItem[position])
    }

    override fun getItemCount(): Int {
        return userItem.size
    }

    fun setUser(user: ArrayList<User>) {
        userItem = user
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            val imageView = itemView.findViewById<CircleImageView>(R.id.avatar)
            val name = itemView.findViewById<MaterialTextView>(R.id.member_name)
            Picasso.with(itemView.context).load(user.avatar).into(imageView)
            name.text = user.username
        }
    }
}