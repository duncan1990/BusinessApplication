package com.example.mybusinessapp.product.bottomsheet

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mybusinessapp.R
import com.example.mybusinessapp.databinding.ProductOrderBottomsheetFragmentBinding
import com.example.mybusinessapp.local.DBHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProductOrderBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: ProductOrderBottomsheetFragmentBinding? = null
    private val binding get() = _binding!!
    private val db by lazy { DBHelper(requireContext())  }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ProductOrderBottomsheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clientsArrayList : ArrayList<String> = arrayListOf()
        val cList = db.readData()
        cList.forEach { _list ->
            clientsArrayList.add(_list.clientName)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, clientsArrayList)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        observeQuantityTextUI()
        setupOnClickListener()
        setupSearchBar(clientsArrayList, arrayAdapter)
    }

    private fun setupOnClickListener() {
        binding.apply {
            btnSave.setOnClickListener {
                if (editTextQuantity.text.toString() != "" && autoCompleteTextView.text.toString() != getString(
                        R.string.client
                    )
                ) {
                    showAlertSaveDialog()
                } else {
                    Toast.makeText(context, getString(R.string.enter_all_fields), Toast.LENGTH_SHORT).show()
                }
            }

            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun observeQuantityTextUI() {
        binding.apply {
            editTextQuantity.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    if (s.toString() != "") {
                        val total = s.toString().toInt() * arguments?.getDouble("price")!!
                        textViewTotal.text = String.format("%.3f", total)
                    } else {
                        textViewTotal.text = getString(R.string.total)
                    }
                }
            })
        }
    }

    private fun showAlertSaveDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.confirm_save))
        builder.setTitle(getString(R.string.question_save))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            val total = binding.editTextQuantity.text.toString().toInt() * arguments?.getDouble("price")!!
            db.updateTotal(total, binding.autoCompleteTextView.text.toString())
            Toast.makeText(context, getString(R.string.update_total), Toast.LENGTH_SHORT).show()
            dialog.cancel()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun setupSearchBar(clientsArrayList: ArrayList<String>, arrayAdapter: ArrayAdapter<String>) {
        binding.autoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.toString() != "") {
                    arrayAdapter.filter.filter(s)
                } else {
                    binding.autoCompleteTextView.setAdapter(arrayAdapter)
                }
            }
        })
    }

    companion object {
        const val TAG = "ProductOrderBottomSheetFragment"
        fun newInstance(price: Double): ProductOrderBottomSheetFragment = ProductOrderBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putDouble("price", price)
            }
        }
    }
}