package com.example.education.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.education.data.Crypto
import com.example.education.databinding.ItemLayoutBinding

class CryptoAdapter(
    private val list: ArrayList<Crypto>
) :
    RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    inner class CryptoViewHolder(private var binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Crypto) {
            binding.symbol.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return CryptoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val listItem = list[position]
        holder.bind(listItem)
    }

    fun update(newList: List<Crypto>) {
        list.clear()
        list.addAll(newList)
    }
}