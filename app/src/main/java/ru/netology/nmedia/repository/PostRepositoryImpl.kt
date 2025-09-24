package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

fun formatNumber(number: Int): String {
    return when {
        number < 1000 -> number.toString()
        number < 10000 -> {
            val thousands = number / 1000
            val hundreds = (number % 1000) / 100
            "$thousands.$hundreds" + "к"
        }
        number < 1000000 -> {
            (number / 1000).toString() + "K"
        }
        else -> {
            val millions = number / 1000000
            val hundredThousands = (number % 1000000) / 100000
            "$millions.$hundredThousands" + "M"
        }
    }
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likes = 999,
        reposts = 1199,
        views = 12090)
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data
    override fun like() {
        post = post.copy(likeByMe = !post.likeByMe, likes = if (post.likeByMe) post.likes - 1 else post.likes + 1)
        data.value = post
    }

    override fun repost() {
       post = post.copy(repostByMe = !post.repostByMe, reposts = post.reposts + 1)
        data.value = post
    }
}