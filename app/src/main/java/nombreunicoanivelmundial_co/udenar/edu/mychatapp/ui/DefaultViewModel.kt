package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Event
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result

/** Clase abstracta llamada que extiende ViewModel.
 * Esta clase sirve como una base común para otros ViewModels en la aplicación y proporciona
 * funcionalidades básicas relacionadas con la carga de datos y la manipulación de eventos. */

abstract class DefaultViewModel : ViewModel() {
    protected val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = mSnackBarText

    private val mDataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>> = mDataLoading

    /* Es una función protegida que maneja los resultados de las operaciones asincrónicas
    (generalmente cargas de datos) y actualiza las LiveData correspondientes. */
    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, result: Result<T>) {
        when (result) {

            /* Cuando se recibe un resultado de tipo Result.Loading, se activa la señal de carga. */
            is Result.Loading -> mDataLoading.value = Event(true)

            /* En caso de un resultado de tipo Result.Error, se desactiva la señal de carga y se
            muestra un mensaje de error en el Snackbar. */
            is Result.Error -> {
                mDataLoading.value = Event(false)
                result.msg?.let { mSnackBarText.value = Event(it) }
            }

            /* Si el resultado es de tipo Result.Success, se desactiva la señal de carga, y si hay
            datos, se actualiza la LiveData correspondiente. Además, si hay un mensaje, se muestra en el Snackbar. */
            is Result.Success -> {
                mDataLoading.value = Event(false)
                result.data?.let { mutableLiveData?.value = it }
                result.msg?.let { mSnackBarText.value = Event(it) }
            }
        }
    }
}