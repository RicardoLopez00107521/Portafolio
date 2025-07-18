package com.avanzo.avanzotestsimulator.ui.home

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.avanzo.avanzotestsimulator.AvanzoReviewerApplication
import com.avanzo.avanzotestsimulator.MainActivity
import com.avanzo.avanzotestsimulator.R
import com.avanzo.avanzotestsimulator.databinding.ActivityMainBinding
import com.avanzo.avanzotestsimulator.databinding.ActivityProfileBinding
import com.avanzo.avanzotestsimulator.databinding.ActivityTestDetailsBinding
import com.avanzo.avanzotestsimulator.ui.home.viewmodel.UserViewModel
import com.avanzo.avanzotestsimulator.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val userViewModel: UserViewModel by viewModels {
        UserViewModel.Factory
    }

    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Mostrar ProgressBar y ocultar Main section
                binding.progressBar.visibility = View.VISIBLE
                binding.mainSection.visibility = View.GONE

                val points = userViewModel.getLocalUser().points.toDouble()
                val pointsAsInt = round(points).toInt()
                binding.numberPoints.text = pointsAsInt.toString()

                firstName = userViewModel.getLocalUser().firstname
                val editableText = Editable.Factory.getInstance().newEditable(firstName)
                binding.editTextName.text = editableText

                lastName = userViewModel.getLocalUser().lastname
                val editableText2 = Editable.Factory.getInstance().newEditable(lastName)
                binding.editTextLastName.text = editableText2

                email = userViewModel.getLocalUser().email
                val editableText3 = Editable.Factory.getInstance().newEditable(email)
                binding.editTextEmail.text = editableText3

                val initial = firstName?.firstOrNull()?.toString() ?: ""
                binding.userInitial.text = initial

                // Ocultar ProgressBar y mostrar Main section
                binding.progressBar.visibility = View.GONE
                binding.mainSection.visibility = View.VISIBLE

            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }

        binding.logoutButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        binding.updateButton.setOnClickListener {
            val newPassword = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextPasswordConfirmation.text.toString()

            if (newPassword.isBlank() || confirmPassword.isBlank()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else if (newPassword.length < 8) {
                Toast.makeText(this, "Caracteres insuficientes", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    userViewModel.updateUser(newPassword, confirmPassword)
                }
                Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle("Cerrar sesión")
        builder.setMessage("¿Estás seguro que quieres cerrar sesión?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Limpiamos el token y abrimos el login acitivty
            val app = application as AvanzoReviewerApplication
            lifecycleScope.launch {
                app.saveAuthToken("")
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.main))
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.main))
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