package nombreunicoanivelmundial_co.udenar.edu.mychatapp.util

/* Verifica si la cadena proporcionada es una dirección de correo electrónico válida. Utiliza el
patrón predefinido de direcciones de correo electrónico proporcionado por Patterns.EMAIL_ADDRESS
para realizar la validación. */
fun isEmailValid(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

/* Verifica si la cadena proporcionada es válida según ciertos criterios.
La cadena no debe ser nula o en blanco (isNullOrBlank()).
La longitud de la cadena debe ser al menos igual a minLength. */
fun isTextValid(minLength: Int, text: String?): Boolean {
    if (text.isNullOrBlank() || text.length < minLength) {
        return false
    }
    return true
}

