package com.example.expensare.ui.manage_group.debts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.Group
import com.example.expensare.data.UserDebt
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class DetailedGroupDebtAdapter: RecyclerView.Adapter<DetailedGroupDebtAdapter.ViewHolder>() {

    private var list = arrayListOf<UserDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_debt_expanded_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getDebts(userDebt: ArrayList<UserDebt>) {
        list = userDebt
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(userDebt: UserDebt) {
            val name = itemView.findViewById<MaterialTextView>(R.id.member_name)
            val debt = itemView.findViewById<MaterialTextView>(R.id.item_price)
            val image = itemView.findViewById<CircleImageView>(R.id.icon)

            name.text = userDebt.user.username
            debt.text = userDebt.fullAmount.toString()
            Picasso.with(itemView.context).load(userDebt.user.avatar).into(image)
        }
    }
}