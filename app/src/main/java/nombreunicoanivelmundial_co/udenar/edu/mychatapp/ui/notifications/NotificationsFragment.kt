package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.App
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.FragmentNotificationsBinding

/** este fragmento está diseñado para mostrar una lista de notificaciones en un RecyclerView. **/

class NotificationsFragment : Fragment() {

    private val viewModel: NotificationsViewModel by viewModels { NotificationsViewModelFactory(App.myUserID) }
    private lateinit var viewDataBinding: FragmentNotificationsBinding
    private lateinit var listAdapter: NotificationsListAdapter

    /* Infla y devuelve la vista utilizando el FragmentNotificationsBinding. También establece el
    ciclo de vida del propietario de la vista (viewLifecycleOwner) para permitir el enlace de datos. */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    /* Se invoca cuando la actividad asociada al fragmento ha sido creada. */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
    }

    /* nicializa el adaptador (NotificationsListAdapter) utilizando el viewModel.
    Configura el adaptador en el RecyclerView dentro de la vista (viewDataBinding.usersRecyclerView). */
    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = NotificationsListAdapter(viewModel)
            viewDataBinding.usersRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }
}