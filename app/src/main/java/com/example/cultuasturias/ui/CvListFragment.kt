package com.example.cultuasturias.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cultuasturias.databinding.FragmentCvListBinding
import com.example.cultuasturias.domain.CulturalVenueViewModel
import com.example.cultuasturias.domain.SharedCvNameViewModel
import kotlinx.coroutines.launch

class CvListFragment : Fragment() {

    private var _binding: FragmentCvListBinding? = null
    private val binding get() = _binding!!
    private val culturalVenuesListAdapter = CulturalVenuesListAdapter()
    private val culturalVenueViewModel = CulturalVenueViewModel()
    private val sharedViewModel: SharedCvNameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCvListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = culturalVenuesListAdapter
        binding.recyclerView.setHasFixedSize(true)

        // Observar el estado general de la UI
        culturalVenueViewModel.cultuAstUIStateObservable.observe(viewLifecycleOwner) { result ->
            when (result) {
                is CultuAstUIState.Success -> {
                    culturalVenuesListAdapter.submitList(result.datos)
                }
                is CultuAstUIState.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        // Observar el nombre de búsqueda
        sharedViewModel.searchName.observe(viewLifecycleOwner) { name ->
            lifecycleScope.launch {
                culturalVenueViewModel.searchCulturalVenues(name)
            }
        }

        // Mostrar los resultados de la búsqueda
        lifecycleScope.launch {
            culturalVenueViewModel.searchResults.collect { results ->
                culturalVenuesListAdapter.submitList(results)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}