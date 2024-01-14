package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.chat

import androidx.lifecycle.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.Chat
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.Message
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceChildObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceValueObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.addNewItem

/* esta clase irve como fábrica para crear instancias de ChatViewModel. */
class ChatViewModelFactory(private val myUserID: String, private val otherUserID: String, private val chatID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(myUserID, otherUserID, chatID) as T
    }
}

/* Gestiona la lógica del chat, incluyendo la carga de mensajes, la observación de cambios en la
base de datos, y el manejo de mensajes nuevos. */
class ChatViewModel(private val myUserID: String, private val otherUserID: String, private val chatID: String) : DefaultViewModel() {

    /* Se utiliza una instancia de DatabaseRepository para interactuar con la base de datos. */
    private val dbRepository: DatabaseRepository = DatabaseRepository()

    private val _otherUser: MutableLiveData<UserInfo> = MutableLiveData()
    private val _addedMessage = MutableLiveData<Message>()

    private val fbRefMessagesChildObserver = FirebaseReferenceChildObserver()
    private val fbRefUserInfoObserver = FirebaseReferenceValueObserver()

    /* Se declara LiveData para representar la lista de mensajes (messagesList), el texto del nuevo
    mensaje (newMessageText), y la información del otro usuario (otherUser). */
    val messagesList = MediatorLiveData<MutableList<Message>>()
    val newMessageText = MutableLiveData<String>()
    val otherUser: LiveData<UserInfo> = _otherUser

    /* Aqui se inicia la carga y observación de la información del otro usuario, también se verifica
    y se actualiza el estado del último mensaje visto. */
    init {
        setupChat()
        checkAndUpdateLastMessageSeen()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefMessagesChildObserver.clear()
        fbRefUserInfoObserver.clear()
    }

    /* Define métodos privados para cargar y observar mensajes nuevos, y para configurar el chat. */

    private fun checkAndUpdateLastMessageSeen() {
        dbRepository.loadChat(chatID) { result: Result<Chat> ->
            if (result is Result.Success && result.data != null) {
                result.data.lastMessage.let {
                    if (!it.seen && it.senderID != myUserID) {
                        it.seen = true
                        dbRepository.updateChatLastMessage(chatID, it)
                    }
                }
            }
        }
    }

    private fun setupChat() {
        dbRepository.loadAndObserveUserInfo(otherUserID, fbRefUserInfoObserver) { result: Result<UserInfo> ->
            onResult(_otherUser, result)
            if (result is Result.Success && !fbRefMessagesChildObserver.isObserving()) {
                loadAndObserveNewMessages()
            }
        }
    }

    private fun loadAndObserveNewMessages() {
        messagesList.addSource(_addedMessage) { messagesList.addNewItem(it) }

        dbRepository.loadAndObserveMessagesAdded(
            chatID,
            fbRefMessagesChildObserver
        ) { result: Result<Message> ->
            onResult(_addedMessage, result)
        }
    }

    /* Se implementa este metodo para enviar un nuevo mensaje, actualizando la base de datos con el
    nuevo mensaje y actualizando el último mensaje del chat. */
    fun sendMessagePressed() {
        if (!newMessageText.value.isNullOrBlank()) {
            val newMsg = Message(myUserID, newMessageText.value!!)
            dbRepository.updateNewMessage(chatID, newMsg)
            dbRepository.updateChatLastMessage(chatID, newMsg)
            newMessageText.value = null
        }
    }
}