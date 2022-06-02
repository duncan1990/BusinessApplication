package com.example.mybusinessapp.customer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mybusinessapp.R
import com.example.mybusinessapp.databinding.ItemCustomerBinding
import com.example.mybusinessapp.databinding.ItemHeaderBinding
import com.example.mybusinessapp.model.Customer
import com.example.mybusinessapp.model.GroupItemsViewType

class CustomerAdapter(private val onClickItem: (Customer) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var customerList : MutableList<Customer> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: MutableList<Customer>){
        customerList.clear()
        customerList.addAll(list)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind() {
            binding.apply {}
        }
    }

    inner class CustomerViewHolder(private val binding: ItemCustomerBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(item: Customer, onClickItem: (Customer) -> Unit) {
            binding.apply {
                if (layoutPosition % 2 != 0){
                    consLayoutCustomer.setBackgroundColor(consLayoutCustomer.context.getColor(R.color.gull_gray))
                } else {
                    consLayoutCustomer.setBackgroundColor(consLayoutCustomer.context.getColor(R.color.silver))
                }
                textViewName.text = item.clientName
                textViewCountry.text = item.country
                textViewTotal.text = String.format("%.3f", item.total) + " €"

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
                return CustomerViewHolder(bodyView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder.itemViewType) {
                GroupItemsViewType.Header.ordinal -> (holder as? HeaderViewHolder)?.bind()
                else -> (holder as? CustomerViewHolder)?.bind(customerList[position - 1], onClickItem)
            }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {GroupItemsViewType.Header.ordinal} else {GroupItemsViewType.Body.ordinal}
    }

    override fun getItemCount(): Int  = customerList.size + 1

}