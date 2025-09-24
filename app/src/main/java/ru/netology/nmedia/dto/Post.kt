package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int,
    val likeByMe: Boolean = false,
    val reposts: Int,
    val repostByMe: Boolean = false,
    val views: Int,
)
