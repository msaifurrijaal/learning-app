package com.example.learningapp.presentation.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import com.example.learningapp.R
import com.example.learningapp.databinding.ActivityUserBinding
import com.example.learningapp.presentation.changepassword.ChangePasswordActivity
import com.example.learningapp.presentation.login.LoginActivity
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()

    }
    private fun onAction() {
        binding.apply {
            btnCloseUser.setOnClickListener { finish() }

            btnChangeLanguageUser.setOnClickListener {
                startActivity(Intent(ACTION_LOCALE_SETTINGS))
            }

            btnChangePasswordUser.setOnClickListener {
                startActivity<ChangePasswordActivity>()
            }

            btnLogoutUser.setOnClickListener {
                startActivity<LoginActivity>()
                finishAffinity()
            }
        }
    }
}