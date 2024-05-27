package com.example.cultuasturias.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cultuasturias.R
import com.example.cultuasturias.databinding.ActivityMainBinding
import com.example.cultuasturias.domain.SharedCvNameViewModel

/**
 * Actividad principal de la aplicación.
 *
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val sharedDataViewModel : SharedCvNameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restaurar el tema desde SharedPreferences
        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val themeMode = sharedPref.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)

        // Inflar el layout de la actividad
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cambiar el color de la barra de navegación (del dispositivo móvil)
        val window: Window = window
        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPhoneNavBar)

        // Configurar la navegación
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.cvListFragment,
                R.id.cvMapFragment,
                R.id.settingsFragment
            )
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavigation.setupWithNavController(navController)

        // Actualizar el nombre de búsqueda al escribir en el campo de texto
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sharedDataViewModel.setSearchName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Ocultar la barra de búsqueda en el mapa y el perfil
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.cvMapFragment, R.id.cvItemDetailsFragment, R.id.settingsFragment, R.id.aboutFragment -> {
                    binding.searchBar.visibility = View.GONE
                }
                else -> {
                    binding.searchBar.visibility = View.VISIBLE
                    binding.toolbar.title = ""
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        binding.toolbar.inflateMenu(R.menu.top_menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aboutFragment -> {
                navController.navigate(R.id.aboutFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}