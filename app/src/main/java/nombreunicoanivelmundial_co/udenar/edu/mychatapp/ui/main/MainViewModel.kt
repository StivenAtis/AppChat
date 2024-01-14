package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.App
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserNotification
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseAuthStateObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceConnectedObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseReferenceValueObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.AuthRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import com.google.firebase.auth.FirebaseUser

class MainViewModel : ViewModel() {

    private val dbRepository = DatabaseRepository()
    private val authRepository = AuthRepository()

    private val _userNotificationsList = MutableLiveData<MutableList<UserNotification>>()

    private val fbRefNotificationsObserver = FirebaseReferenceValueObserver()
    private val fbAuthStateObserver = FirebaseAuthStateObserver()
    private val fbRefConnectedObserver = FirebaseReferenceConnectedObserver()

    /* Se define un LiveData mutable llamado _userNotificationsList para almacenar la lista de
    notificaciones del usuario. Este LiveData se expone como userNotificationsList para que las
    actividades o fragmentos interesados puedan observar cambios en la lista de notificaciones. */
    var userNotificationsList: LiveData<MutableList<UserNotification>> = _userNotificationsList

    /* se llama a setupAuthObserver(), que inicia la observación del estado de autenticación de
    Firebase (FirebaseAuthStateObserver). Dependiendo del resultado de la autenticación, se inicia
    o detiene la observación de notificaciones y se establece el estado de conexión. */
    init {
        setupAuthObserver()
    }

    /* Se sobrescribe el método onCleared de la clase ViewModel para limpiar los observadores al
    liberar el ViewModel. */
    override fun onCleared() {
        super.onCleared()
        fbRefNotificationsObserver.clear()
        fbRefConnectedObserver.clear()
        fbAuthStateObserver.clear()
    }

    /* Este método configura un observador para el estado de autenticación de Firebase.
    Cuando el estado de autenticación cambia, se decide si comenzar o detener la observación de
    notificaciones y configurar el estado de conexión. */
    private fun setupAuthObserver() {
        authRepository.observeAuthState(fbAuthStateObserver) { result: Result<FirebaseUser> ->
            if (result is Result.Success) {
                val userID = result.data!!.uid
                startObservingNotifications(userID)
                fbRefConnectedObserver.start(userID)
            } else {
                fbRefConnectedObserver.clear()
                stopObservingNotifications()
            }
        }
    }

    /* ste método inicia la observación de las notificaciones del usuario a través del repositorio
    de la base de datos. Cuando se reciben los resultados, se actualiza el LiveData
    _userNotificationsList con la nueva lista de notificaciones. */
    private fun startObservingNotifications(userID: String) {
        dbRepository.loadAndObserveUserNotifications(
            userID,
            fbRefNotificationsObserver
        ) { result: Result<MutableList<UserNotification>> ->
            if (result is Result.Success) {
                _userNotificationsList.value = result.data!!
            }
        }
    }

    /* Este método detiene la observación de notificaciones al limpiar el observador
    (fbRefNotificationsObserver). */
    private fun stopObservingNotifications() {
        fbRefNotificationsObserver.clear()
    }
}
