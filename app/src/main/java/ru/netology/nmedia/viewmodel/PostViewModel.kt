package ru.netology.nmedia.viewmodel

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val empty = Post(
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

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val edited = MutableLiveData(Post.empty)

    val data: LiveData<List<Post>> = repository.get()

    fun like(id: Long) = repository.like(id)

    fun repost(id: Long) = repository.repost(id)

    fun remove (id: Long) = repository.remove(id)

    fun edit (post: Post){
        edited.value = post
    }

    fun save (newContent: String){
        edited.value?.let { post ->
            if (post.content != newContent) {
                repository.save(post.copy(content = newContent))
            }
        }

        edited.value = empty
    }

    fun cancelEdit() {
        edited.value?.let { post ->
            repository.cancelEdit(post)
        }
        edited.value = Post.empty
    }
}




