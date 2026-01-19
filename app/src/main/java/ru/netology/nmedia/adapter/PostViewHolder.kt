package ru.netology.nmedia.adapter

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import androidx.core.net.toUri
import formatNumber

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            favorite.text = formatNumber(post.likes)
            arrow.text = formatNumber(post.reposts)
            favorite.isChecked = post.likeByMe

            favorite.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            arrow.setOnClickListener {
                onInteractionListener.onRepost(post)
            }

            more.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            if (!post.video.isNullOrBlank()) {
                videoBlock.visibility = View.VISIBLE

                val openVideo: (View) -> Unit = { v ->
                    try {
                        val uri = post.video!!.toUri()
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        v.context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(v.context, v.context.getString(R.string.cant_open_link), Toast.LENGTH_SHORT).show()
                    }
                }



                videoBlock.setOnClickListener(openVideo)
                videoPreview.setOnClickListener(openVideo)
                videoPlay.setOnClickListener(openVideo)
            } else {
                videoBlock.visibility = View.GONE
                videoBlock.setOnClickListener(null)
                videoPreview.setOnClickListener(null)
                videoPlay.setOnClickListener(null)
            }
        }
    }
}