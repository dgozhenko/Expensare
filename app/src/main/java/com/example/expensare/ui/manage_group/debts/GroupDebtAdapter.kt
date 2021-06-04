package com.example.expensare.ui.manage_group.debts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.UserDebt
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

private const val VIEW_TYPE_MY_DEBT = 1
private const val VIEW_TYPE_TO_ME_DEBT = 2

class GroupDebtAdapter(val debtToMe: Boolean): RecyclerView.Adapter<GroupDebtAdapter.ViewHolder>() {

    private var list = arrayListOf<UserDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_MY_DEBT) {
            MyDebtViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_debt_item, parent, false))
        } else {
            ToMeDebtViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_debt_item, parent, false))
        }
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

    override fun getItemViewType(position: Int): Int {
        return when {
            debtToMe -> VIEW_TYPE_TO_ME_DEBT
            else -> VIEW_TYPE_MY_DEBT
        }
    }

    inner class MyDebtViewHolder(itemView: View): ViewHolder(itemView) {
        private val name = itemView.findViewById<MaterialTextView>(R.id.name)
        private val amount = itemView.findViewById<MaterialTextView>(R.id.money)
        private val image = itemView.findViewById<CircleImageView>(R.id.icon)

        override fun bind(userDebt: UserDebt) {
            super.bind(userDebt)
            name.text = userDebt.toUser.username
            amount.setTextColor(itemView.context.resources.getColor(R.color.red, itemView.context.theme))
            amount.text = "-$${userDebt.fullAmount}"
            Picasso.with(itemView.context).load(userDebt.toUser.avatar).into(image)
        }
    }

    inner class ToMeDebtViewHolder(itemView: View): ViewHolder(itemView) {
        private val name = itemView.findViewById<MaterialTextView>(R.id.name)
        private val amount = itemView.findViewById<MaterialTextView>(R.id.money)
        private val image = itemView.findViewById<CircleImageView>(R.id.icon)

        override fun bind(userDebt: UserDebt) {
            super.bind(userDebt)
            name.text = userDebt.toUser.username
            amount.setTextColor(itemView.context.resources.getColor(R.color.dark_green, itemView.context.theme))
            amount.text = "+$${userDebt.fullAmount}"
            Picasso.with(itemView.context).load(userDebt.toUser.avatar).into(image)
        }
    }

    open class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        open fun bind(userDebt: UserDebt) {

        }
    }
}