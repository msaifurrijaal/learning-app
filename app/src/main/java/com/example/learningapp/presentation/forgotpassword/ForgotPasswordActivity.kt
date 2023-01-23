package com.example.learningapp.presentation.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learningapp.R
import com.example.learningapp.databinding.ActivityForgotPasswordBinding
import org.jetbrains.anko.toast

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()
    }
    private fun onAction() {
        binding.apply {
            btnCloseForgotPassword.setOnClickListener { finish() }

            btnForgotPassword.setOnClickListener {
                toast("Forgot Password")
            }
        }
    }
}