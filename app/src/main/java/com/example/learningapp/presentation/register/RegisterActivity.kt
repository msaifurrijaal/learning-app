package com.example.learningapp.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learningapp.databinding.ActivityRegisterBinding
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()

    }
    private fun onAction() {
        binding.apply {
            btnCloseRegister.setOnClickListener {
                finish()
            }

            btnRegister.setOnClickListener {
                toast("Register Success")
            }
        }
    }
}