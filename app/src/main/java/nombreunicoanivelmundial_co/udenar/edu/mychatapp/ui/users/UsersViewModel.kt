package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.users

import androidx.lifecycle.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.User
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository.DatabaseRepository
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.DefaultViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Event
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result

/* crear instancias del UsersViewModel y proporcionar el ID del usuario actual (myUserID) al ViewModel. */
class UsersViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(myUserID) as T
    }
}

/* Contiene un objeto de repositorio (DatabaseRepository) para manejar las operaciones de base de
datos relacionadas con los usuarios. */
class UsersViewModel(private val myUserID: String) : DefaultViewModel() {
    private val repository: DatabaseRepository = DatabaseRepository()

    private val _selectedUser = MutableLiveData<Event<User>>()
    var selectedUser: LiveData<Event<User>> = _selectedUser
    private val updatedUsersList = MutableLiveData<MutableList<User>>()
    val usersList = MediatorLiveData<List<User>>()

    init {
        usersList.addSource(updatedUsersList) { mutableList ->
            usersList.value = updatedUsersList.value?.filter { it.info.id != myUserID }
        }
        loadUsers()
    }

    /* Utiliza el repositorio para cargar la lista de usuarios desde la base de datos.
    Cuando la operación se completa, se utiliza la función onResult para manejar el resultado y
    actualizar updatedUsersList. */
    private fun loadUsers() {
        repository.loadUsers { result: Result<MutableList<User>> ->
            onResult(updatedUsersList, result)
        }
    }

    /* Se llama cuando se selecciona un usuario en la interfaz de usuario. */
    fun selectUser(user: User) {
        _selectedUser.value = Event(user)
    }
}