@file:Suppress("unused")

package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.chats

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.ChatWithUserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.db.entity.Message

/* Actualiza dinámicamente la lista de chats en el RecyclerView. */
@BindingAdapter("bind_chats_list")
fun bindChatsList(listView: RecyclerView, items: List<ChatWithUserInfo>?) {
    items?.let { (listView.adapter as ChatsListAdapter).submitList(items) }
}

@BindingAdapter("bind_chat_message_text", "bind_chat_message_text_viewModel")
/* Vincula el texto de un mensaje en un TextView, prefijando "You:" si el mensaje fue enviado por
el usuario actual. */
fun TextView.bindMessageYouToText(message: Message, viewModel: ChatsViewModel) {
    this.text = if (message.senderID == viewModel.myUserID) {
        "You: " + message.text
    } else {
        message.text
    }
}

@BindingAdapter("bind_message_view", "bind_message_textView", "bind_message", "bind_myUserID")
/* Controla la visibilidad y apariencia del elemento de la interfaz de usuario que indica que un
* mensaje ha sido visto */
fun View.bindMessageSeen(view: View, textView: TextView, message: Message, myUserID: String) {
    if (message.senderID != myUserID && !message.seen) {
        view.visibility = View.VISIBLE
        textView.setTextAppearance(R.style.MessageNotSeen)
    } else {
        view.visibility = View.INVISIBLE
        textView.setTextAppearance(R.style.MessageSeen)
    }
}

