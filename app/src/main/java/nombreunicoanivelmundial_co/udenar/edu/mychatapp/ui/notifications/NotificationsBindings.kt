package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.notifications

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserInfo

/* Este BindingAdapter se usa en los archivos de diseño XML para realizar la conexión entre el
RecyclerView y la lista de notificaciones (UserInfo). Al establecer el atributo
bind_notifications_list en el archivo XML de diseño, se invoca este BindingAdapter y se realiza la
actualización de la lista en el RecyclerView correspondiente. */

@BindingAdapter("bind_notifications_list")
fun bindNotificationsList(listView: RecyclerView, items: List<UserInfo>?) {
    items?.let { (listView.adapter as NotificationsListAdapter).submitList(items) }
}
