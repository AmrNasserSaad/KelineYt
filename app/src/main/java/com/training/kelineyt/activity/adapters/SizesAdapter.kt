package com.training.kelineyt.activity.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.training.kelineyt.databinding.SizeRvItemBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {

    private var selectedPosition = -1

    inner class SizesViewHolder(val binding: SizeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(size: String, position: Int) {

            binding.tvSize.text = size
            if (position == selectedPosition) {
                // Size is Selected
                binding.apply {
                    imageShadow.visibility = View.VISIBLE

                }
            } else {
                // Size is not selected
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE

                }
            }

        }
    }


    // differ
    private val diffCalBack = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }


    }
    val differ = AsyncListDiffer(this, diffCalBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(
            SizeRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size, position)


        holder.itemView.setOnClickListener {

            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)

            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)

            onItemClick?.invoke(size)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


     var onItemClick: ((String) -> Unit)? = null

}