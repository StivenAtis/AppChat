package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository

import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseDataSource
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceChildObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceValueObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.wrapSnapshotToArrayList
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.wrapSnapshotToClass

/* Proporciona métodos para realizar operaciones de lectura y escritura en la base de datos
Firebase Realtime Database. */
class DatabaseRepository {
    private val firebaseDatabaseService = FirebaseDataSource()

    //region Update

    //Funciones para Actualizar
    /* Actualiza el estado de un usuario. */
    fun updateUserStatus(userID: String, status: String) {
        firebaseDatabaseService.updateUserStatus(userID, status)
    }

    /* Agrega un nuevo mensaje a una conversación. */
    fun updateNewMessage(messagesID: String, message: Message) {
        firebaseDatabaseService.pushNewMessage(messagesID, message)
    }

    /* Agrega un nuevo usuario a la base de datos. */
    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }

    /* Agrega una nueva amistad entre dos usuarios.  */
    fun updateNewFriend(myUser: UserFriend, otherUser: UserFriend) {
        firebaseDatabaseService.updateNewFriend(myUser, otherUser)
    }

    /* Agrega una nueva solicitud de amistad enviada. */
    fun updateNewSentRequest(userID: String, userRequest: UserRequest) {
        firebaseDatabaseService.updateNewSentRequest(userID, userRequest)
    }

    /* Agrega una nueva notificación a un usuario. */
    fun updateNewNotification(otherUserID: String, userNotification: UserNotification) {
        firebaseDatabaseService.updateNewNotification(otherUserID, userNotification)
    }

    /* Actualiza el último mensaje de una conversación. */
    fun updateChatLastMessage(chatID: String, message: Message) {
        firebaseDatabaseService.updateLastMessage(chatID, message)
    }

    /* Agrega una nueva conversación. */
    fun updateNewChat(chat: Chat){
        firebaseDatabaseService.updateNewChat(chat)
    }

    /* Actualiza la url de la imagen de un usuario */
    fun updateUserProfileImageUrl(userID: String, url: String){
        firebaseDatabaseService.updateUserProfileImageUrl(userID, url)
    }

    //endregion

    //region Remove

    //Funciones para Eliminar
    /* Elimina una notificación de un usuario. */
    fun removeNotification(userID: String, notificationID: String) {
        firebaseDatabaseService.removeNotification(userID, notificationID)
    }

    /*  Elimina una amistad entre dos usuarios. */
    fun removeFriend(userID: String, friendID: String) {
        firebaseDatabaseService.removeFriend(userID, friendID)
    }

    /* Elimina una solicitud de amistad enviada. */
    fun removeSentRequest(otherUserID: String, myUserID: String) {
        firebaseDatabaseService.removeSentRequest(otherUserID, myUserID)
    }

    /* Elimina una conversación. */
    fun removeChat(chatID: String) {
        firebaseDatabaseService.removeChat(chatID)
    }

    /* Elimina mensajes de una conversación. */
    fun removeMessages(messagesID: String){
        firebaseDatabaseService.removeMessages(messagesID)
    }

    //endregion

    //region Load Single

    //Funciones para Cargar Datos
    /* Carga la información de un usuario. */
    fun loadUser(userID: String, b: ((Result<User>) -> Unit)) {
        firebaseDatabaseService.loadUserTask(userID).addOnSuccessListener {
            b.invoke(Result.Success(wrapSnapshotToClass(User::class.java, it)))
        }.addOnFailureListener { b.invoke(Result.Error(it.message)) }
    }

    /* Carga la información de un usuario (detalles del perfil). */
    fun loadUserInfo(userID: String, b: ((Result<UserInfo>) -> Unit)) {
        firebaseDatabaseService.loadUserInfoTask(userID).addOnSuccessListener {
            b.invoke(Result.Success(wrapSnapshotToClass(UserInfo::class.java, it)))
        }.addOnFailureListener { b.invoke(Result.Error(it.message)) }
    }

    /* Carga la información de una conversación. */
    fun loadChat(chatID: String, b: ((Result<Chat>) -> Unit)) {
        firebaseDatabaseService.loadChatTask(chatID).addOnSuccessListener {
            b.invoke(Result.Success(wrapSnapshotToClass(Chat::class.java, it)))
        }.addOnFailureListener { b.invoke(Result.Error(it.message)) }
    }

    //endregion

    //region Load List

    //Funciones para Cargar Listas de Datos
    /* Carga la lista de usuarios. */
    fun loadUsers(b: ((Result<MutableList<User>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadUsersTask().addOnSuccessListener {
            val usersList = wrapSnapshotToArrayList(User::class.java, it)
            b.invoke(Result.Success(usersList))
        }.addOnFailureListener { b.invoke(Result.Error(it.message)) }
    }

    /* Carga la lista de amigos de un usuario. */
    fun loadFriends(userID: String, b: ((Result<List<UserFriend>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadFriendsTask(userID).addOnSuccessListener {
            val friendsList = wrapSnapshotToArrayList(UserFriend::class.java, it)
            b.invoke(Result.Success(friendsList))
        }.addOnFailureListener { b.invoke(Result.Error(it.message)) }
    }

    /* Carga la lista de notificaciones de un usuario. */
    fun loadNotifications(userID: String, b: ((Result<MutableList<UserNotification>>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseDatabaseService.loadNotificationsTask(userID).addOnSuccessListener {
            val notificationsList = wrapSnapshotToArrayList(UserNotification::class.java, it)
            b.invoke(Result.Success(notificationsList))
        }.addOnFailureListener { b.invoke(Result.Error(it.message)) }
    }

    //endregion

    //#region Load and Observe

    // Funciones para Cargar y Observar Datos
    /* Carga y observa la información de un usuario. */
    fun loadAndObserveUser(userID: String, observer: FirebaseReferenceValueObserver, b: ((Result<User>) -> Unit)) {
        firebaseDatabaseService.attachUserObserver(User::class.java, userID, observer, b)
    }

    /* Carga y observa la información del perfil de un usuario. */
    fun loadAndObserveUserInfo(userID: String, observer: FirebaseReferenceValueObserver, b: ((Result<UserInfo>) -> Unit)) {
        firebaseDatabaseService.attachUserInfoObserver(UserInfo::class.java, userID, observer, b)
    }

    /* Carga y observa la lista de notificaciones de un usuario. */
    fun loadAndObserveUserNotifications(userID: String, observer: FirebaseReferenceValueObserver, b: ((Result<MutableList<UserNotification>>) -> Unit)){
        firebaseDatabaseService.attachUserNotificationsObserver(UserNotification::class.java, userID, observer, b)
    }

    /* Observa mensajes nuevos agregados a una conversación. */
    fun loadAndObserveMessagesAdded(messagesID: String, observer: FirebaseReferenceChildObserver, b: ((Result<Message>) -> Unit)) {
        firebaseDatabaseService.attachMessagesObserver(Message::class.java, messagesID, observer, b)
    }

    /* Carga y observa la información de una conversación. */
    fun loadAndObserveChat(chatID: String, observer: FirebaseReferenceValueObserver, b: ((Result<Chat>) -> Unit)) {
        firebaseDatabaseService.attachChatObserver(Chat::class.java, chatID, observer, b)
    }

    //endregion
}

