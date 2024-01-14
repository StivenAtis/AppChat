package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.settings

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceValueObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.AuthRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.StorageRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Event
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result

/* Proporciona una instancia del SettingsViewModel con el ID de usuario (userID). */
class SettingsViewModelFactory(private val userID: String) : ViewModelProvider.Factory {
    /* Crea y devuelve una instancia de SettingsViewModel con el ID de usuario proporcionado. */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(userID) as T
    }
}

/* Administra la lógica de negocios y la interacción de la interfaz de usuario relacionada
con la pantalla de configuración. */
class SettingsViewModel(private val userID: String) : DefaultViewModel() {

    private val dbRepository: DatabaseRepository = DatabaseRepository()
    private val storageRepository = StorageRepository()
    private val authRepository = AuthRepository()

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    /*  LiveData que contiene la información del usuario.*/
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _editStatusEvent = MutableLiveData<Event<Unit>>()
    /* LiveData<Event<Unit>> que representa el evento de edición de estado. */
    val editStatusEvent: LiveData<Event<Unit>> = _editStatusEvent

    private val _editImageEvent = MutableLiveData<Event<Unit>>()
    /* LiveData<Event<Unit>> que representa el evento de edición de imagen. */
    val editImageEvent: LiveData<Event<Unit>> = _editImageEvent

    private val _logoutEvent = MutableLiveData<Event<Unit>>()
    /* LiveData<Event<Unit>> que representa el evento de cierre de sesión. */
    val logoutEvent: LiveData<Event<Unit>> = _logoutEvent

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()

    init {
        loadAndObserveUserInfo()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    /* Carga y observa la información del usuario en tiempo real. */
    private fun loadAndObserveUserInfo() {
        dbRepository.loadAndObserveUserInfo(userID, firebaseReferenceObserver)
        { result: Result<UserInfo> -> onResult(_userInfo, result) }
    }

    /* Actualiza el estado del usuario en la base de datos. */
    fun changeUserStatus(status: String) {
        dbRepository.updateUserStatus(userID, status)
    }

    /* Actualiza la imagen de perfil del usuario en el almacenamiento y actualiza la URL de la
    imagen en la base de datos. */
    fun changeUserImage(byteArray: ByteArray) {
        storageRepository.updateUserProfileImage(userID, byteArray) { result: Result<Uri> ->
            onResult(null, result)
            if (result is Result.Success) {
                dbRepository.updateUserProfileImageUrl(userID, result.data.toString())
            }
        }
    }

    /* Dispara un evento indicando que se presionó el botón para cambiar la imagen del usuario. */
    fun changeUserImagePressed() {
        _editImageEvent.value = Event(Unit)
    }

    /* Dispara un evento indicando que se presionó el botón para cambiar el estado del usuario. */
    fun changeUserStatusPressed() {
        _editStatusEvent.value = Event(Unit)
    }

    /* Cierra la sesión del usuario y dispara un evento indicando que se presionó el botón de
    cierre de sesión. */
    fun logoutUserPressed() {
        authRepository.logoutUser()
        _logoutEvent.value = Event(Unit)
    }
}

