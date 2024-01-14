package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@BindingAdapter("bind_image_url_blur")

/* Carga una imagen desde una URL utilizando la biblioteca Picasso.
Aplica una transformación de desenfoque a la imagen.
Si la URL es nula o vacía, muestra un ícono de error predeterminado.*/
fun bindBlurImageWithPicasso(imageView: ImageView, url: String?) {
    if (!url.isNullOrBlank()) {
        Picasso.get().load(url).error(R.drawable.ic_baseline_error_24)
            .transform(BlurTransformation(imageView.context, 15, 1)).into(imageView)
    }
}

@BindingAdapter("bind_image_url")

/* Carga una imagen desde una URL utilizando la biblioteca Picasso.
Si la URL es nula, establece un fondo predeterminado.
Si la URL está vacía, muestra un ícono predeterminado.*/
fun bindImageWithPicasso(imageView: ImageView, url: String?) {
    when (url) {
        null -> Unit
        "" -> imageView.setBackgroundResource(R.drawable.ic_baseline_person_24)
        else -> Picasso.get().load(url).error(R.drawable.ic_baseline_error_24).into(imageView)
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("bind_epochTimeMsToDate_with_days_ago")

/* Convierte un tiempo en milisegundos (epochTimeMs) a un formato de fecha con la información de
"Ayer" o "Hace X días".
Utiliza la clase SimpleDateFormat para formatear la fecha.*/
fun TextView.bindEpochTimeMsToDateWithDaysAgo(epochTimeMs: Long) {
    val numOfDays = TimeUnit.MILLISECONDS.toDays(Date().time - epochTimeMs)

    this.text = when {
        numOfDays == 1.toLong() -> "Yesterday"
        numOfDays > 1.toLong() -> "$numOfDays days ago"
        else -> {
            val pat =
                SimpleDateFormat().toLocalizedPattern().replace("\\W?[YyMd]+\\W?".toRegex(), " ")
            val formatter = SimpleDateFormat(pat, Locale.getDefault())
            formatter.format(Date(epochTimeMs))
        }
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("bind_epochTimeMsToDate")

/* si la diferencia es mayor o igual a un día, solo muestra la fecha sin la información de "Ayer" o
"Hace X días". */
fun TextView.bindEpochTimeMsToDate(epochTimeMs: Long) {
    if (epochTimeMs > 0) {
        val currentTimeMs = Date().time
        val numOfDays = TimeUnit.MILLISECONDS.toDays(currentTimeMs - epochTimeMs)

        val replacePattern = when {
            numOfDays >= 1.toLong() -> "Yy"
            else -> "YyMd"
        }
        val pat = SimpleDateFormat().toLocalizedPattern()
            .replace("\\W?[$replacePattern]+\\W?".toRegex(), " ")
        val formatter = SimpleDateFormat(pat, Locale.getDefault())
        this.text = formatter.format(Date(epochTimeMs))
    }
}

@BindingAdapter("bind_disable_item_animator")

/* Se utiliza para desactivar animaciones en el RecyclerView, */
fun bindDisableRecyclerViewItemAnimator(recyclerView: RecyclerView, disable: Boolean) {
    if (disable) {
        recyclerView.itemAnimator = null
    }
}
