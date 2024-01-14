package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model

/* Representa los datos necesarios para crear un nuevo usuario en el sistema. */
data class CreateUser(
    var displayName: String = "",
    var email: String = "",
    var password: String = ""
)