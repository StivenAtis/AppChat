package nombreunicoanivelmundial_co.udenar.edu.mychatapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import com.google.android.material.snackbar.Snackbar

/* Oculta el teclado virtual asociado a la vista actual.
Utiliza el InputMethodManager para ocultar el teclado virtual desde la ventana de la vista. */
fun View.forceHideKeyboard() {
    val inputManager: InputMethodManager =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

/* Muestra un Snackbar en la vista actual con el texto proporcionado.
Utiliza la clase Snackbar de Material Design para mostrar un mensaje emergente con duración corta. */
fun View.showSnackBar(text: String) {
    Snackbar.make(this.rootView.findViewById(R.id.container), text, Snackbar.LENGTH_SHORT).show()
}