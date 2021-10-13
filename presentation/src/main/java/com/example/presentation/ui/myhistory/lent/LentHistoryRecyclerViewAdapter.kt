package com.example.presentation.ui.myhistory.lent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Debt
import com.example.domain.models.util.Response
import com.example.presentation.ui.mydebts.lent.LentRecyclerViewAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.inner_circles_apps.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class LentHistoryRecyclerViewAdapter (): RecyclerView.Adapter<LentHistoryRecyclerViewAdapter.ViewHolder>(){

    private var list = arrayListOf<Debt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_to_me_debt_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getDebts(debts: Response<ArrayList<Debt>>) {
        list.clear()
        notifyDataSetChanged()
        list.addAll(debts.data!!)
        notifyItemRangeChanged(0, list.size)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(debt: Debt) {
            val userName = itemView.findViewById<MaterialTextView>(R.id.name_user)
            val date = itemView.findViewById<MaterialTextView>(R.id.date)
            val money = itemView.findViewById<MaterialTextView>(R.id.debt_amount)
            val avatar = itemView.findViewById<CircleImageView>(R.id.avatar)
            val debtFor = itemView.findViewById<MaterialTextView>(R.id.debt_for_content)
            val debtPaidButton = itemView.findViewById<MaterialButton>(R.id.debt_payed_button)

            debtPaidButton.visibility = View.GONE
            userName.text = debt.oweUser.username
            money.text = "$${debt.lentAmount}"
            debtFor.text = debt.name
            date.text = debt.date
            Picasso.with(itemView.context).load(debt.oweUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(avatar, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {

                }

            })
        }
    }
}