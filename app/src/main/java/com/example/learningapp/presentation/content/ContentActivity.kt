package com.example.learningapp.presentation.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.viewpager.widget.ViewPager
import com.example.learningapp.adapter.PagesAdapter
import com.example.learningapp.databinding.ActivityContentBinding
import com.example.learningapp.model.Material
import com.example.learningapp.model.PagesItem
import com.example.learningapp.presentation.main.MainActivity
import com.example.learningapp.repository.Repository
import com.example.learningapp.utils.invisible
import com.example.learningapp.utils.disabled
import com.example.learningapp.utils.enabled
import com.example.learningapp.utils.visible
import org.jetbrains.anko.startActivity

class ContentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityContentBinding
    private lateinit var pagesAdapter: PagesAdapter
    private var currentPosition = 0
    private var materialPosition = 0

    companion object{
        const val EXTRA_MATERIAL = "extra_material"
        const val EXTRA_POSITION = "extra_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init
        pagesAdapter = PagesAdapter(this)

        getDataIntent()
        onAction()
        viewPagerCurrentPosition()
    }

    private fun viewPagerCurrentPosition() {
        binding.vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val totalIndex = pagesAdapter.count
                currentPosition = position
                val textIndex = "${currentPosition + 1} / $totalIndex"
                binding.tvIndexContent.text = textIndex

                if (currentPosition == 0){
                    binding.btnPrevContent.invisible()
                    binding.btnPrevContent.disabled()
                }else{
                    binding.btnPrevContent.visible()
                    binding.btnPrevContent.enabled()
                }
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun getDataIntent() {
        if (intent != null){
            materialPosition = intent.getIntExtra(EXTRA_POSITION, 0)
            val material = intent.getParcelableExtra<Material>(EXTRA_MATERIAL)

            binding.tvTitleContent.text = material?.titleMaterial

            material?.let { getDataContent(material) }
        }
    }

    private fun getDataContent(material: Material) {
        showLoading()
        val content = material.idMaterial?.let { Repository.getContents(this)?.get(it) }

        Handler(Looper.getMainLooper())
            .postDelayed({
                hideLoading()

                pagesAdapter.pages = content?.pages as MutableList<PagesItem>
                binding.vpContent.adapter = pagesAdapter
                binding.vpContent.setPagingEnabled(false)

                //Init untuk tampilan awal index
                val textIndex = "${currentPosition + 1} / ${pagesAdapter.count}"
                binding.tvIndexContent.text = textIndex
            }, 1200)
    }

    private fun showLoading() {
        binding.swipeContent.isRefreshing = true
    }

    private fun hideLoading() {
        binding.swipeContent.isRefreshing = false
    }

    private fun onAction() {
        binding.apply {
            btnCloseContent.setOnClickListener { finish() }

            btnNextContent.setOnClickListener {
                if (currentPosition < pagesAdapter.count - 1){
                    binding.vpContent.currentItem += 1
                }else{
                    startActivity<MainActivity>(
                        MainActivity.EXTRA_POSITION to materialPosition + 1
                    )
                    finish()
                }
            }

            btnPrevContent.setOnClickListener {
                binding.vpContent.currentItem -= 1
            }

            swipeContent.setOnRefreshListener {
                getDataIntent()
            }
        }
    }
}