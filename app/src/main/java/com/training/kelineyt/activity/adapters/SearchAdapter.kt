package com.training.kelineyt.activity.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.training.kelineyt.activity.data.Product
import com.training.kelineyt.activity.helper.getProductPrice
import com.training.kelineyt.databinding.ProductRvItemBinding

 class SearchAdapter(var items : List<Product> )  : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(private val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgProductTable)
                tvName.text = product.name
                val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                tvNewPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                if (product.offerPercentage == null)
                    tvNewPrice.visibility = View.INVISIBLE

                tvPrice.text = "$ ${product.price}"
            }
        }

    }


    // differ
    private val diffCalBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCalBack)

    // differ

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (items.isNotEmpty()){
            val product = items[position]
            holder.bind(product)
            holder.itemView.setOnClickListener {
                onClick?.invoke(product)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    var onClick: ((Product) -> Unit)? = null

     fun addNewListItems(newList : List<Product>  ){


         items = newList

     }
}