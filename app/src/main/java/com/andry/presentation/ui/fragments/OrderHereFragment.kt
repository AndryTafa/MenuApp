package com.andry.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.FragmentOrderHereBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderHereFragment : Fragment() {
    private var _binding: FragmentOrderHereBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHereBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clAll.setOnClickListener {
            findNavController().navigate(R.id.action_orderHereFragment_to_serveTypeFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

