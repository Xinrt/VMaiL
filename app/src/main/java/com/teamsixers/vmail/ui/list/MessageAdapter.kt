package com.teamsixers.vmail.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamsixers.vmail.R
import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.detail.DetailActivity

/**
 * MessageAdapter for messages in listActivity.
 */
class MessageAdapter(private val localMsgList: List<LocalMsg>)
    : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    /**
     * See android official documentation for onCreateViewHolder.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sender: TextView = view.findViewById(R.id.item_list_sender)
        val date: TextView = view.findViewById(R.id.item_list_date)
        val subject: TextView = view.findViewById(R.id.item_list_subject)
    }

    /**
     * See android official documentation for onCreateViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val localMsg = localMsgList[position]
            // when email is clicked, mark it as read.
            localMsg.isRead = true
            localMsg.save()
            val folderName = localMsg.folderName
            val uID = localMsg.uID
            DetailActivity.actionStart(parent.context, folderName, uID)
        }
        return holder
    }

    /**
     * See android official documentation for onCreateViewHolder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val localMsg = localMsgList[position]
        if (localMsg.senderNickname != "") {
            holder.sender.text = localMsg.senderNickname
        } else if (localMsg.senderAddress != ""){
            holder.sender.text = localMsg.senderAddress
        } else {
            holder.sender.text = "No Address or Nickname"
        }
        holder.date.text = localMsg.date
        if (localMsg.subject == "") {
            holder.subject.text = "(No subject)"
        } else {
            holder.subject.text = localMsg.subject
        }
    }

    /**
     * See android official documentation for onCreateViewHolder.
     */
    override fun getItemCount() = localMsgList.size
}