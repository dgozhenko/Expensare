package com.example.expensare.ui.mydebts.owe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.models.ManualDebt
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class OweRecyclerViewAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<OweRecyclerViewAdapter.ViewHolder>() {

    private var list = arrayListOf<ManualDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_from_me_debt_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payMoneyButton = holder.itemView.findViewById<MaterialButton>(R.id.money_pay_button)

            payMoneyButton.setOnClickListener {
                onClickListener.onClick(list[position])
                payMoneyButton.isClickable = false
            }
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getDebts(debts: ArrayList<ManualDebt>) {
        list.clear()
        notifyDataSetChanged()
        list.addAll(debts)
        notifyItemRangeChanged(0, list.size)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(debt: ManualDebt) {
            val userName = itemView.findViewById<MaterialTextView>(R.id.name_user)
            val date = itemView.findViewById<MaterialTextView>(R.id.date)
            val money = itemView.findViewById<MaterialTextView>(R.id.debt_amount)
            val avatar = itemView.findViewById<CircleImageView>(R.id.avatar)
            val debtFor = itemView.findViewById<MaterialTextView>(R.id.debt_for_content)

            userName.text = debt.fromUser.username
            money.text = "-$${debt.amount}"
            debtFor.text = debt.debtFor
            date.text = debt.date
            Picasso.with(itemView.context).load(debt.fromUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(avatar, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(debt.fromUser.avatar).into(avatar)
                }

            })
        }
    }

    class OnClickListener(val clickListener: (manualDebt: ManualDebt) -> Unit) {
        fun onClick(manualDebt: ManualDebt) = clickListener(manualDebt)
    }
}
