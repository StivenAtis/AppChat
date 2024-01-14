package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote

import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.CreateUser
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.Login
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/* actúa como una fuente para gestionar la autenticación a través de Firebase en la aplicación */

/* Observa el estado de autenticación de Firebase. */
class FirebaseAuthStateObserver {

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var instance: FirebaseAuth? = null

    fun start(valueEventListener: FirebaseAuth.AuthStateListener, instance: FirebaseAuth) {
        this.authListener = valueEventListener
        this.instance = instance
        this.instance!!.addAuthStateListener(authListener!!)
    }

    fun clear() {
        authListener?.let { instance?.removeAuthStateListener(it) }
    }
}

/* Proporciona métodos para interactuar con las funciones de autenticación de Firebase. */
class FirebaseAuthSource {

    companion object {
        val authInstance = FirebaseAuth.getInstance()
    }

    /*  Crea y devuelve un AuthStateListener que verifica si hay un usuario autenticado y llama a
    la función proporcionada (b) con el resultado correspondiente. */
    private fun attachAuthObserver(b: ((Result<FirebaseUser>) -> Unit)): FirebaseAuth.AuthStateListener {
        return FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                b.invoke(Result.Error("No user"))
            } else {
                b.invoke(Result.Success(it.currentUser))
            }
        }
    }

    /* Inicia sesión con las credenciales proporcionadas a través de un objeto Login. */
    fun loginWithEmailAndPassword(login: Login): Task<AuthResult> {
        return authInstance.signInWithEmailAndPassword(login.email, login.password)
    }

    /* Crea un nuevo usuario con las credenciales proporcionadas a través de un objeto CreateUser. */
    fun createUser(createUser: CreateUser): Task<AuthResult> {
        return authInstance.createUserWithEmailAndPassword(createUser.email, createUser.password)
    }

    /* Cierra la sesión del usuario actual. */
    fun logout() {
        authInstance.signOut()
    }

    /* Inicia la observación del estado de autenticación a través de un FirebaseAuthStateObserver. */
    fun attachAuthStateObserver(
        firebaseAuthStateObserver: FirebaseAuthStateObserver,
        b: ((Result<FirebaseUser>) -> Unit)
    ) {
        val listener = attachAuthObserver(b)
        firebaseAuthStateObserver.start(listener, authInstance)
    }
}

