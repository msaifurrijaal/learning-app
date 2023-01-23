package com.example.learningapp.presentation.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learningapp.databinding.ActivityChangePasswordBinding
import org.jetbrains.anko.toast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()

    }
    private fun onAction() {
        binding.apply {
            btnChangePassword.setOnClickListener {
                toast("Change Password")
            }

            btnCloseChangePassword.setOnClickListener { finish() }
        }
    }
}