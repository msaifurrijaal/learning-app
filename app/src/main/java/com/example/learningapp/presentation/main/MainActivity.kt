package com.example.learningapp.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.learningapp.R
import com.example.learningapp.adapter.MaterialsAdapter
import com.example.learningapp.databinding.ActivityMainBinding
import com.example.learningapp.model.Material
import com.example.learningapp.model.User
import com.example.learningapp.presentation.content.ContentActivity
import com.example.learningapp.presentation.user.UserActivity
import com.example.learningapp.repository.Repository
import com.example.learningapp.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var materialsAdapter: MaterialsAdapter
    private lateinit var userDatabase: DatabaseReference
    private lateinit var materialDatabase: DatabaseReference
    private var currentUser: FirebaseUser? = null

    companion object{
        const val EXTRA_POSITION = "extra_position"
    }

    private var listenerUser = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val user = snapshot.getValue(User::class.java)
            user?.let {
                binding.apply {
                    tvNameUserMain.text = it.nameUser

                    Glide
                        .with(this@MainActivity)
                        .load(it.avatarUser)
                        .placeholder(android.R.color.darker_gray)
                        .into(ivAvatarMain)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[onCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message)
        }
    }

    private var listenerMaterials = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if (snapshot.value != null){
                showData()
                val json = Gson().toJson(snapshot.value)
                val type = object : TypeToken<MutableList<Material>>() {}.type
                val materials = Gson().fromJson<MutableList<Material>>(json, type)

                materials?.let { materialsAdapter.materials = it }
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("MainActivity", "[onCancelled] - ${error.message}")
            showDialogError(this@MainActivity, error.message)
        }
    }

    private fun showEmptyData() {
        binding.apply {
            ivEmptyData.visible()
            etSearchMain.disabled()
            rvMaterialsMain.gone()
        }
    }

    private fun showData() {
        binding.apply {
            ivEmptyData.gone()
            etSearchMain.enabled()
            rvMaterialsMain.visible()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init
        materialsAdapter = MaterialsAdapter()
        userDatabase = FirebaseDatabase.getInstance().getReference("users")
        materialDatabase = FirebaseDatabase.getInstance().getReference("materials")
        currentUser = FirebaseAuth.getInstance().currentUser

        getDataFirebase()
        // getDataMaterial()
        onAction()

    }

    private fun getDataFirebase() {
        showLoading()
        userDatabase
            .child(currentUser?.uid.toString())
            .addValueEventListener(listenerUser)

        materialDatabase
            .addValueEventListener(listenerMaterials)

        binding.rvMaterialsMain.adapter = materialsAdapter
    }

    override fun onResume() {
        super.onResume()
        if (intent != null){
            val position = intent.getIntExtra(EXTRA_POSITION, 0)
            binding.rvMaterialsMain.smoothScrollToPosition(position)
        }
    }

//    private fun getDataMaterial() {
//        showLoading()
//        val materials = Repository.getMaterials(this)
//
//        Handler(Looper.getMainLooper())
//            .postDelayed({
//                hideLoading()
//                materials?.let {
//                    materialsAdapter.materials = it
//                }
//            }, 1200)
//
//        binding.rvMaterialsMain.adapter = materialsAdapter
//    }

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
                getDataFirebase()
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