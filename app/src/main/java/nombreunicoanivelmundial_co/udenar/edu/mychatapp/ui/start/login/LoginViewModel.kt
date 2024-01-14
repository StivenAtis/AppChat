package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.Login
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.AuthRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Event
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.isEmailValid
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.isTextValid
import com.google.firebase.auth.FirebaseUser

/** gestiona la lógica asociada con el inicio de sesión de un usuario. **/

class LoginViewModel : DefaultViewModel() {

    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()

    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isLoggingIn = MutableLiveData<Boolean>()

    /* Realiza el intento de inicio de sesión utilizando la información proporcionada por el
    usuario (correo electrónico y contraseña). */
    private fun login() {
        /* Actualiza el estado de isLoggingIn durante el proceso de inicio de sesión. */
        isLoggingIn.value = true
        val login = Login(emailText.value!!, passwordText.value!!)

        authRepository.loginUser(login) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) _isLoggedInEvent.value = Event(result.data!!)
            if (result is Result.Success || result is Result.Error) isLoggingIn.value = false
        }
    }

    /* Se llama cuando el usuario presiona el botón de inicio de sesión. */
    fun loginPressed() {
        /* Realiza validaciones simples en el formato del correo electrónico y la longitud de la
        contraseña. */
        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        /* Llama a la función login para realizar el intento de inicio de sesión. */
        login()
    }
}