package com.example.gremlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Message(val text: String, val isUser: Boolean, var rating: String? = null)

class ChatAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_USER = 0
        private const val TYPE_GREMLIN = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) TYPE_USER else TYPE_GREMLIN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_USER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_left, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_right, parent, false)
            GremlinViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder) {
            holder.bind(message)
        } else if (holder is GremlinViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        fun bind(message: Message) {
            tvMessage.text = message.text
        }
    }

    class GremlinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val btnFunny: Button = itemView.findViewById(R.id.btnFunny)
        private val btnMid: Button = itemView.findViewById(R.id.btnMid)

        fun bind(message: Message) {
            tvMessage.text = message.text
            
            // Simple logic to disable buttons if already rated
            if (message.rating != null) {
                btnFunny.isEnabled = false
                btnMid.isEnabled = false
            }

            btnFunny.setOnClickListener {
                message.rating = "funny"
                btnFunny.isEnabled = false
                btnMid.isEnabled = false
                // In a real app, save this to DB
            }

            btnMid.setOnClickListener {
                message.rating = "mid"
                btnFunny.isEnabled = false
                btnMid.isEnabled = false
            }
        }
    }
}
