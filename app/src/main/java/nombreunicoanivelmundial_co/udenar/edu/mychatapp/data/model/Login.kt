package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model

/* Representa los datos necesarios para autenticar a un usuario existente en el sistema. */
data class Login(
    var email: String = "",
    var password: String = ""
)