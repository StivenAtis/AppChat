package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.users

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.User

/** Esta función de extensión bindUsersList se utiliza como un Binding Adapter para asociar
 * dinámicamente una lista de usuarios a un RecyclerView. **/

@BindingAdapter("bind_users_list")
fun bindUsersList(listView: RecyclerView, items: List<User>?) {
    items?.let { (listView.adapter as UsersListAdapter).submitList(items) }
}

