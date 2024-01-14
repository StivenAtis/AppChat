package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity

import com.google.firebase.database.PropertyName
import java.util.*

/*esta clase representa los mensajes da la aplicación que utiliza la base de datos en tiempo real de
Firebase. Los nombres personalizados de las propiedades se utilizan para que los datos se almacenen
y recuperen con nombres específicos en Firebase*/
data class Message(
    @get:PropertyName("senderID") @set:PropertyName("senderID") var senderID: String = "",
    @get:PropertyName("text") @set:PropertyName("text") var text: String = "",
    @get:PropertyName("epochTimeMs") @set:PropertyName("epochTimeMs") var epochTimeMs: Long = Date().time,
    @get:PropertyName("seen") @set:PropertyName("seen") var seen: Boolean = false
)