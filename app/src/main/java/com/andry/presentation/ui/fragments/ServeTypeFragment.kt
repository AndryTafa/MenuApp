package com.andry.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.andry.RestaurantKiosk.R
import com.andry.RestaurantKiosk.databinding.FragmentServeTypeBinding
import com.andry.presentation.viewmodels.SharedViewModel
import com.andry.utils.ServeTypes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ServeTypeFragment : Fragment() {
    private var _binding: FragmentServeTypeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServeTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewRestaurant.setOnClickListener {
            viewModel.setServeType(ServeTypes.IN_RESTAURANT)
            findNavController().navigate(R.id.action_serveTypeFragment_to_firstFragmentNew)
        }
        binding.viewTakeAway.setOnClickListener {
            viewModel.setServeType(ServeTypes.TAKE_AWAY)
            findNavController().navigate(R.id.action_serveTypeFragment_to_firstFragmentNew)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

