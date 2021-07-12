package com.example.expensare.ui.dashboard.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.models.ListItem
import com.example.expensare.data.models.User
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ListAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var list = arrayListOf<ListItem>()

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

    fun removeAt(position: Int): ListItem{
        val item = list[position]
        list.removeAt(position)
        notifyDataSetChanged()
        return item
    }

    fun getListItems(listItems: ArrayList<ListItem>) {
        list = listItems
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(listItem: ListItem) {
            val user = itemView.findViewById<CircleImageView>(R.id.user_icon)
            val type = itemView.findViewById<CircleImageView>(R.id.icon)
            val name = itemView.findViewById<MaterialTextView>(R.id.item_name)
            val price = itemView.findViewById<MaterialTextView>(R.id.list_item_price)
            val quantity = itemView.findViewById<MaterialTextView>(R.id.quantity_list)
            name.text = listItem.name
            //TO-DO: Logic for price display
            price.text = ""
            quantity.text = listItem.quantity.toString()

            if (listItem.type == "Vegetables") {
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
                .load(listItem.user.avatar)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(
                    user,
                    object : Callback {
                        override fun onSuccess() {}

                        override fun onError() {
                            Picasso.with(itemView.context).load(listItem.user.avatar).into(user)
                        }
                    }
                )
        }
    }
}

class OnClickListener(val clickListener: (listItem: ListItem, isChecked: Boolean) -> Unit) {
    fun onClick(listItem: ListItem, isChecked: Boolean) = clickListener(listItem, isChecked)
}