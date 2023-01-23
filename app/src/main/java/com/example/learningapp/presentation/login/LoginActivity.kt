package com.example.learningapp.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learningapp.databinding.ActivityLoginBinding
import com.example.learningapp.presentation.forgotpassword.ForgotPasswordActivity
import com.example.learningapp.presentation.main.MainActivity
import com.example.learningapp.presentation.register.RegisterActivity
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()

    }
    private fun onAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                startActivity<MainActivity>()
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPassLogin.setOnClickListener {
                startActivity<ForgotPasswordActivity>()
            }
        }
    }
}