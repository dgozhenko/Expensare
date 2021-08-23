package com.example.presentation.ui.group.invite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Group
import com.example.domain.models.GroupInvite
import com.example.domain.models.GroupInvites
import com.example.presentation.ui.group.ChooseGroupAdapter
import com.example.presentation.ui.group.OnClickListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.inner_circles_apps.myapplication.R

class GroupInvitesAdapter(private val onClickListener: com.example.presentation.ui.group.invite.OnClickListener): RecyclerView.Adapter<GroupInvitesAdapter.ViewHolder>() {

    private var list = GroupInvites()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_invite, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val acceptButton = holder.itemView.findViewById<MaterialButton>(R.id.accept_button)
        val declineButton = holder.itemView.findViewById<MaterialButton>(R.id.decline_button)
        acceptButton.setOnClickListener {
            onClickListener.onClick(list[position].group, true)
        }
        declineButton.setOnClickListener {
            onClickListener.onClick(list[position].group, false)
        }
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getInvites(invites: GroupInvites) {
        list = invites
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(invite: GroupInvite) {

            val groupName = itemView.findViewById<MaterialTextView>(R.id.group_name)
            val invitedBy = itemView.findViewById<MaterialTextView>(R.id.invited_by_content)
            val message = itemView.findViewById<MaterialTextView>(R.id.message_content)
            val date = itemView.findViewById<MaterialTextView>(R.id.date_content)

            groupName.text = invite.group.groupName
            invitedBy.text = invite.fromUser.username
            message.text = invite.message
            date.text = invite.dateTime
        }
    }
}
class OnClickListener(val clickListener: (group: Group, button: Boolean) -> Unit) {
    fun onClick(group: Group, button: Boolean) = clickListener(group, button)
}
