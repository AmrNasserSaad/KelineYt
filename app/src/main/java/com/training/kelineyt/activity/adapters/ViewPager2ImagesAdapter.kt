package com.training.kelineyt.activity.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.training.kelineyt.databinding.ViewpagerImageItemBinding

class ViewPager2ImagesAdapter :
    RecyclerView.Adapter<ViewPager2ImagesAdapter.ViewPager2ImagesAdapterViewHolder>() {


    inner class ViewPager2ImagesAdapterViewHolder(val binding: ViewpagerImageItemBinding) :
        ViewHolder(binding.root) {

        fun bind(imagePath: String) {
            Glide.with(itemView).load(imagePath).into(binding.imageProductDetails)

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
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPager2ImagesAdapterViewHolder {
        return ViewPager2ImagesAdapterViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPager2ImagesAdapterViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}