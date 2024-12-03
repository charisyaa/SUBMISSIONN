package com.example.submissionpertamafundamental.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.data.response.ListEventsItem
import com.example.submissionpertamafundamental.databinding.ItemRowEventBinding
import com.example.submissionpertamafundamental.model.Event
import com.example.submissionpertamafundamental.ui.EventAdapter.MyViewHolder.Companion.DIFF_CALLBACK

class EventAdapter (private val onItemClick: (ListEventsItem) -> Unit) :
    ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.MyViewHolder {
        val binding = ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
    class MyViewHolder(
        private val binding: ItemRowEventBinding,
        private val onItemClick: (ListEventsItem) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvText.text = event.name
            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.tvImage)

            itemView.setOnClickListener {
                onItemClick(event)
            }
        }

        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
                override fun areItemsTheSame(
                    oldItem: ListEventsItem,
                    newItem: ListEventsItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: ListEventsItem,
                    newItem: ListEventsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}