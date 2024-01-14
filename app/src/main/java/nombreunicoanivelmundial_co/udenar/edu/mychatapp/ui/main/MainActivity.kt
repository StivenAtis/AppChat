package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseDataSource
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.forceHideKeyboard
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

/* actividad principal de la aplicación */

/* Aquí se configuran y gestionan diferentes elementos, como la barra de navegación inferior
(BottomNavigationView), la barra de herramientas (Toolbar), y se establecen observadores para
manejar eventos relacionados con el ciclo de vida de la actividad y las notificaciones del sistema. */
class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var mainToolbar: Toolbar
    private lateinit var notificationsBadge: BadgeDrawable
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* infla la interfaz de usuario desde el archivo de diseño activity_main.xml. */
        setContentView(R.layout.activity_main)

        mainToolbar = findViewById(R.id.main_toolbar)
        navView = findViewById(R.id.nav_view)
        mainProgressBar = findViewById(R.id.main_progressBar)

        notificationsBadge =
            navView.getOrCreateBadge(R.id.navigation_notifications).apply { isVisible = false }

        setSupportActionBar(mainToolbar)

        /* Se crea un controlador de navegación (navController) para gestionar la navegación entre
        fragmentos. */
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.profileFragment -> navView.visibility = View.GONE
                R.id.chatFragment -> navView.visibility = View.GONE
                R.id.startFragment -> navView.visibility = View.GONE
                R.id.loginFragment -> navView.visibility = View.GONE
                R.id.createAccountFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
            showGlobalProgressBar(false)
            currentFocus?.rootView?.forceHideKeyboard()
        }

        /* gestionar la barra de acción junto con la navegación. */
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chats,
                R.id.navigation_notifications,
                R.id.navigation_users,
                R.id.navigation_settings,
                R.id.startFragment
            )
        )

        /* habilitar la navegación de la barra de acción con el controlador de navegación. */
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /* Se sobrescriben los métodos onPause y onResume para manejar el estado de conexión a la base
    de datos Firebase. La aplicación va offline (goOffline()) cuando la actividad está en pausa y
    vuelve online (goOnline()) cuando se reanuda. */

    override fun onPause() {
        super.onPause()
        FirebaseDataSource.dbInstance.goOffline()
    }

    override fun onResume() {
        FirebaseDataSource.dbInstance.goOnline()
        setupViewModelObservers()
        super.onResume()
    }

    /* Se establece un observador para la lista de notificaciones del usuario
    (viewModel.userNotificationsList). Este observador actualiza el distintivo (BadgeDrawable) en
    la barra de navegación en función de la cantidad de notificaciones. */
    private fun setupViewModelObservers() {
        viewModel.userNotificationsList.observe(this, {
            if (it.size > 0) {
                notificationsBadge.number = it.size
                notificationsBadge.isVisible = true
            } else {
                notificationsBadge.isVisible = false
            }
        })
    }

    /* ste método muestra u oculta una barra de progreso global (mainProgressBar) en la interfaz de
    usuario según el valor del parámetro show. */
    fun showGlobalProgressBar(show: Boolean) {
        if (show) mainProgressBar.visibility = View.VISIBLE
        else mainProgressBar.visibility = View.GONE
    }
}