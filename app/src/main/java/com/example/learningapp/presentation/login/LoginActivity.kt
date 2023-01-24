package com.example.learningapp.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.example.learningapp.R
import com.example.learningapp.databinding.ActivityLoginBinding
import com.example.learningapp.presentation.forgotpassword.ForgotPasswordActivity
import com.example.learningapp.presentation.main.MainActivity
import com.example.learningapp.presentation.register.RegisterActivity
import com.example.learningapp.utils.hideSoftKeyboard
import com.example.learningapp.utils.showDialogError
import com.example.learningapp.utils.showDialogLoading
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init
        firebaseAuth = FirebaseAuth.getInstance()
        dialogLoading = showDialogLoading(this)

        onAction()

    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            startActivity<MainActivity>()
            finishAffinity()
        }
    }

    private fun onAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val pass = etPasswordLogin.text.toString().trim()

                if (checkValidation(email, pass)){
                    hideSoftKeyboard(this@LoginActivity, binding.root)
                    loginToServer(email, pass)
                }
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPassLogin.setOnClickListener {
                startActivity<ForgotPasswordActivity>()
            }
        }
    }

    private fun loginToServer(email: String, pass: String) {
        dialogLoading.show()
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                dialogLoading.dismiss()
                startActivity<MainActivity>()
                finishAffinity()
            }
            .addOnFailureListener {
                dialogLoading.dismiss()
                showDialogError(this, it.message.toString())
            }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        binding.apply {
            when{
                email.isEmpty() -> {
                    etEmailLogin.error = getString(R.string.please_field_your_email)
                    etEmailLogin.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmailLogin.error = getString(R.string.please_use_valid_email)
                    etEmailLogin.requestFocus()
                }
                pass.isEmpty() -> {
                    etPasswordLogin.error = getString(R.string.please_field_your_password)
                    etPasswordLogin.requestFocus()
                }
                pass.length < 8 -> {
                    etPasswordLogin.error = getString(R.string.please_field_your_password_more_than_8)
                    etPasswordLogin.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}