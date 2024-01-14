package nombreunicoanivelmundial_co.udenar.edu.mychatapp

import android.app.Application
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.SharedPreferencesUtil

/** Esta clase actúa como la clase de aplicación principal de la aplicación y proporciona un punto
 * central para acceder al ID del usuario actual **/

class App : Application() {

    /* Sobrescribe el método onCreate() de la clase Application.
    Establece la instancia de la aplicación (application) al iniciar la aplicación. */
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    /* Contiene una propiedad estática llamada application que almacena la instancia de la aplicación. */
    companion object {
        lateinit var application: Application
            private set

        /* representa el ID del usuario actual. La propiedad obtiene su valor de
        SharedPreferencesUtil.getUserID(application.applicationContext).orEmpty(). Si el valor es
        nulo, se utiliza una cadena vacía. */
        var myUserID: String = ""
            get() {
                field = SharedPreferencesUtil.getUserID(application.applicationContext).orEmpty()
                return field
            }
            private set
    }
}
