package com.example.learningapp.presentation.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learningapp.R
import com.example.learningapp.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}