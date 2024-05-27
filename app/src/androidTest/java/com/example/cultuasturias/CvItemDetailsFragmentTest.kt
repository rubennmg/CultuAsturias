package com.example.cultuasturias

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.cultuasturias.ui.CvItemDetailsFragment
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Verificar que el fragmento de detalles de un cultural venue muestra
 * correctamente la información. Uso de Espresso y Espresso Intents.
 *
 */
class CvItemDetailsFragmentTest {

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testCvItemDetailsFragment() {
        // Crear el escenario del fragmento en un contenedor
        val fragmentArgs = Bundle().apply {
            putString("culturalVenue", "Dolmen de Entrerríos")
        }
        val scenario = launchFragmentInContainer<CvItemDetailsFragment>(fragmentArgs)

        // Verificar que las vistas iniciales se muestren correctamente
        onView(withId(R.id.cvName)).check(matches(isDisplayed()))
        onView(withId(R.id.cvDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.cvDireccion)).check(matches(isDisplayed()))
        onView(withId(R.id.cvLocation)).check(matches(isDisplayed()))
        onView(withId(R.id.cvTelefono)).check(matches(isDisplayed()))
        onView(withId(R.id.cvObservaciones)).check(matches(isDisplayed()))
        onView(withId(R.id.tvVerMas)).check(matches(isDisplayed()))

        // Verificar que los enlaces sociales están presentes y funcionan correctamente
        onView(withId(R.id.cvFacebook)).check(matches(isDisplayed())).perform(click())
        Intents.intended(hasAction(Intent.ACTION_VIEW))

        onView(withId(R.id.cvTwitter)).check(matches(isDisplayed())).perform(click())
        Intents.intended(hasAction(Intent.ACTION_VIEW))

        onView(withId(R.id.cvInstagram)).check(matches(isDisplayed())).perform(click())
        Intents.intended(hasAction(Intent.ACTION_VIEW))

        onView(withId(R.id.cvYoutube)).check(matches(isDisplayed())).perform(click())
        Intents.intended(hasAction(Intent.ACTION_VIEW))
    }
}