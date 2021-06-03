package com.example.expensare.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.Expense
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
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
        list = expenses
        notifyDataSetChanged()
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
            Picasso.with(itemView.context).load(expense.user.avatar).into(imageView)
        }
    }
}
