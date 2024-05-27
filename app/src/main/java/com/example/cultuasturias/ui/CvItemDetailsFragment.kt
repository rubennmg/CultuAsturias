package com.example.cultuasturias.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.cultuasturias.CultuAstApp
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.FragmentCvItemDetailsBinding
import com.example.cultuasturias.domain.CvItemDetailsViewModel
import com.example.cultuasturias.model.CulturalVenueItem
import com.smarteist.autoimageslider.SliderView
import java.util.ArrayList

/**
 * Fragmento para la vista de detalles de un CulturalVenueItem.
 *
 */
class CvItemDetailsFragment : Fragment() {
    private var _binding: FragmentCvItemDetailsBinding? = null
    private val binding get() = _binding!!
    private val cvItemDetailsViewModel: CvItemDetailsViewModel by viewModels {
        CvItemDetailsViewModel.CvItemDetailsViewModelFactory((activity?.application as CultuAstApp).repository)
    }
    private lateinit var sliderAdapter: ImgSliderAdapter
    private var isExpanded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cvName = arguments?.getString("culturalVenue")
        cvItemDetailsViewModel.setCvName(cvName!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCvItemDetailsBinding.inflate(inflater, container, false)

        // Manejar expansión y compresión del texto de descripción
        binding.tvVerMas.setOnClickListener {
            toggleDescriptionExpansion()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observar los cambios en el objeto cultural venue y actualizar la vista
        cvItemDetailsViewModel.mCulturalVenue.observe(viewLifecycleOwner) { culturalVenue ->
            setupImageSlider(culturalVenue.getSlideUrls())
            bindCvDetails(culturalVenue)
            setupSocialIcons(culturalVenue)
        }
    }

    private fun setupImageSlider(images: List<String>) {
        sliderAdapter = ImgSliderAdapter(ArrayList(images))
        binding.cvImgSlider.setSliderAdapter(sliderAdapter)
        binding.cvImgSlider.apply {
            autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            scrollTimeInSec = 3
            isAutoCycle = true
        }
    }

    private fun bindCvDetails(culturalVenue: CulturalVenueItem) {
        binding.cvName.text = culturalVenue.Nombre.ifEmpty { getString(R.string.no_data) }
        binding.cvDescription.text = culturalVenue.Texto.ifEmpty { getString(R.string.no_data) }
        binding.cvDireccion.text = culturalVenue.Direccion.ifEmpty { getString(R.string.no_data) }
        binding.cvLocation.text = String.format(getString(R.string.location_format),
                culturalVenue.Concejo,
                culturalVenue.Localidad,
                culturalVenue.CodigoPostal,
                culturalVenue.Zona)
        binding.cvTelefono.text = culturalVenue.Telefono.ifEmpty { getString(R.string.no_data) }
        binding.cvObservaciones.text = culturalVenue.Observaciones.ifEmpty { getString(R.string.no_data) }

        setupSocialIcons(culturalVenue)
    }

    private fun setupSocialIcons(culturalVenue: CulturalVenueItem) {
        val socialIcons = mapOf(
            binding.cvFacebook to culturalVenue.Facebook,
            binding.cvTwitter to culturalVenue.Twitter,
            binding.cvInstagram to culturalVenue.Instagram,
            binding.cvYoutube to culturalVenue.Youtube
        )

        socialIcons.forEach { (iconView, url) ->
            iconView.apply {
                setOnClickListener {
                    if(url.isNotEmpty())
                        openUrlInBrowser(url)
                    else
                        Toast.makeText(context, getString(R.string.no_link), Toast.LENGTH_SHORT,).show()
                }
            }
        }
    }

    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun toggleDescriptionExpansion() {
        if (isExpanded) {
            binding.cvDescription.maxLines = 3
            binding.tvVerMas.text = getString(R.string.see_more)
            isExpanded = false
        } else {
            binding.cvDescription.maxLines = Int.MAX_VALUE
            binding.tvVerMas.text = getString(R.string.see_less)
            isExpanded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}