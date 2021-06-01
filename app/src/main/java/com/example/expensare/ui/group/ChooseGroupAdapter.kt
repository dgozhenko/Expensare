package com.example.expensare.ui.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensare.R
import com.example.expensare.data.Group
import com.google.android.material.textview.MaterialTextView

class ChooseGroupAdapter(val onClickListener: OnClickListener): RecyclerView.Adapter<ChooseGroupAdapter.ViewHolder>() {

    private var list = arrayListOf<Group>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_group_item, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onClickListener.onClick(list[position])
        }
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getGroups(groups: ArrayList<Group>) {
        list = groups
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(group: Group) {
            val groupName = itemView.findViewById<MaterialTextView>(R.id.group_name)
            val groupType = itemView.findViewById<MaterialTextView>(R.id.group_type)
            val quantity = itemView.findViewById<MaterialTextView>(R.id.quantity)

            groupName.text = group.groupName
            groupType.text = group.groupType
            quantity.text = group.users.size.toString()
        }
    }
}
class OnClickListener(val clickListener: (group: Group) -> Unit) {
    fun onClick(group: Group) = clickListener(group)
}