package com.example.expensare.ui.requests.pending

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.models.Request
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PendingRecyclerViewAdapter: RecyclerView.Adapter<PendingRecyclerViewAdapter.ViewHolder>() {

    private var list = arrayListOf<Request>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_pending_request_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getRequests(requests: ArrayList<Request>) {
        list = requests
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(request: Request) {
            val userName = itemView.findViewById<MaterialTextView>(R.id.name_user)
            val date = itemView.findViewById<MaterialTextView>(R.id.date)
            val money = itemView.findViewById<MaterialTextView>(R.id.debt_amount)
            val avatar = itemView.findViewById<CircleImageView>(R.id.avatar)
            val debtFor = itemView.findViewById<MaterialTextView>(R.id.debt_for_content)

            userName.text = request.debt.fromUser.username
            money.text = "-$${request.debt.amount}"
            debtFor.text = request.debt.debtFor
            date.text = request.date
            Picasso.with(itemView.context).load(request.debt.fromUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(avatar, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(request.debt.fromUser.avatar).into(avatar)
                }

            })
        }
    }
}