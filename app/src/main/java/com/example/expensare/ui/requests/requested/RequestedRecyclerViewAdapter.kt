package com.example.expensare.ui.requests.requested

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

class RequestedRecyclerViewAdapter: RecyclerView.Adapter<RequestedRecyclerViewAdapter.ViewHolder>() {

    private var list = arrayListOf<Request>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_pending_request_item, parent, false)
        return RequestedRecyclerViewAdapter.ViewHolder(layoutInflater)
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
            val viaContent = itemView.findViewById<MaterialTextView>(R.id.via_content)

            userName.text = request.toUser.username
            money.text = "-$${request.amount}"
            debtFor.text = request.debtFor
            date.text = request.date
            Picasso.with(itemView.context).load(request.toUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(avatar, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(request.toUser.avatar).into(avatar)
                }

            })
        }
    }
}