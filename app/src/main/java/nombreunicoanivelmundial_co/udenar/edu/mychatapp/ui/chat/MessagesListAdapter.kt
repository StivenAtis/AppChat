package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.Message
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.ListItemMessageReceivedBinding
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.ListItemMessageSentBinding

/* Clase donde se Adapta una lista de mensajes para ser mostrada en el RecyclerView, diferenciando
entre mensajes enviados y recibidos. */

class MessagesListAdapter internal constructor(private val viewModel: ChatViewModel, private val userId: String) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2

    class ReceivedViewHolder(private val binding: ListItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatViewModel, item: Message) {
            binding.viewmodel = viewModel
            binding.message = item
            binding.executePendingBindings()
        }
    }

    class SentViewHolder(private val binding: ListItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatViewModel, item: Message) {
            binding.viewmodel = viewModel
            binding.message = item
            binding.executePendingBindings()
        }
    }

    /* determina si un mensaje es enviado o recibido y devuelve el tipo correspondiente. */
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderID != userId) {
            holderTypeMessageReceived
        } else {
            holderTypeMessageSent
        }
    }

    /* utiliza el tipo de ViewHolder para enlazar los datos del mensaje con la vista. */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            holderTypeMessageSent -> (holder as SentViewHolder).bind(
                viewModel,
                getItem(position)
            )
            holderTypeMessageReceived -> (holder as ReceivedViewHolder).bind(
                viewModel,
                getItem(position)
            )
        }
    }

    /* infla la vista adecuada según el tipo de mensaje y crea el ViewHolder correspondiente. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            holderTypeMessageSent -> {
                val binding = ListItemMessageSentBinding.inflate(layoutInflater, parent, false)
                SentViewHolder(binding)
            }
            holderTypeMessageReceived -> {
                val binding = ListItemMessageReceivedBinding.inflate(layoutInflater, parent, false)
                ReceivedViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }
}


/* Es una clase proporcionada por Android para ayudar en la actualización eficiente de listas en
componentes como RecyclerView. */
class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    /* Determina si los elementos antiguos y nuevos son los mismos en términos de referencia. */
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    /* Determina si los contenidos de los elementos antiguos y nuevos son los mismos. */
    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.epochTimeMs == newItem.epochTimeMs
    }
}
