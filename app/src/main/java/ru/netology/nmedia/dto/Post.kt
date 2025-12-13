package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int,
    val likeByMe: Boolean = false,
    val reposts: Int,
    val repostByMe: Boolean = false,
    val views: Int,
) {
    companion object {
        val empty = Post(
            id = 0L,
            author = "",
            published = "",
            content = "",
            likes = 0,
            likeByMe = false,
            reposts = 0,
            repostByMe = false,
            views = 0,
        )
    }
}
