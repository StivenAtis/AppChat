package nombreunicoanivelmundial_co.udenar.edu.mychatapp.ui.settings

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.App
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.R
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.databinding.FragmentSettingsBinding
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.data.EventObserver
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.SharedPreferencesUtil
import nombreunicoanivelmundial_co.udenar.edu.mychatapp.util.convertFileToByteArray

/** Representa la pantalla de configuración en la aplicación. **/

class SettingsFragment : Fragment() {

    /* Administra la lógica de negocios y la interacción de la interfaz de usuario relacionada
    con la pantalla de configuración. */
    private val viewModel: SettingsViewModel by viewModels { SettingsViewModelFactory(App.myUserID) }

    private lateinit var viewDataBinding: FragmentSettingsBinding
    private val selectImageIntentRequestCode = 1

    /* Infla y devuelve la vista asociada al fragmento (FragmentSettingsBinding). */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentSettingsBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)

        return viewDataBinding.root
    }

    /* Llama a setupObservers para configurar los observadores de eventos del ViewModel. */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    /* Maneja la selección de elementos del menú de opciones. En este caso, maneja la acción de
    "Atrás" en la barra de aplicaciones. */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /* Captura el resultado de una actividad que se inicia con startActivityForResult.
    En este caso, maneja la selección de una imagen de la galería y la envía al ViewModel para su
    procesamiento. */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == selectImageIntentRequestCode) {
            data?.data?.let { uri ->
                convertFileToByteArray(requireContext(), uri).let {
                    viewModel.changeUserImage(it)
                }
            }
        }
    }

    /* Configura observadores para los eventos del ViewModel, como la edición de estado, la edición
    de la imagen y el evento de cierre de sesión. */
    private fun setupObservers() {
        viewModel.editStatusEvent.observe(viewLifecycleOwner,
            EventObserver { showEditStatusDialog() })

        viewModel.editImageEvent.observe(viewLifecycleOwner,
            EventObserver { startSelectImageIntent() })

        viewModel.logoutEvent.observe(viewLifecycleOwner,
            EventObserver {
                SharedPreferencesUtil.removeUserID(requireContext())
                navigateToStart()
            })
    }

    /* Muestra un cuadro de diálogo que permite al usuario editar su estado. */
    private fun showEditStatusDialog() {
        val input = EditText(requireActivity() as Context)
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Status:")
            setView(input)
            setPositiveButton("Ok") { _, _ ->
                val textInput = input.text.toString()
                if (!textInput.isBlank() && textInput.length <= 40) {
                    viewModel.changeUserStatus(textInput)
                }
            }
            setNegativeButton("Cancel") { _, _ -> }
            show()
        }
    }

    /* Inicia un intent para seleccionar una imagen de la galería del dispositivo. */
    private fun startSelectImageIntent() {
        val selectImageIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectImageIntent.type = "image/*"
        startActivityForResult(selectImageIntent, selectImageIntentRequestCode)
    }

    /* Navega hacia el fragmento de inicio (StartFragment). */
    private fun navigateToStart() {
        findNavController().navigate(R.id.action_navigation_settings_to_startFragment)
    }
}