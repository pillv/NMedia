package ru.netology.nmedia.viewmodel

import PostRepositoryFiles
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository

private val EMPTY_POST = Post(
    id = 0L,
    author = "",
    published = "",
    content = "",
    likes = 0,
    likeByMe = false,
    reposts = 0,
    repostByMe = false,
    views = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = try {
        PostRepositoryFiles(application)
    } catch (e: Exception) {
        e.printStackTrace()
        object : PostRepository {
            private val posts = mutableListOf<Post>()
            private val data = MutableLiveData<List<Post>>(posts.toList())

            override fun get(): LiveData<List<Post>> = data

            override fun save(post: Post) {
                if (post.id == 0L) {
                    val newId = (posts.maxByOrNull { it.id }?.id ?: 0L) + 1L
                    posts.add(post.copy(id = newId))
                } else {
                    posts.replaceAll { if (it.id == post.id) post else it }
                }
                data.value = posts.toList()
            }

            override fun like(id: Long) {
                posts.replaceAll { if (it.id == id) it.copy(likes = it.likes + 1, likeByMe = !it.likeByMe) else it }
                data.value = posts.toList()
            }

            override fun repost(id: Long) {
                posts.replaceAll { if (it.id == id) it.copy(reposts = it.reposts + 1, repostByMe = true) else it }
                data.value = posts.toList()
            }

            override fun remove(id: Long) {
                posts.removeAll { it.id == id }
                data.value = posts.toList()
            }

            override fun cancelEdit(post: Post) {
            }
        }
    }

    val edited = MutableLiveData(EMPTY_POST)
    val data: LiveData<List<Post>> = repository.get()

    fun like(id: Long) = repository.like(id)


    fun repost(id: Long) = repository.repost(id)

    fun remove(id: Long) = repository.remove(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun save(newContent: String) {
        edited.value?.let { post ->
            if (post.content != newContent) {
                repository.save(post.copy(content = newContent))
            }
        }
        edited.value = EMPTY_POST
    }

    fun cancelEdit() {
        edited.value?.let { post ->
            repository.cancelEdit(post)
        }
        edited.value = EMPTY_POST
    }
}





