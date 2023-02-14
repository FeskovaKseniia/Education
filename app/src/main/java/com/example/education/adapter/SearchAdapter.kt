package com.example.education

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.education.data.SearchResponse
import com.example.education.databinding.ItemLayoutBinding

class SearchAdapter(private var list: ArrayList<SearchResponse>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(private var binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResponse) {
            binding.symbol.text = item.coins[0].name + "\nfrom new request"
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

    fun update(newList: List<SearchResponse>) {
        list.clear()
        list.addAll(newList)
    }
}