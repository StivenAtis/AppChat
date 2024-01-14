package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data

/* es una clase sellada, lo que significa que tiene un conjunto finito de subclases selladas que
pueden ser utilizadas para representar diferentes estados. */
sealed class Result<out R> {
    data class Success<out T>(val data: T? = null, val msg: String? = null) : Result<T>()
    class Error(val msg: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/* Esta estructura es útil cuando se desea tener una representación más robusta de los resultados
de operaciones asíncronas. */