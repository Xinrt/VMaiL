package com.teamsixers.vmail.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.teamsixers.vmail.R
import com.teamsixers.vmail.logic.model.LocalFile
import java.io.File
import kotlin.math.roundToInt

/**
 * If caller does not want to display inline resource, it should
 * pass all attachments that are not inlined.
 */
class AttachmentAdapter(private val attachmentList: List<LocalFile>)
    : RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val context: Context = view.context
        val fileName: TextView = view.findViewById(R.id.item_attachment_attachName_tx)
        val size: TextView = view.findViewById(R.id.item_attachment_size_tx)
    }

    /**
     * init_listener view holder
     * @param parent a special view that can contain other views
     * @param viewType an integer that represents the type of view
     * @return views that are displayed within a RecyclerView.
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val localFile = attachmentList[position]
            val file = File(localFile.path!!)
            if (file.exists()) {
                openFile(holder.context, file, localFile.type)
            } else {
                localFile.attachment?.download {
                    openFile(holder.context, file, localFile.type)
                }
            }
        }
        return holder
    }


    /**
     * open a selected file
     * @param context interface to global information about an application environment
     * @param file An abstract representation of file and directory pathnames
     * @param type a string that represents the name of file
     */
    private fun openFile(context: Context, file: File, type: String?) {
        val intent = Intent().setAction(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(file), type)
        startActivity(context, intent, null)
    }

    /**
     * get local file in attachment list
     * @param holder views that are displayed within a RecyclerView
     * @param position an integer that represents the position of a local file in the attachment list
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val localFile = attachmentList[position]
        if (localFile.name != null) {
            holder.fileName.text = localFile.name
        }
        var size = localFile.size.toDouble() / (1024.0 * 1024.0)
        size = (size * 1000).roundToInt().toDouble() / 1000
        holder.size.text = "$size M"
    }

    override fun getItemCount() = attachmentList.size
}