package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.start.createAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Event
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.User
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.AuthRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.CreateUser
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.isEmailValid
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.isTextValid
import com.google.firebase.auth.FirebaseUser

/**  manejar la lógica relacionada con la creación de cuentas de usuario en la aplicación. **/


class CreateAccountViewModel : DefaultViewModel() {

    private val dbRepository = DatabaseRepository()
    private val authRepository = AuthRepository()

    /* MutableLiveData que contiene un evento de éxito al crear una cuenta. Se utiliza para
    notificar a las actividades o fragmentos sobre la creación exitosa de la cuenta. */
    private val mIsCreatedEvent = MutableLiveData<Event<FirebaseUser>>()

    val isCreatedEvent: LiveData<Event<FirebaseUser>> = mIsCreatedEvent

    /* MutableLiveData que almacena los valores de texto ingresados por el usuario para el nombre
    de usuario, correo electrónico y contraseña respectivamente. */
    val displayNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()

    /* MutableLiveData que indica si actualmente se está llevando a cabo el proceso de creación de
    cuenta. */
    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser =
            CreateUser(displayNameText.value!!, emailText.value!!, passwordText.value!!)

        authRepository.createUser(createUser) { result: Result<FirebaseUser> ->
            onResult(null, result)
            if (result is Result.Success) {
                mIsCreatedEvent.value = Event(result.data!!)
                dbRepository.updateNewUser(User().apply {
                    info.id = result.data.uid
                    info.displayName = createUser.displayName
                })
            }
            if (result is Result.Success || result is Result.Error) isCreatingAccount.value = false
        }
    }

    /* Maneja la acción de prensa del botón para crear una cuenta. */
    /* Realiza validaciones en los datos ingresados por el usuario, como verificar la longitud del
    nombre de usuario y la contraseña, y si el formato del correo electrónico es válido.
    Llama a la función createAccount para iniciar el proceso de creación de cuenta si la validación
    es exitosa.*/
    fun createAccountPressed() {
        if (!isTextValid(2, displayNameText.value)) {
            mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        /* Inicia el proceso de creación de cuenta utilizando el repositorio de autenticación
        (AuthRepository) y el repositorio de base de datos (DatabaseRepository). */
        createAccount()
    }
}