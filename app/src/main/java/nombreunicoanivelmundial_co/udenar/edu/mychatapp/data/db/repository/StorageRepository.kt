package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.repository

import android.net.Uri
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote.FirebaseStorageSource
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.Result

/* Proporciona métodos para cargar y actualizar imágenes de perfil de usuarios en Firebase Storage. */
class StorageRepository {
    private val firebaseStorageService = FirebaseStorageSource()

    /* Esta función toma un userID como identificación de usuario, un arreglo de bytes (byteArray)
    que representa la imagen, y una lambda b para manejar el resultado (Result<Uri>). */
    fun updateUserProfileImage(userID: String, byteArray: ByteArray, b: (Result<Uri>) -> Unit) {
        /* Invoca la lambda b con Result.Loading para indicar que la operación está en curso. */
        b.invoke(Result.Loading)
        /* Llama al método uploadUserImage de FirebaseStorageSource para cargar la
        imagen en Firebase Storage. */
        firebaseStorageService.uploadUserImage(userID, byteArray).addOnSuccessListener {
            /* Si la carga es exitosa, invoca la lambda b con Result.Success y la URL (Uri) de la
            imagen cargada. */
            b.invoke((Result.Success(it)))
        }.addOnFailureListener {
            /* Si hay un fallo, invoca la lambda b con Result.Error y un mensaje de error. */
            b.invoke(Result.Error(it.message))
        }
    }
}