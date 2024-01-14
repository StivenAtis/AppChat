package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Event

/** gestiona la lógica y eventos relacionados con la pantalla de inicio (StartFragment). **/

class StartViewModel : ViewModel() {

    /* Representa el evento de navegar a la pantalla de inicio de sesión. */
    private val _loginEvent = MutableLiveData<Event<Unit>>()

    /* Representa el evento de navegar a la pantalla de creación de cuenta. */
    private val _createAccountEvent = MutableLiveData<Event<Unit>>()

    val loginEvent: LiveData<Event<Unit>> = _loginEvent
    val createAccountEvent: LiveData<Event<Unit>> = _createAccountEvent

    /* Son funciones públicas que se llaman cuando el usuario presiona los botones relacionados con
    la navegación hacia las pantallas de inicio de sesión o creación de cuenta.

    Cada función actualiza el valor de la variable correspondiente _loginEvent o _createAccountEvent
    con un evento que contiene un valor de tipo Unit (indicando simplemente que se ha producido un evento).
     */
    fun goToLoginPressed() {
        _loginEvent.value = Event(Unit)
    }

    fun goToCreateAccountPressed() {
        _createAccountEvent.value = Event(Unit)
    }
}


