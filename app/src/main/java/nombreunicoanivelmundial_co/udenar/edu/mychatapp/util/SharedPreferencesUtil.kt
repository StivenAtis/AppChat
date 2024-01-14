package nombreunicoanivelmundial_co.udenar.edu.mychatapp.util

import android.content.Context
import android.content.SharedPreferences

/** Proporciona métodos para acceder, guardar y eliminar información del SharedPreferences.
 * En este caso, se utiliza para gestionar el ID del usuario. **/

object SharedPreferencesUtil {
    private const val PACKAGE_NAME = "nombreunicoanivelmundial_co.udenar.edu.mychatapp"
    private const val KEY_USER_ID = "user_info"

    /* Devuelve una instancia de SharedPreferences asociada al nombre del paquete de la aplicación
    y con modo privado. */
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    /* Obtiene el ID del usuario almacenado en las preferencias compartidas. Devuelve null si no se
    encuentra el valor. */
    fun getUserID(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_ID, null)
    }

    /* Guarda el ID del usuario en las preferencias compartidas. */
    fun saveUserID(context: Context, userID: String) {
        getPrefs(context).edit().putString(KEY_USER_ID, userID).apply()
    }

    /* Elimina el ID del usuario de las preferencias compartidas. */
    fun removeUserID(context: Context) {
        getPrefs(context).edit().remove(KEY_USER_ID).apply()
    }
}