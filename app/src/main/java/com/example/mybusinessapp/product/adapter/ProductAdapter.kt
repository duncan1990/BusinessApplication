package com.example.mybusinessapp.product.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mybusinessapp.R
import com.example.mybusinessapp.databinding.ItemCustomerBinding
import com.example.mybusinessapp.databinding.ItemHeaderBinding
import com.example.mybusinessapp.model.GroupItemsViewType
import com.example.mybusinessapp.model.Product

class ProductAdapter(private val onClickItem: (Product) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var productList : MutableList<Product> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: MutableList<Product>){
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind() {
            binding.apply {
                textViewCountry.text = textViewCountry.context.getString(R.string.price)
                textViewTotal.isVisible = false
                viewDividerCountry.isVisible = false
            }
        }
    }

    inner class ProductViewHolder(private val binding: ItemCustomerBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(item: Product, onClickItem: (Product) -> Unit) {
            binding.apply {
                if (layoutPosition % 2 != 0){
                    consLayoutCustomer.setBackgroundColor(consLayoutCustomer.context.getColor(R.color.gull_gray))
                } else {
                    consLayoutCustomer.setBackgroundColor(consLayoutCustomer.context.getColor(R.color.silver))
                }
                textViewName.text = item.productName
                textViewCountry.text = String.format("%.3f", item.price)+ " €"
                textViewTotal.isVisible = false
                viewDividerCountry.isVisible = false

                root.setOnClickListener { onClickItem.invoke(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GroupItemsViewType.Header.ordinal -> {
                val headerView = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(headerView)
            }
            else -> {
                val bodyView = ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ProductViewHolder(bodyView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            GroupItemsViewType.Header.ordinal -> (holder as? HeaderViewHolder)?.bind()
            else -> (holder as? ProductViewHolder)?.bind(productList[position - 1], onClickItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {GroupItemsViewType.Header.ordinal} else {GroupItemsViewType.Body.ordinal}
    }

    override fun getItemCount(): Int  = productList.size + 1

}