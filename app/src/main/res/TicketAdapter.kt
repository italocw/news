package com.example.supportactivityhelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.NewsAdapter
import com.example.supportactivityhelper.databinding.ListItemTicketBinding

class TicketAdapter(val clickListener: TicketListener) :
    ListAdapter<Ticket, NewsAdapter.ViewHolder>(TicketDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = getItem(position)
        holder.bind(ticket!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemTicketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ticket: Ticket, clickListener: TicketListener) {
            binding.ticket = ticket
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTicketBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TicketDiffCallback : DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem == newItem
        }
    }

    class TicketListener(val clickListener: (ticket: Ticket) -> Unit) {
        fun onClick(ticket: Ticket) = clickListener(ticket)
    }
}