package com.example.mybusinessapp.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mybusinessapp.databinding.FragmentProductBinding
import com.example.mybusinessapp.local.DBHelper
import com.example.mybusinessapp.model.Product
import com.example.mybusinessapp.product.adapter.ProductAdapter
import com.example.mybusinessapp.product.bottomsheet.ProductOrderBottomSheetFragment

class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val db by lazy { DBHelper(requireContext()) }
    private var adapter: ProductAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.apply {
            adapter = ProductAdapter(::onClickProduct)
            recyclerViewProduct.adapter = adapter
            adapter?.submitData(
                mutableListOf(
                    Product(id = 0, productName = "Aluminum rim", price = 1.400),
                    Product(id = 1, productName = "Sport mirror", price = 1.200),
                    Product(id = 2, productName = "Sound system", price = 1.400)
                )
            )
        }
    }

    private fun onClickProduct(product: Product) {
        if (childFragmentManager.findFragmentByTag(ProductOrderBottomSheetFragment.TAG) == null) {
            ProductOrderBottomSheetFragment.newInstance(product.price).show(childFragmentManager, ProductOrderBottomSheetFragment.TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}