package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.User
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.ListItemUserBinding

/* Extiende de ListAdapter<User, UsersListAdapter.ViewHolder>, que es una clase proporcionada por
Android para ayudar en la actualización eficiente de listas en componentes como RecyclerView. */
class UsersListAdapter internal constructor(private val viewModel: UsersViewModel) :
    ListAdapter<User, UsersListAdapter.ViewHolder>(UserDiffCallback()) {

    class ViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /* Se enlazan los datos del usuario (item) con la vista utilizando Data Binding. */
        fun bind(viewModel: UsersViewModel, item: User) {
            binding.viewmodel = viewModel
            binding.user = item
            /* asegurar que todas las vistas se actualicen de inmediato. */
            binding.executePendingBindings()
        }
    }

    /* Se llama cuando se necesita enlazar un conjunto de datos a una vista en una posición
    específica.
    Llama al método bind del ViewHolder para enlazar los datos del usuario con la vista. */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    /* Se llama cuando se crea una nueva instancia de ViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemUserBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

/* Implementa dos métodos (areItemsTheSame y areContentsTheSame) para determinar si los elementos
son los mismos y si los contenidos de los elementos son iguales, respectivamente. */
class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.info.id == newItem.info.id
    }
}