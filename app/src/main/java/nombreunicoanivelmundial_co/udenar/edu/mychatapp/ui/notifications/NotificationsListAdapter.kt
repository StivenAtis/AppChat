package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.UserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.ListItemNotificationBinding

/* optimizar las actualizaciones del conjunto de datos. */
class NotificationsListAdapter internal constructor(private val viewModel: NotificationsViewModel) :
    ListAdapter<UserInfo, NotificationsListAdapter.ViewHolder>(UserInfoDiffCallback()) {

    /*  Contiene una referencia al enlace de datos (ListItemNotificationBinding) y tiene un método
    bind que enlaza un UserInfo y un NotificationsViewModel con las vistas. */
    class ViewHolder(private val binding: ListItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: NotificationsViewModel, item: UserInfo) {
            binding.viewmodel = viewModel
            binding.userinfo = item
            binding.executePendingBindings()
        }
    }

    /* Se llama cuando se necesita enlazar un conjunto de datos a una vista en una posición
    específica. Llama al método bind del ViewHolder para enlazar los datos del UserInfo con las vistas. */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    /* e llama cuando se necesita crear un nuevo ViewHolder. Infla la vista utilizando el enlace de
    datos (ListItemNotificationBinding) y devuelve una nueva instancia de ViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemNotificationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

/* Implementa la interfaz DiffUtil.ItemCallback<UserInfo>, que se utiliza para calcular las
diferencias entre dos conjuntos de datos y optimizar las actualizaciones del RecyclerView. */
class UserInfoDiffCallback : DiffUtil.ItemCallback<UserInfo>() {
    /* comprueba si los elementos son los mismos */
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem == newItem
    }

    /* comprueba si los contenidos de los elementos son los mismos */
    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem.id == newItem.id
    }
}