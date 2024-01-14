package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.FragmentLoginBinding
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.EventObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.showSnackBar
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.main.MainActivity
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.SharedPreferencesUtil
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.forceHideKeyboard

/** se utiliza para manejar la interfaz de usuario y la lógica asociada con el inicio de sesión de
 * un usuario en la aplicación. **/

class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var viewDataBinding: FragmentLoginBinding

    /* Infla y devuelve la vista asociada con el fragmento. */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentLoginBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    /* Configura los observadores y realiza otras acciones después de que la actividad ha sido
    creada. */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    /* Maneja los elementos del menú en la barra de aplicaciones. En este caso, permite navegar
    hacia atrás cuando se selecciona el ícono de flecha de retroceso en la barra de herramientas. */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        viewModel.isLoggedInEvent.observe(viewLifecycleOwner, EventObserver {
            SharedPreferencesUtil.saveUserID(requireContext(), it.uid)
            navigateToChats()
        })
    }

    /* Navega a la pantalla de chats después de un inicio de sesión exitoso. */
    private fun navigateToChats() {
        findNavController().navigate(R.id.action_loginFragment_to_navigation_chats)
    }
}