package com.example.cultuasturias.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.FragmentCvMapBinding
import com.example.cultuasturias.domain.MapViewModel
import com.example.cultuasturias.model.CulturalVenueItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.launch

class CvMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentCvMapBinding? = null
    private val binding get() = _binding!!

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var clusterManager: ClusterManager<CvClusterItem>? = null

    private var isMapReady = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCvMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return(binding.root)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        isMapReady = true

        // Inicializar ClusterManager
        clusterManager = ClusterManager(requireContext(), googleMap)
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)

        // Listener para navegar al fragmento de detalles
        clusterManager?.setOnClusterItemInfoWindowClickListener { clusterItem ->
            val culturalVenue = clusterItem.culturalVenue
            val bundle = Bundle().apply {
                putString("culturalVenue", culturalVenue.Nombre)
            }
            findNavController().navigate(R.id.action_cvMapFragment_to_cvItemDetailsFragment, bundle)
        }

        observeViewModel()
        enableUserLocation()
    }

    private fun observeViewModel() {
        mapViewModel.mapUIStateObservable.observe(viewLifecycleOwner) { result ->
            when (result) {
                is CultuAstUIState.Success -> {
                    if (isMapReady) {
                        lifecycleScope.launch {
                            val clusterItems = result.datos
                                .filter { culturalVenue ->
                                    culturalVenue.getCoords() != LatLng(0.0, 0.0)
                                }
                                .map { culturalVenue ->
                                    CvClusterItem(culturalVenue)
                                }

                            clusterManager?.apply {
                                clearItems()
                                cluster()
                                addItems(clusterItems)
                            }
                        }
                    }
                }
                is CultuAstUIState.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        googleMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(it.latitude, it.longitude)
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM.toFloat())
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        googleMap.clear()  // Limpiar el mapa al destruir la vista
        clusterManager?.clearItems() // Limpiar los marcadores al destruir la vista
        clusterManager = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 12
    }

    data class CvClusterItem(val culturalVenue: CulturalVenueItem) : ClusterItem {
        override fun getPosition(): LatLng {
            return culturalVenue.getCoords()
        }

        override fun getTitle(): String {
            return culturalVenue.Nombre
        }

        override fun getSnippet(): String {
            return culturalVenue.Direccion
        }

        override fun getZIndex(): Float {
            return 0f
        }
    }
}
