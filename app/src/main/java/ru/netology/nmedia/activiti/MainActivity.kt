package ru.netology.nmedia.activiti

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = PostsAdapter(object : OnInteractionListener {

            override fun onLike(post: Post) {
                viewModel.like(post.id)
            }

            override fun onRepost(post: Post) {
                viewModel.repost(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.remove(post.id)
            }

            override fun onEdit(post: Post){
                viewModel.edit(post)

            }
        }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)

        }
        viewModel.edited.observe(this) { edited ->
            if (edited.id != 0L){
                AndroidUtils.hideKeyboard(binding.content)
                binding.content.setText(edited.content)
                binding.textPost.text = edited.content
                binding.group.visibility = View.VISIBLE
                binding.content.requestFocus()
            } else {
                binding.group.visibility = View.GONE
            }
        }


        binding.save.setOnClickListener {
            val currentText = binding.content.text?.trim()?.toString()

            if(currentText.isNullOrBlank()){
                Toast.makeText(this, R.string.post_content_is_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.save(currentText)

            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
            binding.group.visibility = View.GONE
        }

        binding.cancelEdit.setOnClickListener {
            viewModel.cancelEdit()

            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
            binding.group.visibility = View.GONE
        }
    }
}