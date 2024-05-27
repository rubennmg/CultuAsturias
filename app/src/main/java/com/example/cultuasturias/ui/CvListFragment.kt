package com.example.cultuasturias.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.FragmentCvListBinding
import com.example.cultuasturias.domain.CulturalVenueViewModel
import com.example.cultuasturias.domain.SharedCvNameViewModel
import kotlinx.coroutines.launch

/**
 * Fragmento para la lista de lugares culturales.
 *
 */
class CvListFragment : Fragment() {
    private var _binding: FragmentCvListBinding? = null
    private val binding get() = _binding!!
    private val culturalVenueViewModel: CulturalVenueViewModel by activityViewModels()
    private val sharedViewModel: SharedCvNameViewModel by activityViewModels()
    private val culturalVenuesListAdapter = CulturalVenuesListAdapter { it ->
        val bundle = Bundle().apply {
            putString("culturalVenue", it)
        }
        findNavController().navigate(R.id.action_cvListFragment_to_cvItemDetailsFragment, bundle)
    }

    private lateinit var filterButtons: List<Pair<View, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCvListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterButtons = listOf(
            binding.btnFilterAll to "Todos",
            binding.btnFilterWest to "Occidente de Asturias",
            binding.btnFilterCentre to "Centro de Asturias",
            binding.btnFilterEast to "Oriente de Asturias"
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = culturalVenuesListAdapter
        binding.recyclerView.setHasFixedSize(true)

        // Inicializar las preferencias compartidas
        culturalVenueViewModel.initSharedPreferences(requireContext())

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

        // Observar la zona seleccionada
        culturalVenueViewModel.selectedZone.observe(viewLifecycleOwner) { selectedZone ->
            applyFilterAndSetState(selectedZone)
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

        // Filtrado por zona
        setupZoneFilter()
    }

    private fun setupZoneFilter() {
        filterButtons.forEachIndexed { _, (button, zone) ->
            button.setOnClickListener {
                culturalVenueViewModel.setSelectedZone(zone)
                culturalVenueViewModel.applyZoneFilter(zone)
                updateButtonState(button, filterButtons)
            }
        }

        binding.btnFilterAll.setOnClickListener {
            culturalVenueViewModel.setSelectedZone("Todos")
            culturalVenueViewModel.clearFilters()
            updateButtonState(binding.btnFilterAll, filterButtons)
        }
    }

    private fun updateButtonState(selectedButton: View, filterButtons: List<Pair<View, String>>) {
        filterButtons.forEach { (button, _) ->
            if (button == selectedButton) {
                button.isSelected = true
                button.setBackgroundResource(R.drawable.btn_filter_selected_bg)
            } else {
                button.isSelected = false
                button.setBackgroundResource(R.drawable.btn_filter_normal_bg)
            }
        }
    }

    private fun applyFilterAndSetState(zone: String) {
        if (zone == "Todos") {
            culturalVenueViewModel.clearFilters()
        } else {
            culturalVenueViewModel.applyZoneFilter(zone)
        }
        updateButtonStateForZone(zone)
    }

    private fun updateButtonStateForZone(selectedZone: String) {
        filterButtons.forEach { (button, zone) ->
            if (zone == selectedZone) {
                button.isSelected = true
                button.setBackgroundResource(R.drawable.btn_filter_selected_bg)
            } else {
                button.isSelected = false
                button.setBackgroundResource(R.drawable.btn_filter_normal_bg)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
