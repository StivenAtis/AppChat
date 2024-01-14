package nombreunicoanivelmundial_co.udenar.edu.mychatapp.util

import com.google.firebase.database.DataSnapshot

/** Funciones destinadas a trabajar con datos de Firebase. **/

/* Utiliza la función getValue del DataSnapshot para mapear el contenido del DataSnapshot a una
instancia de la clase especificada (className). */
fun <T> wrapSnapshotToClass(className: Class<T>, snap: DataSnapshot): T? {
    return snap.getValue(className)
}

/* Itera sobre los hijos del DataSnapshot y utiliza la función getValue para mapear cada elemento a
una instancia de la clase especificada (className). Los elementos mapeados se agregan a una lista. */
fun <T> wrapSnapshotToArrayList(className: Class<T>, snap: DataSnapshot): MutableList<T> {
    val arrayList: MutableList<T> = arrayListOf()
    for (child in snap.children) {
        child.getValue(className)?.let { arrayList.add(it) }
    }

    /* Retorna una lista mutable que contiene instancias de la clase especificada */
    return arrayList
}

/* Combina los dos identificadores de usuario en una única cadena o sea, que, siempre devuelve el
mismo identificador combinado al comparar los identificadores de los dos usuarios */
fun convertTwoUserIDs(userID1: String, userID2: String): String {
    return if (userID1 < userID2) {
        userID2 + userID1
    } else {
        userID1 + userID2
    }
}