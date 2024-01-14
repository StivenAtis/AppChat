package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.remote

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

/* Proporciona métodos para cargar imágenes de usuario en Firebase Storage. */
class FirebaseStorageSource {
    private val storageInstance = FirebaseStorage.getInstance()

    /* Carga la imagen de usuario representada por un array de bytes (bArr) en Firebase Storage.
    La imagen se almacena en la ruta "user_photos/$userID/profile_image".
    La función devuelve un Task que, cuando se complete, proporciona la URL de descarga (Uri) de la
    imagen cargada. */
    fun uploadUserImage(userID: String, bArr: ByteArray): Task<Uri> {
        val path = "user_photos/$userID/profile_image"
        val ref = storageInstance.reference.child(path)

        return ref.putBytes(bArr).continueWithTask {
            ref.downloadUrl
        }
    }
}