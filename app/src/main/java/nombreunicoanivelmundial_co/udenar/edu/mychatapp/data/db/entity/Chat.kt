package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity

import com.google.firebase.database.PropertyName

/*estas clases y anotaciones están destinadas a modelar la estructura de datos del chat en la
base de datos Firebase en tiempo real.
Los nombres personalizados de las propiedades se utilizan para que los datos se almacenen y
recuperen con nombres específicos en Firebase*/

data class Chat(
    @get:PropertyName("lastMessage") @set:PropertyName("lastMessage") var lastMessage: Message = Message(),
    @get:PropertyName("info") @set:PropertyName("info") var info: ChatInfo = ChatInfo()
)

data class ChatInfo(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = ""
)