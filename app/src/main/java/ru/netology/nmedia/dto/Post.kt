package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int,
    var likeByMe: Boolean = false,
    var reposts: Int,
    var repostByMe: Boolean = false,
    var views: Int,
)
