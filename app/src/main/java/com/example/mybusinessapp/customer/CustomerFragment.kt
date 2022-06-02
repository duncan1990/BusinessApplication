package com.example.mybusinessapp.customer

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mybusinessapp.R
import com.example.mybusinessapp.customer.adapter.CustomerAdapter
import com.example.mybusinessapp.customer.bottomsheet.AddCustomerBottomSheetFragment
import com.example.mybusinessapp.databinding.FragmentCustomerBinding
import com.example.mybusinessapp.local.DBHelper
import com.example.mybusinessapp.model.Customer


class CustomerFragment: Fragment() {
    private var _binding: FragmentCustomerBinding? = null
    private val binding get() = _binding!!
    private val db by lazy { DBHelper(requireContext())  }
    private var adapter: CustomerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupClickListener()
    }

    private fun setupAdapter() {
        binding.apply {
            adapter = CustomerAdapter(::onClickCustomer)
            recyclerViewCustomer.adapter = adapter
            adapter?.submitData(db.readData())
        }
    }

    private fun onClickCustomer(customer: Customer) {

    }

    private fun setupClickListener() {
        binding.apply {
            btnAdd.setOnClickListener {
                showAddCustomerBottomSheet()
            }

            btnDel.setOnClickListener {
                showAlertDeleteDialog()
            }
        }
    }

    private fun showAddCustomerBottomSheet() {
        if (childFragmentManager.findFragmentByTag(AddCustomerBottomSheetFragment.TAG) == null) {
            AddCustomerBottomSheetFragment.newInstance { clientName, country, total ->
                onSaveClick(clientName = clientName, country = country, total = total)
            }.show(childFragmentManager, AddCustomerBottomSheetFragment.TAG)
        }
    }

    private fun showAlertDeleteDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.confirm_delete))
        builder.setTitle(getString(R.string.question_delete_all_customer))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            db.deleteAllData()
            adapter?.submitData(db.readData())
            dialog.cancel()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun onSaveClick(clientName: String, country: String, total: Double) {
        db.insertData(Customer(clientName = clientName, country = country, total = total))
        adapter?.submitData(db.readData())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}