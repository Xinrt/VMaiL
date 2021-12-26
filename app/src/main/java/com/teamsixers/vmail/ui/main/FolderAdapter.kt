package com.teamsixers.vmail.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamsixers.vmail.R
import com.teamsixers.vmail.ui.list.listview.ListActivity

/**
 * FolderAdapter for folder items in MainActivity.
 */
class FolderAdapter(private val folderList: List<String>) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    /**
     * Get the folder name
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val folderName: TextView = view.findViewById(R.id.item_folder_folderName_tx)
    }

    /**
     * See android official documentation for onCreateViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val folderName = folderList[position]
            ListActivity.actionStart(parent.context, folderName)
        }
        return holder
    }

    /**
     * See android official documentation for onBindViewHolder..
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folderList[position]
        holder.folderName.text = folder
    }

    /**
     * See android official documentation for getItemCount.
     */
    override fun getItemCount(): Int = folderList.size
}