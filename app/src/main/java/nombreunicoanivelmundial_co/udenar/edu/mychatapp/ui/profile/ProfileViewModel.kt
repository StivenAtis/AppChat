package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.profile

import androidx.lifecycle.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceValueObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.convertTwoUserIDs

/* Implementa la interfaz ViewModelProvider.Factory para proporcionar una instancia del
ProfileViewModel con los identificadores del usuario actual (myUserID) y del otro usuario
(otherUserID). */
class ProfileViewModelFactory(private val myUserID: String, private val otherUserID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(myUserID, otherUserID) as T
    }
}

/* Enumera los diferentes estados posibles del diseño de la interfaz de usuario en función de la
relación entre el usuario actual y el otro usuario. */
enum class LayoutState {

    /* IS_FRIEND: Indica que el usuario actual y el otro usuario son amigos.
    NOT_FRIEND: Indica que el usuario actual y el otro usuario no son amigos.
    ACCEPT_DECLINE: Indica que hay una solicitud de amistad pendiente y se debe mostrar la opción
    para aceptar o rechazar.
    REQUEST_SENT: Indica que se ha enviado una solicitud de amistad y se debe mostrar un estado
    relacionado. */
    IS_FRIEND, NOT_FRIEND, ACCEPT_DECLINE, REQUEST_SENT
}

/* Representa el ViewModel para la pantalla del perfil de usuario, maneja la lógica de negocios y
las interacciones de la interfaz de usuario. */
class ProfileViewModel(private val myUserID: String, private val userID: String) :
    DefaultViewModel() {

    private val repository: DatabaseRepository = DatabaseRepository()
    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()
    private val _myUser: MutableLiveData<User> = MutableLiveData()
    private val _otherUser: MutableLiveData<User> = MutableLiveData()

    val otherUser: LiveData<User> = _otherUser
    val layoutState = MediatorLiveData<LayoutState>()

    init {
        layoutState.addSource(_myUser) { updateLayoutState(it, _otherUser.value) }
        setupProfile()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun updateLayoutState(myUser: User?, otherUser: User?) {
        if (myUser != null && otherUser != null) {
            layoutState.value = when {
                myUser.friends[otherUser.info.id] != null -> LayoutState.IS_FRIEND
                myUser.notifications[otherUser.info.id] != null -> LayoutState.ACCEPT_DECLINE
                myUser.sentRequests[otherUser.info.id] != null -> LayoutState.REQUEST_SENT
                else -> LayoutState.NOT_FRIEND
            }
        }
    }

    /*  carga y observa el usuario del perfil actual y del otro usuario, actualizando las
    instancias _myUser y _otherUser. */
    private fun setupProfile() {
        repository.loadUser(userID) { result: Result<User> ->
            onResult(_otherUser, result)
            if (result is Result.Success) {
                repository.loadAndObserveUser(
                    myUserID,
                    firebaseReferenceObserver
                ) { result2: Result<User> ->
                    onResult(_myUser, result2)
                }
            }
        }
    }

    /* Define métodos como addFriendPressed, removeFriendPressed, acceptFriendRequestPressed, y
    declineFriendRequestPressed para manejar las acciones del usuario en función del estado de la relación. */
    fun addFriendPressed() {
        repository.updateNewSentRequest(myUserID, UserRequest(_otherUser.value!!.info.id))
        repository.updateNewNotification(_otherUser.value!!.info.id, UserNotification(myUserID))
    }

    fun removeFriendPressed() {
        repository.removeFriend(myUserID, _otherUser.value!!.info.id)
        repository.removeChat(convertTwoUserIDs(myUserID, _otherUser.value!!.info.id))
        repository.removeMessages(convertTwoUserIDs(myUserID, _otherUser.value!!.info.id))
    }

    fun acceptFriendRequestPressed() {
        repository.updateNewFriend(UserFriend(myUserID), UserFriend(_otherUser.value!!.info.id))

        val newChat = Chat().apply {
            info.id = convertTwoUserIDs(myUserID, _otherUser.value!!.info.id)
            lastMessage = Message(seen = true, text = "Say hello!")
        }

        repository.updateNewChat(newChat)
        repository.removeNotification(myUserID, _otherUser.value!!.info.id)
        repository.removeSentRequest(_otherUser.value!!.info.id, myUserID)
    }

    fun declineFriendRequestPressed() {
        repository.removeSentRequest(myUserID, _otherUser.value!!.info.id)
        repository.removeNotification(myUserID, _otherUser.value!!.info.id)
    }
}
