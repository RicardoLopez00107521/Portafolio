package com.avanzo.avanzotestsimulator.ui.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication

import com.avanzo.avanzotestsimulator.MainActivity
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.databinding.FragmentLoginBinding
import com.avanzo.avanzotestsimulator.ui.login.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var loader: ConstraintLayout
    private lateinit var container: ConstraintLayout
    private lateinit var passwordEditText: EditText
    private lateinit var showPasswordCheckBox: CheckBox

    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginViewModel.Factory
    }

    private lateinit var binding: FragmentLoginBinding

    val app by lazy {
        requireActivity().application as AvanzoReviewerApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        loader = view.findViewById(R.id.loader)
        container = view.findViewById(R.id.container)

        setViewModel()
        observeStatus()

        binding.createAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        passwordEditText = view.findViewById(R.id.password)
        showPasswordCheckBox = view.findViewById(R.id.showPasswordCheckBox)

        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!showPasswordCheckBox.isChecked) {
                    passwordEditText.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordEditText.setSelection(passwordEditText.text.length)
                }
            }
        })

    }

    private fun setViewModel() {
        binding.viewmodel = loginViewModel
    }

    private fun observeStatus() {
        loginViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: LoginUiStatus) {
        when (status) {
            is LoginUiStatus.Error -> {
                Toast.makeText(requireContext(), "Credenciales inválidas", Toast.LENGTH_SHORT).show()
            }
            is LoginUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            is LoginUiStatus.Success -> {
                loginViewModel.clearStatus()
                loginViewModel.clearData()
                lifecycleScope.launch {
                    app.saveAuthToken(status.token)
                }
                container.visibility = View.GONE
                loader.visibility = View.VISIBLE
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            else -> {}
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
        builder.setTitle("Cerrar aplicación")
        builder.setMessage("¿Estás seguro que quieres cerrar la aplicación?")
        builder.setPositiveButton("Sí") { _, _ ->
            requireActivity().finishAffinity()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.main))
        }
        dialog.show()
        val window = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.white)
        window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.alert_dialog_width),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.decorView?.setBackgroundResource(R.drawable.dialog_background)
    }

}

