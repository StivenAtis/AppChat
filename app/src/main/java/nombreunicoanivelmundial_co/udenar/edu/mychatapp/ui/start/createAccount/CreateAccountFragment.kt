package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.start.createAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.EventObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.FragmentCreateAccountBinding
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.main.MainActivity
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.SharedPreferencesUtil
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.forceHideKeyboard
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.showSnackBar

/** Fragmento de la aplicación que está diseñado para crear una cuenta de usuario. **/

class CreateAccountFragment : Fragment() {

    private val viewModel by viewModels<CreateAccountViewModel>()
    private lateinit var viewDataBinding: FragmentCreateAccountBinding

    /* Infla y configura la vista del fragmento utilizando Data Binding. También establece el
    ciclo de vida del propietario de la vista y activa la opción del menú de la barra de herramientas. */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentCreateAccountBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    /* Maneja la selección de elementos del menú de la barra de herramientas. En este caso, si se
    selecciona el ítem de inicio (android.R.id.home), se regresa al fragmento anterior en la pila
    de retroceso. */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /* Configura los observadores para eventos específicos del ViewModel. */
    private fun setupObservers() {
        /* Observa cambios en el indicador de carga de datos y muestra/oculta la barra de
        progreso global en la actividad principal. */
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        /* Observa mensajes de barra de snack y muestra la barra de snack en la vista del fragmento. */
        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        /* Observa el evento de creación de cuenta y guarda el ID del usuario en las preferencias
        compartidas antes de navegar hacia la pantalla de chats. */
        viewModel.isCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            SharedPreferencesUtil.saveUserID(requireContext(), it.uid)
            navigateToChats()
        })
    }

    /* Navega desde este fragmento al fragmento de chats después de haber creado con éxito la cuenta. */
    private fun navigateToChats() {
        findNavController().navigate(R.id.action_createAccountFragment_to_navigation_chats)
    }
}