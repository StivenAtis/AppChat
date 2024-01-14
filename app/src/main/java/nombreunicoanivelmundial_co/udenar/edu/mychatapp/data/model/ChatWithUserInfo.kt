package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model

import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.Chat
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserInfo

/* Representa la combinación de información de un chat y la información de un usuario asociado. */
data class ChatWithUserInfo(
    /* Una instancia de la clase Chat que contiene información sobre el chat, como su identificador,
    mensajes, etc. */
    var mChat: Chat,
    /* Una instancia de la clase UserInfo que contiene información sobre el usuario asociado al chat,
    como su nombre de visualización, estado, etc. */
    var mUserInfo: UserInfo
)
