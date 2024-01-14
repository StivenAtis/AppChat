package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.ChatWithUserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.ListItemChatBinding

/* Se utiliza para adaptar los datos de la lista de ChatWithUserInfo al diseño de la interfaz de
usuario en el RecyclerView. */
class ChatsListAdapter internal constructor(private val viewModel: ChatsViewModel) :
    ListAdapter<(ChatWithUserInfo), ChatsListAdapter.ViewHolder>(ChatDiffCallback()) {

        /* Se representa cada elemento individual en el RecyclerView. */
        class ViewHolder(private val binding: ListItemChatBinding) :
            RecyclerView.ViewHolder(binding.root) {
                /* Se utiliza para realizar el enlace de datos entre el objeto ChatWithUserInfo y
                la vista correspondiente en el RecyclerView. */
                fun bind(viewModel: ChatsViewModel, item: ChatWithUserInfo) {
                    binding.viewmodel = viewModel
                    binding.chatwithuserinfo = item
                    binding.executePendingBindings()
                }
        }

    /* Se llama para vincular los datos de un elemento específico en la posición dada en el
    RecyclerView. */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    /* Se llama cuando se necesita crear un nuevo ViewHolder para representar un elemento en el
    RecyclerView. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemChatBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

/* Se utiliza para comparar dos objetos ChatWithUserInfo y determinar si representan el mismo
elemento y si sus contenidos son iguales. */
class ChatDiffCallback : DiffUtil.ItemCallback<ChatWithUserInfo>() {
    /* Compara si los dos objetos son el mismo objeto en memoria. */
    override fun areItemsTheSame(oldItem: ChatWithUserInfo, itemWithUserInfo: ChatWithUserInfo): Boolean {
        return oldItem == itemWithUserInfo
    }

    /* Compara si los contenidos de los dos objetos son iguales. En este caso, compara los IDs de
    los chats dentro de los objetos ChatWithUserInfo. */
    override fun areContentsTheSame(oldItem: ChatWithUserInfo, itemWithUserInfo: ChatWithUserInfo): Boolean {
        return oldItem.mChat.info.id == itemWithUserInfo.mChat.info.id
    }
}