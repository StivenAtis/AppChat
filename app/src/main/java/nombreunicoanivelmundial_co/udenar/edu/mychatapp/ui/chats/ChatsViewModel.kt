package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.chats

import androidx.lifecycle.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Event
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.Chat
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.ChatWithUserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserFriend
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceValueObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.addNewItem
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.convertTwoUserIDs
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.updateItemAt

/* Proporciona una instancia del ViewModel ChatsViewModel con el ID del usuario actual (myUserID)
como parámetro. */
class ChatsViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {
    /* Se encarga de crear una instancia del ViewModel ChatsViewModel con el ID del
    usuario actual. */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModel(myUserID) as T
    }
}

/*  Maneja la lógica de presentación y la interacción de datos para la pantalla de la lista de
chats. */
class ChatsViewModel(val myUserID: String) : DefaultViewModel() {

    private val repository: DatabaseRepository = DatabaseRepository()
    private val firebaseReferenceObserverList = ArrayList<FirebaseReferenceValueObserver>()
    private val _updatedChatWithUserInfo = MutableLiveData<ChatWithUserInfo>()
    private val _selectedChat = MutableLiveData<Event<ChatWithUserInfo>>()

    var selectedChat: LiveData<Event<ChatWithUserInfo>> = _selectedChat
    val chatsList = MediatorLiveData<MutableList<ChatWithUserInfo>>()

    /* Configura un observador en _updatedChatWithUserInfo para actualizar la lista de chats cuando
    se produce un cambio. */
    init {
        chatsList.addSource(_updatedChatWithUserInfo) { newChat ->
            val chat = chatsList.value?.find { it.mChat.info.id == newChat.mChat.info.id }
            if (chat == null) {
                chatsList.addNewItem(newChat)
            } else {
                chatsList.updateItemAt(newChat, chatsList.value!!.indexOf(chat))
            }
        }
        setupChats()
    }

    /* Se llama cuando el ViewModel es destruido y se encarga de liberar losobservadores de
    Firebase. */
    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserverList.forEach { it.clear() }
    }

    /* Inicia la carga de amigos (userFriends) llamando a loadFriends. */
    private fun setupChats() {
        loadFriends()
    }

    /* Carga la lista de amigos del usuario actual y, para cada amigo, llama a loadUserInfo para
    cargar la información del usuario. */
    private fun loadFriends() {
        repository.loadFriends(myUserID) { result: Result<List<UserFriend>> ->
            onResult(null, result)
            if (result is Result.Success) result.data?.forEach { loadUserInfo(it) }
        }
    }

    /* Carga la información del usuario y, para cada usuario cargado, llama a loadAndObserveChat
    para cargar y observar los chats asociados. */
    private fun loadUserInfo(userFriend: UserFriend) {
        repository.loadUserInfo(userFriend.userID) { result: Result<UserInfo> ->
            onResult(null, result)
            if (result is Result.Success) result.data?.let { loadAndObserveChat(it) }
        }
    }

    /* Carga y observa el chat asociado a dos usuarios (myUserID y otro usuario) utilizando un
    observador de referencia de Firebase. */
    private fun loadAndObserveChat(userInfo: UserInfo) {
        val observer = FirebaseReferenceValueObserver()
        firebaseReferenceObserverList.add(observer)
        repository.loadAndObserveChat(
            convertTwoUserIDs(myUserID, userInfo.id),
            observer
        ) { result: Result<Chat> ->
            if (result is Result.Success) {
                _updatedChatWithUserInfo.value = result.data?.let { ChatWithUserInfo(it, userInfo) }
            } else if (result is Result.Error) {
                chatsList.value?.let {
                    val newList = mutableListOf<ChatWithUserInfo>().apply { addAll(it) }
                    newList.removeIf { it2 -> result.msg.toString().contains(it2.mUserInfo.id) }
                    chatsList.value = newList
                }
            }
        }
    }

    /* Se llama cuando se presiona un chat en la interfaz de usuario y notifica a _selectedChat
    con el chat seleccionado. */
    fun selectChatWithUserInfoPressed(chat: ChatWithUserInfo) {
        _selectedChat.value = Event(chat)
    }
}