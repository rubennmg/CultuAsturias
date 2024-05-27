package com.example.cultuasturias


import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cultuasturias.db.CultuAstDatabase
import com.example.cultuasturias.db.CulturalVenueDAO
import com.example.cultuasturias.model.CulturalVenueItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

/**
 * Prueba básica de inserción y lectura de un CulturalVenueItem en la base de datos.
 *
 */
class CultuAstDatabaseTest {

    private lateinit var db: CultuAstDatabase
    private lateinit var dao: CulturalVenueDAO
    private val sampleVenue: CulturalVenueItem = CulturalVenueItem(
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
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, CultuAstDatabase::class.java).build()
        dao = db.culturalVenueDAO()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadVenue() = runBlocking {
        dao.insertCulturalVenue(sampleVenue)
        val venue = dao.getCulturalVenueByName("Museo de Arte Db Test").first()
        assertEquals(venue.Nombre, "Museo de Arte Db Test")
    }
}