package com.example.presentation.ui.mydebts.owe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.inner_circles_apps.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList


class OweRecyclerViewAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<OweRecyclerViewAdapter.ViewHolder>() {

    private var list = arrayListOf<Debt>()

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

    fun getDebts(debts: ArrayList<Debt>) {
        list = debts
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(debt: Debt) {
            val userName = itemView.findViewById<MaterialTextView>(R.id.name_user)
            val date = itemView.findViewById<MaterialTextView>(R.id.date)
            val money = itemView.findViewById<MaterialTextView>(R.id.debt_amount)
            val avatar = itemView.findViewById<CircleImageView>(R.id.avatar)
            val debtFor = itemView.findViewById<MaterialTextView>(R.id.debt_for_content)

            userName.text = debt.lentUser.username
            money.text = "$${debt.lentAmount}"
            debtFor.text = debt.name
            date.text = debt.date
            Picasso.with(itemView.context).load(debt.lentUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(avatar, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(debt.oweUser.avatar).into(avatar)
                }

            })
        }
    }

    class OnClickListener(val clickListener: (manualDebt: Debt) -> Unit) {
        fun onClick(manualDebt: Debt) = clickListener(manualDebt)
    }
}
