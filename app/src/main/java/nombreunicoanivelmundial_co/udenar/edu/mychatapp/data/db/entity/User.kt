package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity

import com.google.firebase.database.PropertyName

/* Representa la entidad de usuario principal con su información básica, lista de amigos,
notificaciones y solicitudes enviadas. */
data class User(
    @get:PropertyName("info") @set:PropertyName("info") var info: UserInfo = UserInfo(),
    @get:PropertyName("friends") @set:PropertyName("friends") var friends: HashMap<String, UserFriend> = HashMap(),
    @get:PropertyName("notifications") @set:PropertyName("notifications") var notifications: HashMap<String, UserNotification> = HashMap(),
    @get:PropertyName("sentRequests") @set:PropertyName("sentRequests") var sentRequests: HashMap<String, UserRequest> = HashMap()
)

/* Representa la entidad de un amigo de usuario, utilizado dentro de la clase User. */
data class UserFriend(
    @get:PropertyName("userID") @set:PropertyName("userID") var userID: String = ""
)

/* Representa la entidad de información básica del usuario, utilizado dentro de la clase User. */
data class UserInfo(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("displayName") @set:PropertyName("displayName") var displayName: String = "",
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "No status",
    @get:PropertyName("profileImageUrl") @set:PropertyName("profileImageUrl") var profileImageUrl: String = "",
    @get:PropertyName("online") @set:PropertyName("online") var online: Boolean = false
)

/* Representa la entidad de una notificación de usuario, utilizado dentro de la clase User. */
data class UserNotification(
    @get:PropertyName("userID") @set:PropertyName("userID") var userID: String = ""
)

/* Representa la entidad de una solicitud de usuario enviada, utilizado dentro de la clase User. */
data class UserRequest(
    @get:PropertyName("userID") @set:PropertyName("userID") var userID: String = ""
)

