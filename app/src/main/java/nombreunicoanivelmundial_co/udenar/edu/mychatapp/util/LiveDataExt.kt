package nombreunicoanivelmundial_co.udenar.edu.mychatapp.util

import androidx.lifecycle.MutableLiveData

/**  extensiones de funciones para la clase MutableLiveData, diseñadas para manipular listas
 * contenidas dentro de MutableLiveData. **/

/* Crea una nueva lista mutable (newList) basada en el valor actual de MutableLiveData.
Agrega el nuevo elemento (item) a la lista mutable.
Actualiza el valor de MutableLiveData con la nueva lista. */
fun <T> MutableLiveData<MutableList<T>>.addNewItem(item: T) {
    val newList = mutableListOf<T>()
    this.value?.let { newList.addAll(it) }
    newList.add(item)
    this.value = newList
}

/* Crea una nueva lista mutable (newList) basada en el valor actual de MutableLiveData.
Actualiza el elemento en la posición especificada (index) con el nuevo valor (item). */
fun <T> MutableLiveData<MutableList<T>>.updateItemAt(item: T, index: Int) {
    val newList = mutableListOf<T>()
    this.value?.let { newList.addAll(it) }
    newList[index] = item
    this.value = newList
}

/* Crea una nueva lista mutable (newList) basada en el valor actual de MutableLiveData.
Elimina el elemento especificado (item) de la lista mutable.
Actualiza el valor de MutableLiveData con la nueva lista.*/
fun <T> MutableLiveData<MutableList<T>>.removeItem(item: T) {
    val newList = mutableListOf<T>()
    this.value?.let { newList.addAll(it) }
    newList.remove(item)
    this.value = newList
}
