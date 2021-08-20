package com.example.presentation.ui.manage_group.debts

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GroupDebt
import com.google.android.material.textview.MaterialTextView
import com.inner_circles_apps.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

private const val VIEW_TYPE_OWE = 1
private const val VIEW_TYPE_LENT = 2

class GroupDebtAdapter(val isLent: Boolean, private val onClickListener: OnClickListener): RecyclerView.Adapter<GroupDebtAdapter.ViewHolder>() {

    private var list = arrayListOf<GroupDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_OWE) {
            MyDebtViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_debt_item, parent, false))
        } else {
            ToMeDebtViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_debt_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.itemView.findViewById<CardView>(R.id.base_cardview)
        val hiddenView = holder.itemView.findViewById<LinearLayout>(R.id.hidden_view)
        with(list[position]) {
            cardView.setOnClickListener {
                val recyclerView = holder.itemView.findViewById<RecyclerView>(R.id.members_recycler_view)
                onClickListener.onClick(this, recyclerView)
                if (this.expanded) {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    hiddenView.visibility = View.GONE
                    this.expanded = false
                } else {
                    TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                    hiddenView.visibility = View.VISIBLE
                    this.expanded = true
                }
            }
        }

        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getDebts(groupDebt: ArrayList<GroupDebt>) {
        list = groupDebt
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isLent -> VIEW_TYPE_LENT
            else -> VIEW_TYPE_OWE
        }
    }

    inner class MyDebtViewHolder(itemView: View): ViewHolder(itemView) {
        private val name = itemView.findViewById<MaterialTextView>(R.id.name)
        private val amount = itemView.findViewById<MaterialTextView>(R.id.money)
        private val image = itemView.findViewById<CircleImageView>(R.id.icon)

        override fun bind(groupDebt: GroupDebt) {
            super.bind(groupDebt)
                name.text = groupDebt.lentUser.username
                amount.setTextColor(itemView.context.resources.getColor(R.color.red, itemView.context.theme))
                amount.text = "-$${groupDebt.lentedAmount}"
            Picasso.with(itemView.context).load(groupDebt.lentUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(image, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(groupDebt.lentUser.avatar).into(image)
                }

            })
        }
    }

    inner class ToMeDebtViewHolder(itemView: View): ViewHolder(itemView) {
        private val name = itemView.findViewById<MaterialTextView>(R.id.name)
        private val amount = itemView.findViewById<MaterialTextView>(R.id.money)
        private val image = itemView.findViewById<CircleImageView>(R.id.icon)

        override fun bind(groupDebt: GroupDebt) {
            super.bind(groupDebt)
            name.text = groupDebt.lentUser.username
            amount.setTextColor(itemView.context.resources.getColor(R.color.dark_green, itemView.context.theme))
            amount.text = "+$${groupDebt.lentedAmount}"
            Picasso.with(itemView.context).load(groupDebt.lentUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(image, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(groupDebt.lentUser.avatar).into(image)
                }

            })
        }
    }

    open class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        open fun bind(groupDebt: GroupDebt) {

        }
    }
}
class OnClickListener(val clickListener: (groupDebt: GroupDebt, recyclerView: RecyclerView) -> Unit) {
    fun onClick(groupDebt: GroupDebt, recyclerView: RecyclerView) = clickListener(groupDebt, recyclerView)
}