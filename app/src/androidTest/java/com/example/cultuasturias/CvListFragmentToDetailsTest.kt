package com.example.cultuasturias

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cultuasturias.domain.CulturalVenueViewModel
import com.example.cultuasturias.model.CulturalVenueItem
import com.example.cultuasturias.ui.CultuAstUIState
import com.example.cultuasturias.ui.CulturalVenuesViewHolder
import com.example.cultuasturias.ui.CvListFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*

/**
 * Prueba de navegación desde CvListFragment a CvItemDetailsFragment.
 * Se simula un clic en un elemento del RecyclerView y se verifica que se navega al fragmento de detalles
 * con un objeto CulturalVenueItem simulado.
 *
 * No se ha conseguido su correcta ejecución (muchos problemas con mockito). Resultado de ejecución:
 *     Cannot mock/spy class com.example.cultuasturias.domain.CulturalVenueViewModel
 *     Mockito cannot mock/spy because :
 *     - final class
 *     at com.example.cultuasturias.CvListFragmentToDetailsTest.setup(CvListFragmentToDetailsTest.kt:84)
 *
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CvListFragmentToDetailsTest {

    @Mock
    lateinit var culturalVenueViewModel: CulturalVenueViewModel

    private lateinit var scenario: FragmentScenario<CvListFragment>
    private val culturalVenueItem = CulturalVenueItem(
        AccesoATerrazaOZonaExterior = "Sí",
        AccesoAlInterior = "Sí",
        AdmitenOtrasMascotasAdemasDePerros = "No",
        Archivo = "archivo.pdf",
        Canal = "Canal123",
        CodigoPostal = "33001",
        Concejo = "Oviedo",
        Coordenadas = "43.3603,-5.84476",
        CupoMaximoDePerrosEnElInterior = "5",
        DetalleDeLasNormasEspecificas = "No se permiten mascotas en la zona de comedor",
        Direccion = "Calle Test 123",
        Email = "info@culturalvenuedbtest.com",
        Facebook = "facebook.com/culturalvenuedbtest",
        FechaDeConstruccion = "1890",
        Horario = "09:00 - 18:00",
        Instagram = "instagram.com/culturalvenue",
        LimitacionDelTamanopesoDeLaMascota = " ",
        Localidad = "Asturias",
        MasInformacion = "Más información en el sitio web",
        Nombre = "Museo de Arte Db Test",
        NumeroMaximoDePerrosPorPersona = "2",
        Observaciones = "Muy accesible",
        OtrasMascotasPermitidas = "Gorilas",
        Pinterest = "pinterest.com/culturalvenuedbtest",
        Rss = "rss.culturalvenue.com",
        SeAdmitenPppPerrosPotencialmentePeligrosos = "No",
        Slide = "slide.jpg, slide2.jgp",
        Tarifas = "5€ adultos, 3€ niños, gratis con mascotas",
        Telefono = "123-456-789",
        Texto = "Bienvenido al Museo de Arte",
        Titulo = "Museo de Arte",
        Twitter = "twitter.com/culturalvenuesdbtest",
        Url = "dbtest.com",
        Web = "culturalvenuetest.com",
        Youtube = "youtube.com/culturalvenuedbtest",
        Zona = "Centro de Asturias"
    )

    @Before
    fun setup() {
        culturalVenueViewModel = mock(CulturalVenueViewModel::class.java)

        // Lanzar el fragmento
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme_Light) {
            CvListFragment().apply {
                // Inyectar el ViewModel
                val viewModelField = CvListFragment::class.java.getDeclaredField("culturalVenueViewModel")
                viewModelField.isAccessible = true
                viewModelField.set(this, culturalVenueViewModel)
            }
        }
    }

    @Test
    fun testNavigationToCvItemDetailsFragment() = runTest {
        // Configurar el ViewModel para devolver un estado de éxito con un cultural venue
        `when`(culturalVenueViewModel.cultuAstUIStateObservable)
            .thenReturn(MutableLiveData<CultuAstUIState>().apply {
                value = CultuAstUIState.Success(listOf(culturalVenueItem))
            })

        val navController = mock(NavController::class.java)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Simular clic en el primer elemento de la lista
        onView(withId(R.id.recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CulturalVenuesViewHolder>(0, click()))

        // Comprobar que se ha navegado al fragmento de detalles
        verify(navController).navigate(
            R.id.action_cvListFragment_to_cvItemDetailsFragment,
            any(Bundle::class.java)
        )
    }
}