package com.example.expensare.ui.addexpense

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.Group
import com.example.expensare.data.User
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AddExpenseAdapter(val onClickListener: OnClickListener): RecyclerView.Adapter<AddExpenseAdapter.ViewHolder>() {

    private var list = arrayListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_add_expenses_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.itemView.findViewById<CardView>(R.id.add_expense_card_view)
        val checkbox = holder.itemView.findViewById<MaterialCheckBox>(R.id.checkbox)
        checkbox.isEnabled = false
        cardView.setOnClickListener {
            onClickListener.onClick(list[position])
            checkbox.isChecked = true
        }
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getUsers(user: ArrayList<User>) {
        list = user
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            val userName = itemView.findViewById<MaterialTextView>(R.id.name_user)
            val imageView = itemView.findViewById<CircleImageView>(R.id.avatar)

            userName.text = user.username
            Picasso.with(itemView.context).load(user.avatar).into(imageView)
        }
    }
}

class OnClickListener(val clickListener: (user: User) -> Unit) {
    fun onClick(user: User) = clickListener(user)
}
