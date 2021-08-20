package com.example.presentation.ui.dashboard.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GroupList
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.inner_circles_apps.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ListAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var list = arrayListOf<GroupList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_list_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkBox = holder.itemView.findViewById<MaterialCheckBox>(R.id.checkbox)
        checkBox.setOnClickListener {
            onClickListener.onClick(list[position], checkBox.isChecked)
        }
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun removeAt(position: Int): GroupList{
        val item = list[position]
        list.removeAt(position)
        notifyDataSetChanged()
        return item
    }

    fun getListItems(groupLists: ArrayList<GroupList>) {
        list = groupLists
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(groupList: GroupList) {
            val user = itemView.findViewById<CircleImageView>(R.id.user_icon)
            val type = itemView.findViewById<CircleImageView>(R.id.icon)
            val name = itemView.findViewById<MaterialTextView>(R.id.item_name)
            val price = itemView.findViewById<MaterialTextView>(R.id.list_item_price)
            val quantity = itemView.findViewById<MaterialTextView>(R.id.quantity_list)
            name.text = groupList.name
            //TO-DO: Logic for price display
            price.text = ""
            quantity.text = groupList.quantity.toString()

            if (groupList.type == "Vegetables") {
                Picasso.with(itemView.context)
                    .load(
                        Uri.parse("android.resource://com.example.expensare/drawable/" + R.drawable.avatar1))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(
                        type,
                        object : Callback {
                            override fun onSuccess() {}

                            override fun onError() {
                                Picasso.with(itemView.context).load(Uri.parse("android.resource://com.example.expensare/drawable/" + R.drawable.ic_launcher_foreground)).into(type)
                            }
                        }
                    )
            }

            Picasso.with(itemView.context)
                .load(groupList.user.avatar)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(
                    user,
                    object : Callback {
                        override fun onSuccess() {}

                        override fun onError() {
                            Picasso.with(itemView.context).load(groupList.user.avatar).into(user)
                        }
                    }
                )
        }
    }
}

class OnClickListener(val clickListener: (groupList: GroupList, isChecked: Boolean) -> Unit) {
    fun onClick(groupList: GroupList, isChecked: Boolean) = clickListener(groupList, isChecked)
}