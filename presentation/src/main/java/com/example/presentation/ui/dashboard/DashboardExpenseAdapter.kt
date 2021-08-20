package com.example.presentation.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.database.entities.ExpenseEntity
import com.example.domain.models.Expense
import com.google.android.material.textview.MaterialTextView
import com.inner_circles_apps.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList

class DashboardExpenseAdapter: RecyclerView.Adapter<DashboardExpenseAdapter.ViewHolder>() {

    private var list = arrayListOf<Expense>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_history_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getExpenses(expenses: ArrayList<Expense>) {
        list.clear()
        notifyDataSetChanged()
        list.addAll(expenses)
        notifyItemRangeChanged(0, list.size)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(expense: Expense) {
            val userName = itemView.findViewById<MaterialTextView>(R.id.name)
            val date = itemView.findViewById<MaterialTextView>(R.id.date)
            val money = itemView.findViewById<MaterialTextView>(R.id.money)
            val imageView = itemView.findViewById<CircleImageView>(R.id.icon)

            userName.text = expense.user.username
            date.text = expense.date
            money.text = "-$${expense.amount}"
            Picasso.with(itemView.context).load(expense.user.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(expense.user.avatar).into(imageView)
                }

            })
        }
    }
}
