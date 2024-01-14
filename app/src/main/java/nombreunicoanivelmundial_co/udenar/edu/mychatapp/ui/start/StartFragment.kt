package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.FragmentStartBinding
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.EventObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.SharedPreferencesUtil

/** fragmento llamado que sirve como la pantalla de inicio de la aplicación. **/

class StartFragment : Fragment() {

    private val viewModel by viewModels<StartViewModel>()
    private lateinit var viewDataBinding: FragmentStartBinding

    /* Infla y devuelve la vista asociada al fragmento utilizando el FragmentStartBinding. */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentStartBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(false)
        return viewDataBinding.root
    }

    /* Se llama cuando la actividad ha sido creada.
    Configura los observadores y realiza la navegación directa a la pantalla de chats si el usuario
    ya ha iniciado sesión. */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()

        if (userIsAlreadyLoggedIn()) {
            navigateDirectlyToChats()
        }
    }

    /* Verifica si el usuario ya ha iniciado sesión mediante la verificación de la existencia del
    ID de usuario en las preferencias compartidas (SharedPreferencesUtil). */
    private fun userIsAlreadyLoggedIn(): Boolean {
        return SharedPreferencesUtil.getUserID(requireContext()) != null
    }

    /* Observa los eventos emitidos por el ViewModel utilizando el mecanismo de LiveData con
    EventObserver.
    Los eventos observados son el intento de inicio de sesión (loginEvent) y el intento de creación
    de cuenta (createAccountEvent). */
    private fun setupObservers() {
        viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver { navigateToLogin() })
        viewModel.createAccountEvent.observe(
            viewLifecycleOwner, EventObserver { navigateToCreateAccount() })
    }

    /* Navega directamente a la pantalla de chats si el usuario ya ha iniciado sesión. */
    private fun navigateDirectlyToChats() {
        findNavController().navigate(R.id.action_startFragment_to_navigation_chats)
    }

    /* Navegan a las pantallas de inicio de sesión y creación de cuenta, respectivamente.
    Utilizan la clase NavController para realizar la navegación.
     */
    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_startFragment_to_loginFragment)
    }

    private fun navigateToCreateAccount() {
        findNavController().navigate(R.id.action_startFragment_to_createAccountFragment)
    }
}