package com.example.presentation.ui.manage_group.debts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class DetailedGroupDebtAdapter(val isLent: Boolean): RecyclerView.Adapter<DetailedGroupDebtAdapter.ViewHolder>() {

    private var list = arrayListOf<GroupDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_OWE) {
            OweViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_debt_expanded_item, parent, false))
        } else {
            LentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_debt_expanded_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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

    inner class OweViewHolder(itemView: View): ViewHolder(itemView) {
        val name: MaterialTextView = itemView.findViewById(R.id.member_name)
        private val debt: MaterialTextView = itemView.findViewById(R.id.item_price)
        private val image: CircleImageView = itemView.findViewById(R.id.icon)

        override fun bind(groupDebt: GroupDebt) {
            name.text = "To: ${groupDebt.lentUser.username}"
            debt.setTextColor(itemView.context.resources.getColor(R.color.black, itemView.context.theme))
            debt.text = "$${groupDebt.lentedAmount}"
            Picasso.with(itemView.context).load(groupDebt.lentUser.avatar).networkPolicy(
                NetworkPolicy.OFFLINE).into(image, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(groupDebt.lentUser.avatar).into(image)
                }

            })
        }
    }

    inner class LentViewHolder(itemView: View): ViewHolder(itemView) {
        val name: MaterialTextView = itemView.findViewById(R.id.member_name)
        private val debt: MaterialTextView = itemView.findViewById(R.id.item_price)
        private val image: CircleImageView = itemView.findViewById(R.id.icon)

        override fun bind(groupDebt: GroupDebt) {
            name.text = "From: ${groupDebt.lentUser.username}"
            debt.setTextColor(itemView.context.resources.getColor(R.color.black, itemView.context.theme))
            debt.text = "$${groupDebt.lentedAmount}"
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