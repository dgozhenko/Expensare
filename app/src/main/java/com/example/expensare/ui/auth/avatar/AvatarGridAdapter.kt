package com.example.expensare.ui.auth.avatar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.Avatar
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AvatarGridAdapter(val onClickListener: OnClickListener): RecyclerView.Adapter<AvatarGridAdapter.ViewHolder>() {

  private var itemAvatar = arrayListOf<Avatar>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_avatar_cell, parent, false)
    return ViewHolder(inflatedView)
  }

  override fun onBindViewHolder(holder: AvatarGridAdapter.ViewHolder, position: Int) {
    holder.itemView.setOnClickListener {
      onClickListener.onClick(itemAvatar[position])
    }
    return holder.bind(itemAvatar[position])
  }

  override fun getItemCount(): Int {
    return itemAvatar.size
  }

  fun setAvatar(avatar: ArrayList<Avatar>) {
    itemAvatar = avatar
    notifyDataSetChanged()
  }

  class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(avatar: Avatar) {
      val imageView = itemView.findViewById<CircleImageView>(R.id.avatar_recycler_item)
      imageView.setImageURI(avatar.avatar)
    }
  }
}

class OnClickListener(val clickListener: (avatar: Avatar) -> Unit) {
  fun onClick(avatar: Avatar) = clickListener(avatar)
}