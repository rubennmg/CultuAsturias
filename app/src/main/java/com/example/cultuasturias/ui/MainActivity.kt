package com.example.cultuasturias.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cultuasturias.databinding.ActivityMainBinding
import com.example.cultuasturias.domain.CulturalVenueViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val culturalVenuesListAdapter = CulturalVenuesListAdapter()
    private val culturalVenueViewModel = CulturalVenueViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.menu)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = culturalVenuesListAdapter

        // Observar el estado general de la UI
        culturalVenueViewModel.cultuAstUIStateObservable.observe(this) { result ->
            when (result) {
                is CultuAstUIState.Success -> {
                    culturalVenuesListAdapter.submitList(result.datos)
                }
                is CultuAstUIState.Error -> {
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Manejar la búsqueda
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                culturalVenueViewModel.searchCulturalVenues(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No hacer nada aquí
            }
        })

        // Observar los resultados de la búsqueda
        lifecycleScope.launch{
            culturalVenueViewModel.searchResults.collect {
                culturalVenuesListAdapter.submitList(it)
            }
        }
    }
}