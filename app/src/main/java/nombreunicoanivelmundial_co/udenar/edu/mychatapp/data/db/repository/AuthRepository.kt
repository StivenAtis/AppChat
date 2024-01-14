package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository

import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.CreateUser
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.Login
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseAuthSource
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseAuthStateObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import com.google.firebase.auth.FirebaseUser

/* Proporciona métodos para realizar operaciones de autenticación, como iniciar sesión, crear un
nuevo usuario y observar el estado de autenticación. */
class AuthRepository{
    private val firebaseAuthService = FirebaseAuthSource()

    /* Observa el estado de autenticación. */
    fun observeAuthState(stateObserver: FirebaseAuthStateObserver, b: ((Result<FirebaseUser>) -> Unit)){
        firebaseAuthService.attachAuthStateObserver(stateObserver,b)
    }

    /* Inicia sesión de un usuario con credenciales proporcionadas. */
    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.loginWithEmailAndPassword(login).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    /* Crea un nuevo usuario con las credenciales proporcionadas. */
    fun createUser(createUser: CreateUser, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.createUser(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    /* Cierra sesión del usuario actual. */
    fun logoutUser() {
        firebaseAuthService.logout()
    }
}