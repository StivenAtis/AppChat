package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.notifications

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.addNewItem
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.removeItem
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.convertTwoUserIDs

/* Proporciona una instancia de NotificationsViewModel con el myUserID especificado. */
class NotificationsViewModelFactory(private val myUserID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(myUserID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class NotificationsViewModel(private val myUserID: String) : DefaultViewModel() {

    private val dbRepository: DatabaseRepository = DatabaseRepository()
    private val updatedUserInfo = MutableLiveData<UserInfo>()
    private val userNotificationsList = MutableLiveData<MutableList<UserNotification>>()

    /* actúa como un contenedor para la lista actualizada de información de usuarios. */
    val usersInfoList = MediatorLiveData<MutableList<UserInfo>>()

    init {
        usersInfoList.addSource(updatedUserInfo) { usersInfoList.addNewItem(it) }
        loadNotifications()
    }

    /* Carga las notificaciones de usuario utilizando dbRepository y actualiza la lista de
    información del usuario correspondiente. */
    private fun loadNotifications() {
        dbRepository.loadNotifications(myUserID) { result: Result<MutableList<UserNotification>> ->
            onResult(userNotificationsList, result)
            if (result is Result.Success) result.data?.forEach { loadUserInfo(it) }
        }
    }

    /* Carga la información del usuario basándose en la notificación proporcionada. */
    private fun loadUserInfo(userNotification: UserNotification) {
        dbRepository.loadUserInfo(userNotification.userID) { result: Result<UserInfo> ->
            onResult(updatedUserInfo, result)
        }
    }

    /* Actualiza las notificaciones y realiza acciones específicas (como agregar un nuevo amigo y
    crear un nuevo chat) en respuesta a una notificación. */
    private fun updateNotification(otherUserInfo: UserInfo, removeOnly: Boolean) {
        val userNotification = userNotificationsList.value?.find {
            it.userID == otherUserInfo.id
        }

        if (userNotification != null) {
            if (!removeOnly) {
                dbRepository.updateNewFriend(UserFriend(myUserID), UserFriend(otherUserInfo.id))
                val newChat = Chat().apply {
                    info.id = convertTwoUserIDs(myUserID, otherUserInfo.id)
                    lastMessage = Message(seen = true, text = "Say hello!")
                }
                dbRepository.updateNewChat(newChat)
            }
            dbRepository.removeNotification(myUserID, otherUserInfo.id)
            dbRepository.removeSentRequest(otherUserInfo.id, myUserID)

            usersInfoList.removeItem(otherUserInfo)
            userNotificationsList.removeItem(userNotification)
        }
    }

    /* Maneja la acción de aceptar una notificación llamando a updateNotification con removeOnly
    como false. */
    fun acceptNotificationPressed(userInfo: UserInfo) {
        updateNotification(userInfo, false)
    }

    /* Maneja la acción de rechazar una notificación llamando a updateNotification con removeOnly
    como true. */
    fun declineNotificationPressed(userInfo: UserInfo) {
        updateNotification(userInfo, true)
    }
}