package com.example.learningapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.example.learningapp.R
import com.example.learningapp.adapter.MaterialsAdapter
import com.example.learningapp.databinding.ActivityMainBinding
import com.example.learningapp.presentation.content.ContentActivity
import com.example.learningapp.presentation.user.UserActivity
import com.example.learningapp.repository.Repository
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var materialsAdapter: MaterialsAdapter
    companion object{
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        materialsAdapter = MaterialsAdapter()

        getDataMaterial()
        onAction()

    }

    private fun getDataMaterial() {
        showLoading()
        val materials = Repository.getMaterials(this)

        Handler(Looper.getMainLooper())
            .postDelayed({
                hideLoading()
                materials?.let {
                    materialsAdapter.materials = it
                }
            }, 1200)

        binding.rvMaterialsMain.adapter = materialsAdapter
    }

    private fun showLoading() {
        binding.swipeMain.isRefreshing = true
    }

    private fun hideLoading() {
        binding.swipeMain.isRefreshing = false
    }

    private fun onAction() {
        binding.apply {
            ivAvatarMain.setOnClickListener {
                startActivity<UserActivity>()
            }

            etSearchMain.addTextChangedListener {
                materialsAdapter.filter.filter(it.toString())
            }

            etSearchMain.setOnEditorActionListener{_, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    val dataSearch = etSearchMain.text.toString().trim()
                    materialsAdapter.filter.filter(dataSearch)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            swipeMain.setOnRefreshListener {
                getDataMaterial()
            }
        }
        materialsAdapter.onClick { material, position ->
            startActivity<ContentActivity>(
                ContentActivity.EXTRA_MATERIAL to material,
                ContentActivity.EXTRA_POSITION to position
            )
        }
    }
}