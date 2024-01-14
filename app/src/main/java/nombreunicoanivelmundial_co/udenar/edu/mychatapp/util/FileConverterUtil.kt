package nombreunicoanivelmundial_co.udenar.edu.mychatapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.InputStream

/** Esta función, convierte un archivo de imagen representado por su Uri a un arreglo de bytes (ByteArray). **/

fun convertFileToByteArray(context: Context, uri: Uri): ByteArray {

    /* Abre un flujo de entrada (InputStream) desde la Uri utilizando el contentResolver del contexto. */
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)

    /* Convierte el flujo de entrada en un objeto Bitmap utilizando BitmapFactory.decodeStream. */
    val bitmap = BitmapFactory.decodeStream(inputStream)

    /* Crea un ByteArrayOutputStream para almacenar los bytes de la imagen. */
    val byteArrayOutputStream = ByteArrayOutputStream()

    /* Comprime el Bitmap en formato JPEG con calidad al 100%, escribiendo los bytes comprimidos en
    el ByteArrayOutputStream. */
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

    /* Retorna el arreglo de bytes resultante del proceso de compresión. */
    return byteArrayOutputStream.toByteArray()
}
