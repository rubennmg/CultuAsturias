package com.example.cultuasturias.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.cultuasturias.databinding.FragmentSettingsBinding

/**
 * Fragmento para la vista de ajustes de la aplicación.
 * Se permite el cambio de tema oscuro/claro.
 *
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.btnLight.id -> {
                    // Cambiar al tema claro
                    activity?.let { setAppTheme(it, AppCompatDelegate.MODE_NIGHT_NO) }
                }
                binding.btnDark.id -> {
                    // Cambiar al tema oscuro
                    activity?.let { setAppTheme(it, AppCompatDelegate.MODE_NIGHT_YES) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAppTheme(context: Context, mode: Int) {
        when (mode) {
            AppCompatDelegate.MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            AppCompatDelegate.MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        // Guardar el modo seleccionado en SharedPreferences
        val sharedPref = context.getSharedPreferences("settings", AppCompatActivity.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("ThemeMode", mode)
            apply()
        }
    }
}