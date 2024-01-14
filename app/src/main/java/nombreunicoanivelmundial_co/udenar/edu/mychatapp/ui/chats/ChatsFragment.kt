package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.App
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.model.ChatWithUserInfo
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.FragmentChatsBinding
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.EventObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.chat.ChatFragment
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.convertTwoUserIDs

class ChatsFragment : Fragment() {

    private val viewModel: ChatsViewModel by viewModels { ChatsViewModelFactory(App.myUserID) }
    private lateinit var viewDataBinding: FragmentChatsBinding
    private lateinit var listAdapter: ChatsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        /* El objeto viewDataBinding se inicializa inflando el diseño FragmentChatsBinding.
        Este objeto se utiliza para realizar enlace de datos y acceder a las vistas del fragmento. */
        viewDataBinding =
            FragmentChatsBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupObservers()
    }

    /* se crea un adaptador de lista (ChatsListAdapter) asociado al ViewModel y se asigna al
    RecyclerView en la interfaz de usuario. */
    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = ChatsListAdapter(viewModel)
            viewDataBinding.chatsRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    /* Se establece un observador en la propiedad selectedChat del ViewModel utilizando EventObserver.
    Este observador se dispara cuando se selecciona un chat en la interfaz de usuario. */
    private fun setupObservers() {
        viewModel.selectedChat.observe(viewLifecycleOwner,
            EventObserver { navigateToChat(it) })
    }

    /* Este método se llama cuando se selecciona un chat.
    Este método crea un Bundle (Un paquete nuevo y vacío.) con información necesaria para iniciar
    la pantalla de chat, como los IDs de usuario y el ID del chat. Luego, utiliza
    findNavController() para navegar a la pantalla de chat (ChatFragment). */
    private fun navigateToChat(chatWithUserInfo: ChatWithUserInfo) {
        val bundle = bundleOf(
            ChatFragment.ARGS_KEY_USER_ID to App.myUserID,
            ChatFragment.ARGS_KEY_OTHER_USER_ID to chatWithUserInfo.mUserInfo.id,
            ChatFragment.ARGS_KEY_CHAT_ID to convertTwoUserIDs(App.myUserID, chatWithUserInfo.mUserInfo.id)
        )
        findNavController().navigate(R.id.action_navigation_chats_to_chatFragment, bundle)
    }
}