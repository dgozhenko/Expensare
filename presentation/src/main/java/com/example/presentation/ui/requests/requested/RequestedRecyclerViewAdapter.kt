package com.example.presentation.ui.requests.requested

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Request
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.inner_circles_apps.myapplication.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RequestedRecyclerViewAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<RequestedRecyclerViewAdapter.ViewHolder>() {

    private var list = arrayListOf<Request>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_requested_request_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val acceptButton = holder.itemView.findViewById<MaterialButton>(R.id.accept_button)
        val declineButton = holder.itemView.findViewById<MaterialButton>(R.id.decline_button)

        acceptButton.setOnClickListener {
            onClickListener.onClick(list[position], true)
            acceptButton.isClickable = false
            declineButton.isClickable = false
        }
        declineButton.setOnClickListener {
            onClickListener.onClick(list[position], false)
            acceptButton.isClickable = false
            declineButton.isClickable = false
        }

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

            userName.text = request.debt.lentUser.username
            money.text = "-$${request.debt.lentAmount}"
            debtFor.text = request.debt.name
            date.text = request.date
            Picasso.with(itemView.context).load(request.debt.lentUser.avatar).networkPolicy(NetworkPolicy.OFFLINE).into(avatar, object :
                Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    Picasso.with(itemView.context).load(request.debt.lentUser.avatar).into(avatar)
                }

            })
        }
    }

    //if User click on Accept button - choice will be True, if Decline button - False
    class OnClickListener(val clickListener: (request: Request, choice: Boolean) -> Unit) {
        fun onClick(request: Request, choice: Boolean) = clickListener(request, choice)
    }
}