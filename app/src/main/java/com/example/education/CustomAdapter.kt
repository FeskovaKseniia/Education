package com.example.education

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.education.data.Crypto
import com.example.education.databinding.ItemLayoutBinding

class CustomAdapter(private var list: List<Crypto>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(private var binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Crypto) {
            binding.symbol.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = list[position]
        holder.bind(listItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(newList: List<Crypto>?) {
        if (list == newList) return
        if (newList != null) {
            list = newList
        }
    }
}