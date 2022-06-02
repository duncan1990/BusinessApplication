package com.example.mybusinessapp.customer.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mybusinessapp.databinding.AddCustomerBottomsheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddCustomerBottomSheetFragment(private val onSaveClick: (clientName: String, country: String, total: Double) -> Unit) :
    BottomSheetDialogFragment() {
    private var _binding: AddCustomerBottomsheetFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AddCustomerBottomsheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListener()
    }

    private fun setupOnClickListener() {
        binding.apply {
            btnSave.setOnClickListener {
                if (editTextCompanyName.text.toString() != "") {
                    onSaveClick.invoke(
                        editTextCompanyName.text.toString(), editTextCountry.text.toString(),
                        if (editTextTotal.text.toString() == "") {
                            0.0
                        } else {
                            editTextTotal.text.toString().toDouble()
                        }
                    )
                }
                dismiss()
            }

            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "AddCustomerBottomSheetFragment"
        fun newInstance(
            onSaveClick: (String, String, Double) -> Unit
        ): AddCustomerBottomSheetFragment = AddCustomerBottomSheetFragment(onSaveClick)
    }
}