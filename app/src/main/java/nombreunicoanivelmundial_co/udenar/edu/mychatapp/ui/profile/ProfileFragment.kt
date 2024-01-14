package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.profile

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.App
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.*
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.EventObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.showSnackBar
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.main.MainActivity
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.forceHideKeyboard

/** Representa un fragmento que muestra el perfil de un usuario en la aplicación del chat. **/

/* Esta clase infla y vincula la vista utilizando el archivo de diseño FragmentProfileBinding. */
class ProfileFragment : Fragment() {

    /* Proporciona un compañero objeto (singleton) dentro de la clase ProfileFragment con una
    constante ARGS_KEY_USER_ID. La constante ARGS_KEY_USER_ID se utiliza como clave para pasar el
    ID del usuario como argumento entre fragmentos. */
    companion object {
        const val ARGS_KEY_USER_ID = "bundle_user_id"
    }

    /* tiliza el ViewModel (ProfileViewModel) para manejar la lógica de presentación y la
    interacción con la capa de datos. */
    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(App.myUserID, requireArguments().getString(ARGS_KEY_USER_ID)!!)
    }

    private lateinit var viewDataBinding: FragmentProfileBinding

    /* Establece el ciclo de vida del propietario de la vista y habilita las opciones de menú. */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentProfileBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    /* Configura observadores y realiza acciones adicionales cuando se crea la actividad asociada
    al fragmento. */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*  Configura observadores para manejar eventos específicos del ciclo de vida del fragmento y
    eventos relacionados con el perfil del usuario. */
    private fun setupObservers() {
        /* Observa cambios en la propiedad dataLoading del ProfileViewModel para mostrar u ocultar
        una barra de progreso global. */
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        /* Observa cambios en la propiedad snackBarText del ProfileViewModel para mostrar mensajes
        en una barra de pestañas y ocultar el teclado virtual. */
        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })
    }
}