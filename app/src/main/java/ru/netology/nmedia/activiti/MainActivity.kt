package ru.netology.nmedia.activiti

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.viewModel.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.repository.formatNumber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
        //    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        //    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        //    insets
        //}

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likes.text = formatNumber(post.likes)
                reposts.text = formatNumber(post.reposts)
                favorite.setImageResource(
                    if (post.likeByMe) R.drawable.baseline_favorite_24 else R.drawable.outline_favorite_border_24
                )
            }
        }
        binding.favorite.setOnClickListener {
            viewModel.like()
        }

        binding.arrow.setOnClickListener {
            viewModel.repost()
        }
    }
}