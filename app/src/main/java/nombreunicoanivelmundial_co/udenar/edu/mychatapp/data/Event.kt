package nombreunicoanivelmundial_co.udenar.edu.mychatapp.data

import androidx.lifecycle.Observer

/* Representa un evento único. El propósito principal es garantizar que un evento solo sea
manejado una vez. */
open class Event<out T>(private val content: T) {
    private var isHandled = false

    fun getContentIfNotHandled(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            content
        }
    }
}

/* Su función principal es manejar el contenido del evento solo si aún no ha sido manejado. */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        event.getContentIfNotHandled()?.let { onEventUnhandledContent(it) }
    }
}
